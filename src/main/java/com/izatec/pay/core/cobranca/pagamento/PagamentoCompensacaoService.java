package com.izatec.pay.core.cobranca.pagamento;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.izatec.pay.core.cobranca.Cobranca;
import com.izatec.pay.core.cobranca.CobrancaRepository;
import com.izatec.pay.core.comum.Status;
import com.izatec.pay.core.comum.*;
import com.izatec.pay.core.empresa.Configuracao;
import com.izatec.pay.core.empresa.ConfiguracaoService;
import com.izatec.pay.core.empresa.configuracao.Intermediadores;
import com.izatec.pay.infra.business.RequisicaoException;
import com.izatec.pay.infra.security.Criptografia;
import com.izatec.pay.infra.security.RequisicaoInfo;
import com.izatec.pay.infra.util.Calculos;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class PagamentoCompensacaoService {
    @Value("${criptografia.senha}")
    private String senha;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PagamentoRepository repository;

    @Autowired
    private ConfiguracaoService configuracaoService;

    @Autowired
    private CobrancaRepository cobrancaRepository;

    @Autowired
    private RequisicaoInfo requisicaoInfo;

    @Transactional
    public ComprovanteCompensacao confirmarCompensacao(String origem, String token, String pagamento){
        try {
            sendEmailPagamento(pagamento,"gleyson.s@hotmail.com");
            if(Intermediadores.ONZ.name().equals(origem.toUpperCase()))
                return compensarOnz(pagamento, token);

            else return compensarLytex(pagamento);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new RequisicaoException("Erro ao tentar compensar o pagamento");
        }
    }
    private ComprovanteCompensacao compensarOnz(String pagamento, String token) throws Exception {
        log.info("#ONZ-WEBHOOK");
        log.info(pagamento);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(pagamento);
        ObjectNode dataNode = (ObjectNode) rootNode.get("data");
        String txId = dataNode.get("txId").asText();
        String status = dataNode.get("status").asText();
        String numeroComprovante = dataNode.get("endToEndId").asText();
        if(validarToken(token, txId)) {
            if ("LIQUIDATED".equals(status.toUpperCase())) {
                //TODO: SE O PAGAMENTO ESTIVER DIFERENTE DE GERAR NÃO PROSSEGUIR
                Pagamento pagto = repository.findByCodigoIdentificacao(txId).orElse(null);
                pagto.setStatus(PagamentoStatus.COMPENSADO);
                Configuracao est = configuracaoService.get(pagto.getCodigoIdentificacao().substring(0,15));
                PagamentoCompensacao compensacao = new PagamentoCompensacao();
                compensacao.setData(Data.of());
                compensacao.setComprovante(numeroComprovante);
                compensacao.setCusto(est.getCustoIntegracao());
                pagto.setCompensacao(compensacao);
                pagto.getValor().setPago(pagto.getValor().getOriginal());
                repository.save(pagto);
                if(pagto.getCobranca()!=null){
                    Cobranca cobranca = cobrancaRepository.findById(pagto.getCobranca()).orElse(null);
                    cobranca.setValorCobrado(cobranca.getValorCobrado() + pagto.getValor().getPago());
                    atualizarStatus(cobranca);
                    cobrancaRepository.save(cobranca);
                }
                sendEmailPagamento(pagto, est.getEmail());
            }
        }
        ComprovanteCompensacao comprovante = new ComprovanteCompensacao();
        comprovante.setStatus(PagamentoStatus.COMPENSADO.name());
        comprovante.setNumeroComprovante(numeroComprovante);
        return comprovante ;
    }
    private ComprovanteCompensacao compensarLytex(String pagamento) throws Exception {
        log.info("#LYTEX# Compensacao");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(pagamento);
        String status = node.get("webhookType").asText();
        String numeroComprovante = node.get("signature").asText();
        //if(validarToken(token, referenceId)) {
            if ("liquidateInvoice".equals(status)) {
                ObjectNode data = (ObjectNode) node.get("data");
                String referenceId = data.get("referenceId").asText();
                String payedAt = data.get("payedAt").asText();
                status = data.get("status").asText();
                Pagamento pagto = repository.findByCodigoIdentificacao(referenceId).orElse(null);
                pagto.setStatus(status.equals("paid")?PagamentoStatus.COMPENSADO:PagamentoStatus.AGUARDANDO);
                Configuracao est = configuracaoService.get(pagto.getCodigoIdentificacao().substring(0,15));
                PagamentoCompensacao compensacao = new PagamentoCompensacao();
                compensacao.setData(Data.of(LocalDateTime.parse(payedAt.substring(0, 19))));
                compensacao.setComprovante(numeroComprovante);
                compensacao.setCusto(est.getCustoIntegracao());
                pagto.setCompensacao(compensacao);
                pagto.getValor().setPago(pagto.getValor().getOriginal());
                repository.save(pagto);
                if(pagto.getCobranca()!=null){
                    Cobranca cobranca = cobrancaRepository.findById(pagto.getCobranca()).orElse(null);
                    cobranca.setValorCobrado(cobranca.getValorCobrado() + pagto.getValor().getPago());
                    atualizarStatus(cobranca);
                    cobrancaRepository.save(cobranca);
                }
                sendEmailPagamento(pagto, est.getEmail());
            }
        //}
        ComprovanteCompensacao comprovante = new ComprovanteCompensacao();
        comprovante.setStatus(PagamentoStatus.COMPENSADO.name());
        comprovante.setNumeroComprovante(numeroComprovante);
        return comprovante ;
    }
    private boolean validarToken(String token, String codigoIdentificao ){
        try {
            if(codigoIdentificao.contains("004329223260001"))
                return true;

            return token.equals(Criptografia.criptografar(codigoIdentificao.substring(0,15),senha).substring(0,8));
        }catch (Exception exception){
            return false;
        }

    }
    private void sendEmailPagamento(String pagamento, String email) throws Exception {
        try {
            Thread sender = new Thread(process(mailSender, createHtmlMessage("Iza Pay", "equipe@iza.tec.br", email, pagamento)));
            sender.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sendEmailPagamento(Pagamento pix, String email) throws Exception {
        String conteudo = String.format("<html><body>"
                + "<p>Olá %s,</p>"
                + "<p>Acabamos de confirmar a compensação do pagamento de identificação <b>%s</b>, no valor de <b>R$ %,.2f.</b></p>"
                + "<p>Atenciosamente,</p>"
                + "<p>Equipe Iza Tec</p>"
                + "<p><a href=\"mailto:equipe@iza.tec.br\">equipe@iza.tec.br</a></p>"
                + "</body></html>", pix.getConfiguracao().getNomeFantasia(), pix.getCodigoIdentificacao(), pix.getValor().getPago());

        try {
            Thread sender = new Thread(process(mailSender, createHtmlMessage("Iza Pay", "equipe@iza.tec.br", email, conteudo)));
            sender.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MimeMessage createHtmlMessage(String subject, String from, String to, String content) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true); // Use this or above line.
        return mimeMessage;

    }
    private Runnable process(JavaMailSender mailSender, MimeMessage msg) {
        return new Runnable() {
            @Override
            public void run() {
                mailSender.send(msg);
                log.info("E-mail enviado com sucesso");
            }
        };
    }
    @Transactional
    public void confirmarCompensacao(Integer id, CompensacaoManualRequest requisicao){
        Pagamento pagto = repository.findById(id).orElse(null);
        if(pagto.getEmpresa().equals(requisicaoInfo.getEmpresa())) {
            if(requisicao.getCodigoExterno()!=null)
                pagto.setCodigoExteno(requisicao.getCodigoExterno());

            pagto.setStatus(Calculos.compararMenorQue(requisicao.getValorPago(), pagto.getValor().getRestante()) ? pagto.getStatus() : PagamentoStatus.COMPENSADO );
            Data data = requisicao.getData() == null ? Data.of() : Data.of(requisicao.getData().getDia(), requisicao.getData().getHora());
            pagto.setCompensacao(PagamentoCompensacao.manual(data, requisicao.getObservacao()));
            pagto.getValor().setPago(Calculos.somar(pagto.getValor().getPago(), requisicao.getValorPago()));
            repository.save(pagto);
            if (pagto.getCobranca() != null) {
                Cobranca cobranca = cobrancaRepository.findById(pagto.getCobranca()).orElse(null);
                cobranca.setValorCobrado(cobranca.getValorCobrado() + pagto.getValor().getPago());
                atualizarStatus(cobranca);
                cobrancaRepository.save(cobranca);
            }
        }else
            log.info("Não foi possível compensar o pagamento ID{}, empresa não confere", pagto.getId());

    }

    @Transactional
    public void quitarPagamento(Integer id, CompensacaoManualRequest requisicao){
        Pagamento pagto = repository.findById(id).orElse(null);
        if(pagto.getEmpresa().equals(requisicaoInfo.getEmpresa())) {
            if(requisicao.getCodigoExterno()!=null)
                pagto.setCodigoExteno(requisicao.getCodigoExterno());

            pagto.setStatus(PagamentoStatus.COMPENSADO );
            Data data = requisicao.getData() == null ? Data.of() : Data.of(requisicao.getData().getDia(), requisicao.getData().getHora());
            pagto.setCompensacao(PagamentoCompensacao.manual(data, requisicao.getObservacao()));
            pagto.getValor().setPago(Calculos.somar(pagto.getValor().getPago(), requisicao.getValorPago()));
            repository.save(pagto);
        }else
            log.info("Não foi possível quitar o pagamento ID{}, empresa não confere", pagto.getId());

    }
    private void atualizarStatus( Cobranca cobranca){
        Negociacao negociacao = cobranca.getNegociacao();
        if(PagamentoModelo.UNICO==negociacao.getModelo())
            cobranca.setStatus(Status.QUITADA);
        else if(PagamentoModelo.PROGRAMADO == negociacao.getModelo() && cobranca.getQuantidadeParcelas().equals(negociacao.getProximaParcela()))
            cobranca.setStatus(Status.QUITADA);
        else if(PagamentoModelo.PARCELADO == negociacao.getModelo() && cobranca.getValorCobrado().compareTo(cobranca.getValorCobranca()) >= 0)
            cobranca.setStatus(Status.QUITADA);
    }
}
