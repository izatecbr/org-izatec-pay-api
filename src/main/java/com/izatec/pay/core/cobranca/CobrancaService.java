package com.izatec.pay.core.cobranca;

import com.izatec.pay.core.cadastro.*;
import com.izatec.pay.core.comum.*;
import com.izatec.pay.core.empresa.ConfiguracaoService;
import com.izatec.pay.core.empresa.Configuracao;
import com.izatec.pay.core.cobranca.pagamento.PagamentoRequest;
import com.izatec.pay.core.cobranca.pagamento.PagamentoService;
import com.izatec.pay.infra.security.RequisicaoInfo;
import com.izatec.pay.infra.util.Filtros;
import com.izatec.pay.infra.util.Identificacao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static com.izatec.pay.infra.Atributos.*;

@Service
@Slf4j
public class CobrancaService {
    @Autowired
    private CobrancaRepository repository;
    @Autowired
    private ConfiguracaoService empresaService;
    @Autowired
    private PagamentoService pagamentoService;
    @Autowired
    private CadastroService cadastroService;
    @Autowired
    private RequisicaoInfo requisicaoInfo;
    public Integer gerarCobranca(CobrancaRequest requisicao){
        NegociacaoRequest nr = requisicao.getNegociacao();
        int quantidadeParcelas = nr.getQuantidadeParcelas()==null ? 1 : nr.getQuantidadeParcelas();
        int proximaParcela = nr.getProximaParcela()==null?1:nr.getProximaParcela();
        LocalDate proximoVencimento= nr.getProximoVencimento()==null?LocalDate.now():nr.getProximoVencimento();

        Configuracao integracao = empresaService.get(requisicao.getCodigoIdentificacao());
        Cobranca entidade = definir(integracao, requisicao);
        Negociacao negociacao = new Negociacao();
        entidade.setQuantidadeParcelas(quantidadeParcelas);
        negociacao.setModelo(nr.getModelo());
        negociacao.setRecorrencia(nr.getRecorrencia());
        negociacao.setProximaParcela(proximaParcela==0?1:proximaParcela);
        negociacao.setProximoVencimento(proximoVencimento);
        negociacao.setDiaVencimento(negociacao.getProximoVencimento().getDayOfMonth());
        entidade.setNegociacao(negociacao);

        if(nr.getModelo() == PagamentoModelo.RECORRENTE){
            entidade.setQuantidadeParcelas(0);
            entidade.getNegociacao().setProximaParcela(1);
        }
        if(requisicao.getDataVigencia()!=null){
            entidade.setDataVigencia(Data.end(requisicao.getDataVigencia()));
        }
        return repository.save(entidade).getId();
    }

    private Cobranca definir(Configuracao configuracao, CobrancaRequest requisicao){
        Cobranca entidade = new Cobranca();
        entidade.setStatus(Status.ATIVA);
        entidade.setDataGeracao(Data.of());
        entidade.setCodigoExterno(Identificacao.codigoExterno(requisicao.getCodigoExterno()));
        entidade.setTitulo(requisicao.getTitulo());
        entidade.setEmpresa(configuracao.getEmpresa());
        entidade.setDescricao(requisicao.getDescricao()==null ? requisicao.getTitulo() : requisicao.getDescricao());
        entidade.getConfiguracao().setCnpj(configuracao.getCnpj());
        entidade.getConfiguracao().setNomeFantasia(configuracao.getNomeFantasia());
        entidade.getConfiguracao().setCodigoIdentificacao(configuracao.getCodigoIdentificacao());
        entidade.getConfiguracao().setEmpresa(configuracao.getEmpresa());
        entidade.setQuantidadeParcelas(requisicao.getNegociacao().getQuantidadeParcelas());
        entidade.setValorCobranca(requisicao.getValor());
        entidade.setValorCobrado(0.0);
        entidade.setEndereco(requisicao.getEndereco());
        entidade.setSacado(definirParceiro(configuracao.getEmpresa(), requisicao.getSacado()));
        return entidade;
    }

