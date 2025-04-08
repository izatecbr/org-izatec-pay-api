package com.izatec.pay.core.cobranca.pagamento;

import com.izatec.pay.core.cadastro.CadastroService;
import com.izatec.pay.core.cadastro.Parceiro;
import com.izatec.pay.core.cadastro.ParceiroRequest;
import com.izatec.pay.core.comum.*;
import com.izatec.pay.core.empresa.Configuracao;
import com.izatec.pay.core.empresa.ConfiguracaoService;
import com.izatec.pay.core.empresa.configuracao.Intermediadores;
import com.izatec.pay.infra.util.Identificacao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@Slf4j
public class PagamentoService {
    @Autowired
    private PagamentoRepository repository;
    @Autowired
    private ConfiguracaoService empresaService;
    @Autowired
    private CadastroService cadastroService;
    public PagamentoResponse gerarPagamento(PagamentoRequest requisicao){
        Configuracao configuracao = empresaService.get(requisicao.getCodigoIdentificacao());
        Pagamento entidade = definir(requisicao,configuracao);
        entidade.setCobranca(requisicao.getCobranca());
        entidade.setParcela(requisicao.getParcela());
        Data vencimento = vencimento(requisicao.getVencimento());
        entidade.setDataVencimento(vencimento);
        entidade.setDataPrevista(vencimento);
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
        return pagamento;
    }

    private Parceiro definirParceiro(Integer empresa, ParceiroRequest requisicao){
        return cadastroService.atualizarCadastro(empresa, Parceiro.of(requisicao));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PagamentoResponse integrarPagamento(Integer id){
        log.info("Iniciando a integração do pagamento id {}", id);
        Pagamento pagamento = repository.findById(id).orElse(null);
        Configuracao configuracao = empresaService.get(pagamento.getConfiguracao().getCodigoIdentificacao());
        pagamento.setConfiguracao(configuracao);
        PagamentoResponse response = new PagamentoResponse();
        PagamentoIntegracao integracao = pagamento.getIntegracao()==null ? new PagamentoIntegracao(): pagamento.getIntegracao();
        try {
            IntegracaoResponse integracaoResponse = Intermediadores.ONZ == configuracao.getIntermediador() ? integrarOnz(pagamento,configuracao) : integrarLytex(pagamento,configuracao);
            /*integracao.setDataHora(LocalDateTime.now());
            integracao.setConteudo(integracaoResponse.getConteudo());
            integracao.setLink(integracaoResponse.getLink());
            integracao.setIntermediador(configuracao.getIntermediador().name());
            pagamento.setStatus(integracaoResponse.getStatus());
            response.setStatus(pagamento.getStatus().name());
            response.setNumeroProtocolo(pagamento.getCodigoIdentificacao());
            response.setMensagem("Pagamento integrado com sucesso");*/
        }catch (Exception ex){
            log.error("#ERRO#PIX# ao tentar integrar o pagamento id {}-{} ao modobank", pagamento.getId(), pagamento.getCodigoIdentificacao() );
            log.error("Erro->",ex);
            pagamento.setStatus(PagamentoStatus.AGUARDANDO);
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
        return null;
    }
    private IntegracaoResponse integrarLytex(Pagamento pagamento, Configuracao configuracao ){
       return null;
    }


}
