package com.izatec.pay.core.previsao;

import com.izatec.pay.core.cadastro.Cadastro;
import com.izatec.pay.core.cadastro.CadastroService;
import com.izatec.pay.core.cadastro.Parceiro;
import com.izatec.pay.core.cadastro.ParceiroRequest;
import com.izatec.pay.core.cobranca.Cobranca;
import com.izatec.pay.core.comum.Status;
import com.izatec.pay.core.comum.*;
import com.izatec.pay.core.previsao.aplicacao.Aplicacao;
import com.izatec.pay.core.previsao.aplicacao.AplicacaoRequest;
import com.izatec.pay.core.previsao.despesa.DespesaRequest;
import com.izatec.pay.core.previsao.despesa.DespesaService;
import com.izatec.pay.core.empresa.ConfiguracaoService;
import com.izatec.pay.infra.Atributos;
import com.izatec.pay.infra.Entidades;
import com.izatec.pay.infra.business.RegistroNaoLocalizadoException;
import com.izatec.pay.infra.business.RequisicaoInvalidaException;
import com.izatec.pay.infra.security.RequisicaoInfo;
import com.izatec.pay.infra.util.Filtros;
import com.izatec.pay.infra.util.Formatacao;
import com.izatec.pay.infra.util.Identificacao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.izatec.pay.infra.Atributos.*;
import static com.izatec.pay.infra.Atributos.STATUS;

@Service
@Slf4j
public class PrevisaoService {
    @Autowired
    private PrevisaoRepository repository;
    @Autowired
    private ConfiguracaoService empresaService;
    @Autowired
    private CadastroService cadastroService;
    @Autowired
    private DespesaService despesaService;
    @Autowired
    private RequisicaoInfo requisicaoInfo;
    public Integer gerar(PrevisaoRequest requisicao){

        NegociacaoRequest nr = requisicao.getNegociacao();
        int quantidadeParcelas = nr.getQuantidadeParcelas()==null ? 1 : nr.getQuantidadeParcelas();
        int proximaParcela = nr.getProximaParcela()==null?1:nr.getProximaParcela();
        LocalDate proximoVencimento= nr.getProximoVencimento()==null?LocalDate.now():nr.getProximoVencimento();

        Previsao entidade = definir(requisicao);

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
        return repository.save(entidade).getId();
    }

