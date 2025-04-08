package com.izatec.pay.core.notificacao.template;

import com.izatec.pay.core.cadastro.Cadastro;
import com.izatec.pay.core.cadastro.CadastroRepository;
import com.izatec.pay.core.cadastro.Parceiro;
import com.izatec.pay.core.cobranca.Cobranca;
import com.izatec.pay.core.cobranca.CobrancaRepository;
import com.izatec.pay.core.cobranca.pagamento.Pagamento;
import com.izatec.pay.core.cobranca.pagamento.PagamentoRepository;
import com.izatec.pay.infra.email.Mensagem;
import com.izatec.pay.infra.email.ServicoEmail;
import com.izatec.pay.infra.security.RequisicaoInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class NotificacaoCobrancaPagamentoEmail {
    @Value("${qrcode.url}")
    private String url;
    @Autowired
    private RequisicaoInfo requisicaoInfo;
    @Autowired
    private PagamentoRepository repository;
    @Autowired
    private CobrancaRepository cobrancaRepository;
    @Autowired
    private CadastroRepository cadastroRepository;
    @Autowired
    private ServicoEmail servicoEmail;
    public void notificarPagamento(Integer id){
        notificarPagamento(requisicaoInfo.getEmpresa(), id);
    }
    public void notificarPagamento(Integer empresa, Integer id){
       try {
           Pagamento pagto = repository.findByEmpresaAndId(empresa, id).orElse(null);
           if (pagto != null) {
               if (pagto.getCobranca() == null && pagto.getSacado() == null && pagto.getSacado().getId() ==null)
                   return;

               Cobranca cob = cobrancaRepository.findById(pagto.getCobranca()).orElse(null);
               Parceiro sacado = pagto.getSacado();
               StringBuilder conteudo = new StringBuilder();

               conteudo.append("<body>");
               conteudo.append(String.format("<p>Olá %s,</p><p>Segue dados e QrCode para que você possa efetuar o pagamento conforme detalhes abaixo:</p>", sacado.getNomeCompleto()));
               conteudo.append(String.format("<p><b>Favorecido:</b> %s - %s ", cob.getConfiguracao().getCnpj(), cob.getConfiguracao().getNomeFantasia()));
               conteudo.append(String.format("<p><b>Contrato:</b> %s", cob.getTitulo()));
               conteudo.append(String.format("<p><b>Nr.° Parcela:</b> %d de %d", cob.getNegociacao().getProximaParcela(), cob.getQuantidadeParcelas()));
               conteudo.append(String.format("<p><b>R$ Parcela:</b> %,.2f", pagto.getValor().getOriginal()));
               conteudo.append(String.format("<p><b>Vencimento:</b> %s </p>", DateTimeFormatter.ofPattern("dd/MM/yyyy").format(pagto.getDataVencimento().getDia())));
               conteudo.append(String.format("<p><b>Pix copia-cola:</b></p> <p>%s</p> ", pagto.getIntegracao().getConteudo()));
               String urlQrCode = String.format("%s/publico/pagamentos/%d/qrcode",url,  pagto.getId());
               //log.info("URL-QRCODE: {}", urlQrCode);
               conteudo.append("<img src=\"" + urlQrCode + "\" alt=\"IzaPay\" width=\"250\" height=\"250\">");
               conteudo.append("<p>Atenciosamente,</p>");
               conteudo.append("<p>Iza Pay - Soluções em pagamento</p>");
               conteudo.append("</body>");

               Cadastro sacadoCadastro = cadastroRepository.findById(pagto.getSacado().getId()).orElse(null);
               log.info("Enviando email para o Sacado: {}-{}-{}",sacadoCadastro.getId(), sacadoCadastro.getNomeCompleto(),  sacadoCadastro.getEmail());

               Mensagem mensagem = new Mensagem();
               mensagem.setTitulo(String.format("%s-%s",pagto.getConfiguracao().getNomeFantasia(), pagto.getMensagem()));
               mensagem.setCorpo(conteudo.toString());
               mensagem.setDestinatario(sacadoCadastro.getEmail());
               mensagem.setRemetente("equipe@iza.tec.br");
               servicoEmail.enviar(mensagem, true);
               log.info("Email enviado com sucesso para o Sacado: {}-{}-{}",sacadoCadastro.getId(), sacadoCadastro.getNomeCompleto(),  sacadoCadastro.getEmail());
           }
       }catch (Exception exception){
           exception.printStackTrace();
       }
    }
}
