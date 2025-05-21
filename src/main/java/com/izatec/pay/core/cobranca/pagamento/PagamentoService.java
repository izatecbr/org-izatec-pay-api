package com.izatec.pay.core.cobranca.pagamento;

import com.izatec.pay.core.cadastro.CadastroService;
import com.izatec.pay.core.cadastro.Parceiro;
import com.izatec.pay.core.cadastro.ParceiroRequest;
import com.izatec.pay.core.comum.*;
import com.izatec.pay.core.comum.titulo.Titulo;
import com.izatec.pay.core.comum.titulo.TituloParticipante;
import com.izatec.pay.core.comum.titulo.TituloValor;
import com.izatec.pay.core.empresa.Configuracao;
import com.izatec.pay.core.empresa.ConfiguracaoService;
import com.izatec.pay.core.empresa.configuracao.Intermediadores;
import com.izatec.pay.infra.security.RequisicaoInfo;
import com.izatec.pay.infra.util.Formatacao;
import com.izatec.pay.infra.util.GoogleImagem;
import com.izatec.pay.infra.util.Identificacao;
import com.izatec.pay.infra.util.Validacoes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class PagamentoService {
    @Autowired
    private PagamentoRepository repository;
    @Autowired
    private ConfiguracaoService configuracaoService;
    @Autowired
    private CadastroService cadastroService;
    @Autowired
    private RequisicaoInfo requisicaoInfo;
    public PagamentoResponse gerarPagamento(PagamentoRequest requisicao){
        if(requisicao.isCompensacaoManual())
            return gerarPagamentoManual(requisicao);

        else return gerarPagamentoIntegracao(requisicao);
    }
    private PagamentoResponse gerarPagamentoManual(PagamentoRequest requisicao){
        Configuracao configuracao = Configuracao.of(requisicaoInfo.getEmpresa());
        Pagamento entidade = definir(requisicao,configuracao);
        repository.save(entidade);

        PagamentoResponse response = new PagamentoResponse();
        response.setId(entidade.getId());
        response.setNumeroProtocolo(entidade.getCodigoIdentificacao());
        response.setStatus(entidade.getStatus().getId());
        response.setMensagem("Pagamento gerado com sucesso");
        return response;
    }
    private PagamentoResponse gerarPagamentoIntegracao(PagamentoRequest requisicao){
        Configuracao configuracao = configuracaoService.get(requisicao.getCodigoIdentificacao());
        Pagamento entidade = definir(requisicao,configuracao);
        repository.save(entidade);

        return integrarPagamento(entidade.getId());
    }
    private  Data vencimento (VencimentoRequest vencimentoRequest){
        Data vencimento = Data.end();
        if(vencimentoRequest!=null) {
            vencimento.setDia(LocalDate.parse(vencimentoRequest.getData()));
            if(vencimentoRequest.getHora()==null)
                vencimento.setHora(LocalTime.of(23,59));
            else
                vencimento.setHora(LocalTime.parse(vencimentoRequest.getHora()));
        }
        return vencimento;
    }
    private Pagamento definir(PagamentoRequest requisicao, Configuracao configuracao){
        Pagamento pagamento = new Pagamento();
        pagamento.setDataGeracao(Data.of());
        pagamento.setCodigoExteno(Identificacao.codigoExterno(requisicao.getCodigoExterno()));
        pagamento.setValor(Valor.of(requisicao.getValor()));
        pagamento.setStatus(PagamentoStatus.GERADO);
        pagamento.setCodigoIdentificacao(Identificacao.codigo(configuracao.getCodigoIdentificacao(), pagamento.getCodigoExteno()));
        pagamento.getConfiguracao().setCnpj(configuracao.getCnpj());
        pagamento.getConfiguracao().setNomeFantasia(configuracao.getNomeFantasia());
        pagamento.getConfiguracao().setCodigoIdentificacao(configuracao.getCodigoIdentificacao());
        pagamento.setEmpresa(configuracao.getEmpresa());
        pagamento.setMensagem(requisicao.getMensagem());
        pagamento.setObservacao(requisicao.getObservacao());
        pagamento.setParcela(requisicao.getParcela());
        pagamento.setSacado(definirParceiro(configuracao.getEmpresa(), requisicao.getSacado()));
        pagamento.getNotificacao().setWhatsapp(requisicao.getNotificacao().isWhatsapp());
        pagamento.getNotificacao().setEmail(requisicao.getNotificacao().isEmail());
        pagamento.setCobranca(requisicao.getCobranca());
        pagamento.setParcela(requisicao.getParcela());
        Data vencimento = vencimento(requisicao.getVencimento());
        pagamento.setDataVencimento(vencimento);
        pagamento.setDataPrevista(vencimento);
        return pagamento;
    }

    private Parceiro definirParceiro(Integer empresa, ParceiroRequest requisicao){
        return cadastroService.atualizarCadastro(empresa, Parceiro.of(requisicao));
    }
    private PagamentoResponse integrarPagamento(Integer id){
        return integrarPagamento(id,null);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PagamentoResponse integrarPagamento(Integer id, Data dataVencimento){
        log.info("Iniciando a integração do pagamento id {}", id);
        Pagamento pagamento = repository.findById(id).orElse(null);
        Configuracao configuracao = configuracaoService.get(pagamento.getConfiguracao().getCodigoIdentificacao());

        if(dataVencimento!=null) {
            pagamento.setCodigoIdentificacao(Identificacao.codigo(configuracao.getCodigoIdentificacao(), pagamento.getCodigoExteno()));
            pagamento.setDataVencimento(dataVencimento);
        }

        pagamento.setConfiguracao(configuracao);
        PagamentoResponse response = new PagamentoResponse();
        PagamentoIntegracao integracao = pagamento.getIntegracao()==null ? new PagamentoIntegracao(): pagamento.getIntegracao();
        try {
            IntegracaoResponse integracaoResponse = Intermediadores.ONZ == configuracao.getIntermediador() ? integrarOnz(pagamento,configuracao) : integrarLytex(pagamento,configuracao);
            integracao.setDataHora(LocalDateTime.now());
            integracao.setConteudo(integracaoResponse.getConteudo());
            integracao.setLink(integracaoResponse.getLink());
            integracao.setIntermediador(configuracao.getIntermediador().name());
            integracao.setErro(integracaoResponse.isErro());
            pagamento.setStatus(integracaoResponse.getStatus());
            response.setStatus(pagamento.getStatus().name());
            //response.setNumeroProtocolo(pagamento.getCodigoIdentificacao());
            response.setMensagem("Pagamento integrado com sucesso");
            log.info("Pagamento id {}-{} integrado com sucesso ao modobank para a data de vencimento: {}", pagamento.getId(), pagamento.getCodigoIdentificacao(), pagamento.getDataVencimento().getDia());
        }catch (Exception ex){
            log.error("#ERRO#PIX# ao tentar integrar o pagamento id {}-{} ao modobank", pagamento.getId(), pagamento.getCodigoIdentificacao() );
            log.error("Erro->",ex);
            //pagamento.setStatus(PagamentoStatus.AGUARDANDO);
            integracao.setErro(true);
            response.setStatus("ERRO");
            response.setMensagem("Erro ao tentar integrar o pagamento ao modobank");
        }
        integracao.incrementarTentativas();
        pagamento.setIntegracao(integracao);
        response.setId(pagamento.getId());
        response.setNumeroProtocolo(pagamento.getCodigoIdentificacao());
        repository.save(pagamento);

        return response;
    }
    private IntegracaoResponse integrarOnz(Pagamento pagamento, Configuracao configuracao ){

        //IZAPAY
        IntegracaoResponse response = new IntegracaoResponse();
        response.setStatus( PagamentoStatus.GERADO);
        //response.setConteudo(responseDto.pixCopiaECola());
        //response.setErro(!"ATIVA".equalsIgnoreCase(responseDto.status()));
        response.setLink("");
        return  response;
    }
    private IntegracaoResponse integrarLytex(Pagamento pagamento, Configuracao configuracao ){
        PagamentoIntegracaoRequest integracaoRequest = new PagamentoIntegracaoRequest();
        integracaoRequest.setCpfCnpj(pagamento.getSacado().getDocumento());
        integracaoRequest.setNomeCliente(pagamento.getSacado().getNomeCompleto());
        integracaoRequest.setMensagem(pagamento.getMensagem());
        integracaoRequest.setValor(pagamento.getValor().getOriginal());
        integracaoRequest.setDataVencimento(pagamento.getDataVencimento().getDia());
        integracaoRequest.setCodigoIdentificacao(pagamento.getCodigoIdentificacao());
        integracaoRequest.setNumeroParcelas(pagamento.getCobranca()==null ? 1: repository.buscarQuantidadeParcelas(pagamento.getCobranca()));
         //IZAPAY
        IntegracaoResponse response = new IntegracaoResponse();
        response.setStatus(PagamentoStatus.GERADO);
        response.setConteudo("");
        //response.setLink(invoice.getPaymentLink());
        return  response;
    }

    public Titulo gerarTitulo(Integer id) throws Exception{
        Pagamento pagamento = repository.findById(id).orElse(null);
        return  gerarTitulo(pagamento);
    }
    public Titulo gerarTitulo(Pagamento pagamento) throws Exception{
        Titulo titulo = new Titulo();
        titulo.setDataEmissao(Formatacao.data(LocalDate.now()));
        titulo.setDataVencimento(Formatacao.dataHora(pagamento.getDataVencimento().getDataHora()));
        titulo.setParcela(String.format("%06d", pagamento.getParcela()));
        titulo.setDocumento(pagamento.getCodigoExteno());
        titulo.setNumeroCobranca(pagamento.getCobranca()==null ? "" : String.format("%04d", pagamento.getCobranca()) );

        byte[] imagemBytes = GoogleImagem.gerarQRCode(pagamento.getIntegracao().getConteudo());
        titulo.setQrCode(imagemBytes);

        titulo.setValor(TituloValor.of(Formatacao.moeda(pagamento.getValor().getOriginal())));
        titulo.setPagador(TituloParticipante.of(pagamento.getSacado().getNomeCompleto(), pagamento.getSacado().getDocumento()));
        titulo.setFavorecido(TituloParticipante.of(pagamento.getConfiguracao().getNomeFantasia(), Formatacao.cpfCnpj(pagamento.getConfiguracao().getCnpj())));
        titulo.setEmitente(titulo.getFavorecido());

        return titulo;
    }

}