    private Previsao definir(PrevisaoRequest requisicao){
        Previsao entidade = new Previsao();
        entidade.setStatus(Status.ATIVA);
        entidade.setDataGeracao(Data.of());
        entidade.setCodigoExteno(Identificacao.codigoExterno(requisicao.getCodigoExteno()));
        entidade.setTitulo(requisicao.getTitulo());
        entidade.setEmpresa(requisicaoInfo.getEmpresa());
        entidade.setDescricao(requisicao.getDescricao()==null ? requisicao.getTitulo() : requisicao.getDescricao());
        entidade.setQuantidadeParcelas(requisicao.getNegociacao().getQuantidadeParcelas());
        entidade.setValorDespesa(requisicao.getValor());
        entidade.setValorPago(0.0);
        entidade.setAplicacao(definirAplicacao(requisicao.getAplicacao()));
        entidade.setFavorecido(definirParceiro(requisicao.getFavorecido(),entidade.getEmpresa()));
        return entidade;
    }
    private Aplicacao definirAplicacao(AplicacaoRequest requisicao){
        if(requisicao==null){
            return null;
        }
        Aplicacao aplicacao = new Aplicacao();
        aplicacao.setCategoria(requisicao.getCategoria());
        aplicacao.setGrupo(requisicao.getGrupo());
        return aplicacao;
    }
    private Parceiro definirParceiro(ParceiroRequest requisicao,Integer empresa){
        return cadastroService.atualizarCadastro(empresa, Parceiro.of(requisicao));
    }
    public void processarPrevisao(List<Previsao> despesas){
        despesas.stream().forEach(c->{
            try {
                //log.info("Iniciando o processo de geração de pagamento de parcela da cobranca estabelecimento:{},modelo:{}, id:{}, ce:{}", c.getConfiguracao().getCnpj(), c.getNegociacao().getModelo().name(), c.getId(), c.getCodigoExteno());
                Double valorPagamento= PagamentoModelo.PARCELADO== c.getNegociacao().getModelo() ? new BigDecimal(c.getValorDespesa()).divide(new BigDecimal(c.getQuantidadeParcelas()), 2, RoundingMode.HALF_UP).doubleValue() : c.getValorDespesa();
                gerarPagamento(c, valorPagamento);
            }catch (Exception exception){
                //log.error("##ERRO:CobrancaJob.gerarPagamentoParcela: Ao tentar gerar a parcela da cobranca estabelecimento:{}, id:{}, ce:{}", c.getConfiguracao().getCnpj(), c.getId(), c.getCodigoExteno(), exception);
                exception.printStackTrace();
            }
        });
    }
    public PagamentoResponse gerarPagamento(Integer id){
        Previsao cobranca = repository.findById(id).orElseThrow(()-> new RegistroNaoLocalizadoException(Entidades.COBRANCA, Atributos.ID, id));
        if(cobranca.getStatus() == Status.ATIVA)
            return gerarPagamento(cobranca, cobranca.getValorDespesa());
        else
            throw new RequisicaoInvalidaException("Previsão já foi quitada ou finalizada.");

    }
    @Transactional
    private PagamentoResponse gerarPagamento(Previsao previsao, Double valorPagamento){
        Negociacao negociacao = previsao.getNegociacao();
        DespesaRequest requisicao = new DespesaRequest();
        requisicao.setCodigoExterno(Identificacao.gerarCodigoExterno(previsao.getId(), negociacao.getProximaParcela()));
        if(previsao.getFavorecido()!=null){
            ParceiroRequest favorecido = new ParceiroRequest();
            Cadastro cadastro = cadastroService.buscar(previsao.getFavorecido().getId());
            if(cadastro!=null) {
                favorecido.setDocumento(cadastro.getDocumento());
                favorecido.setNomeCompleto(cadastro.getNomeCompleto());
                favorecido.setId(cadastro.getId());
                requisicao.setFavorecido(favorecido);
            }
        }
        requisicao.setMensagem(String.format("Pcl%03d-%s", negociacao.getProximaParcela(), previsao.getTitulo()));
        requisicao.setValor(valorPagamento);
        VencimentoRequest vencimentoRequest = new VencimentoRequest();
        vencimentoRequest.setData(negociacao.getProximoVencimento().toString());
        requisicao.setVencimento(vencimentoRequest);
        requisicao.setPrevisao(previsao.getId());
        requisicao.setParcela(negociacao.getProximaParcela());
        requisicao.setAplicacao(definirAplicacaoRequest(previsao.getAplicacao()));
        requisicao.setPrevisao(previsao.getId());
        PagamentoResponse response = despesaService.gerarDespesa(requisicao, previsao.getEmpresa());

        if(PagamentoModelo.RECORRENTE!=previsao.getNegociacao().getModelo() && negociacao.getProximaParcela()==previsao.getQuantidadeParcelas()){
            previsao.setStatus(Status.FINALIZADA);
        }else{
            negociacao.setProximaParcela(negociacao.getProximaParcela()+1);
            negociacao.setProximoVencimento(negociacao.getRecorrencia().gerarProximaData(negociacao.getProximoVencimento(), negociacao.getDiaVencimento()));
        }
        repository.save(previsao);
        return response;
    }
    private AplicacaoRequest definirAplicacaoRequest(Aplicacao aplicacao ){
        if(aplicacao==null){
            return null;
        }
        AplicacaoRequest requisicao = new AplicacaoRequest();
        requisicao.setCategoria(aplicacao.getCategoria());
        requisicao.setGrupo(aplicacao.getGrupo());
        return requisicao;
    }
    public List<Previsao> listar(Filtros filtros){
        String dataInicio = filtros.getStringData(DATA_INICIO);
        String dataFim = filtros.getStringData(DATA_FIM);
        Integer empresa = requisicaoInfo.getEmpresa();
        Integer favorecido = filtros.getInt(FAVORECIDO);
        Status status = filtros.getEnum(STATUS, Status.class);
        return repository.listar(empresa,favorecido,status, dataInicio, dataFim);
    }
    @Transactional
    public String cancelar(Integer id, CancelamentoRequest requisicao){
        Previsao previsao = repository.findById(id).orElseThrow(()-> new RegistroNaoLocalizadoException(Entidades.PREVISAO, Atributos.ID, id));
        previsao.setStatus(Status.CANCELADA);
        String observacao = Optional.ofNullable(previsao.getObservacao())
                .map(obs -> obs + System.lineSeparator())
                .orElse("") + requisicao.getMotivo();
        previsao.setObservacao(observacao.concat(" - ").concat(Formatacao.dataHora(LocalDateTime.now())));
        repository.save(previsao);
        return previsao.getStatus().getNome();
    }
}