    private Parceiro definirParceiro(Integer empresa, ParceiroRequest requisicao){
        return cadastroService.atualizarCadastro(empresa, Parceiro.of(requisicao));
    }

    public void processarCobranca(List<Cobranca> cobrancas){
        cobrancas.stream().forEach(c->{
            try {
                log.info("Iniciando o processo de geração de pagamento de parcela da cobranca estabelecimento:{},modelo:{}, id:{}, ce:{}", c.getConfiguracao().getCnpj(), c.getNegociacao().getModelo().name(), c.getId(), c.getCodigoExterno());
                gerarPagamento(c);
            }catch (Exception exception){
                log.error("##ERRO:CobrancaJob.gerarPagamentoParcela: Ao tentar gerar a parcela da cobranca estabelecimento:{}, id:{}, ce:{}", c.getConfiguracao().getCnpj(), c.getId(), c.getCodigoExterno(), exception);
                exception.printStackTrace();
            }
        });
    }
    @Transactional
    private void gerarPagamento(Cobranca cobranca){

        Negociacao negociacao = cobranca.getNegociacao();
        PagamentoRequest requisicao = new PagamentoRequest();
        requisicao.setCodigoExterno(Identificacao.gerarCodigoExterno(cobranca.getId(), negociacao.getProximaParcela()));
        if(cobranca.getSacado()!=null){
            ParceiroRequest sacado = new ParceiroRequest();
            Cadastro cadastro = cadastroService.buscar(cobranca.getSacado().getId());
            if(cadastro!=null){
                sacado.setDocumento(cadastro.getDocumento());
                sacado.setNomeCompleto(cadastro.getNomeCompleto());
                requisicao.setSacado(sacado);
            }

        }
        requisicao.setMensagem(cobranca.getTitulo());
        requisicao.setCodigoIdentificacao(cobranca.getConfiguracao().getCodigoIdentificacao());
        requisicao.setValor(valorPagamento(cobranca));
        VencimentoRequest vencimentoRequest = new VencimentoRequest();
        vencimentoRequest.setData(negociacao.getProximoVencimento().toString());
        requisicao.setVencimento(vencimentoRequest);
        requisicao.setCobranca(cobranca.getId());
        requisicao.setParcela(negociacao.getProximaParcela());

        pagamentoService.gerarPagamento(requisicao);

        if(PagamentoModelo.UNICO==cobranca.getNegociacao().getModelo() || negociacao.getProximaParcela().equals(cobranca.getQuantidadeParcelas())){
            cobranca.setStatus(Status.FINALIZADA);
        }else{

            negociacao.setProximaParcela(negociacao.getProximaParcela()+1);
            negociacao.setProximoVencimento(negociacao.getRecorrencia().gerarProximaData(negociacao.getProximoVencimento(), negociacao.getDiaVencimento()));
        }
        repository.save(cobranca);
    }

    public List<Cobranca> listar(Filtros filtros){
        LocalDate dataInicio = filtros.getData(DATA_INICIO);
        LocalDate dataFim = filtros.getData(DATA_FIM);
        return repository.listar(requisicaoInfo.getEmpresa(), filtros.getInt(SACADO), filtros.getEnum(STATUS, Status.class), dataInicio, dataFim==null ? dataInicio : dataFim);
    }
    private Double valorPagamento(Cobranca cobranca){
        Negociacao negociacao = cobranca.getNegociacao();
        Double valorPagamento= cobranca.getValorCobranca();
        if(PagamentoModelo.PARCELADO== negociacao.getModelo()){
            int parcelas = cobranca.getQuantidadeParcelas();
            BigDecimal valor = new BigDecimal(cobranca.getValorCobranca()) ;
            BigDecimal valorParcela = valor.divide(new BigDecimal(parcelas), 2, RoundingMode.HALF_UP);
            valorPagamento = valorParcela.add(negociacao.getProximaParcela().equals(parcelas)?new BigDecimal("0.01"):BigDecimal.ZERO).doubleValue();
        }
        return valorPagamento;
    }

}
