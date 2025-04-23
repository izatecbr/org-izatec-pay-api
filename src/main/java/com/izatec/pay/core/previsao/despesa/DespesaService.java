package com.izatec.pay.core.previsao.despesa;

import com.izatec.pay.core.cadastro.CadastroRepository;
import com.izatec.pay.core.cadastro.CadastroService;
import com.izatec.pay.core.cadastro.Parceiro;
import com.izatec.pay.core.cadastro.ParceiroRequest;
import com.izatec.pay.core.comum.*;
import com.izatec.pay.core.previsao.aplicacao.Aplicacao;
import com.izatec.pay.core.previsao.aplicacao.AplicacaoRequest;
import com.izatec.pay.infra.security.RequisicaoInfo;
import com.izatec.pay.infra.util.Filtros;
import com.izatec.pay.infra.util.Identificacao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.izatec.pay.infra.Atributos.*;
import static com.izatec.pay.infra.Atributos.STATUS;

@Service
@Slf4j
public class DespesaService {
    @Autowired
    private CadastroRepository cadastroRepository;
    @Autowired
    private DespesaRepository repository;
    @Autowired
    private CadastroService cadastroService;
    @Autowired
    private RequisicaoInfo requisicaoInfo;
    public PagamentoResponse gerarDespesa(DespesaRequest requisicao){
        return gerarDespesa(requisicao, requisicaoInfo.getEmpresa());
    }
    public PagamentoResponse gerarDespesa(DespesaRequest requisicao, Integer empresa){
        Despesa entidade = definir(requisicao, empresa);
        Data vencimento = new Data();
        vencimento.setDia(LocalDate.parse(requisicao.getVencimento().getData()));
        if(requisicao.getVencimento().getHora()==null)
            vencimento.setHora(LocalTime.of(23,59));
        else
            vencimento.setHora(LocalTime.parse(requisicao.getVencimento().getHora()));

        entidade.setDataVencimento(vencimento);

        Despesa pagamento = repository.save(entidade);
        PagamentoResponse response = new PagamentoResponse();
        response.setNumeroProtocolo(pagamento.getId().toString());
        response.setStatus(pagamento.getStatus().name());
        response.setMensagem("Despesa gerada com sucesso");
        return response;
    }
    private Despesa definir(DespesaRequest requisicao, Integer empresa){
        Despesa despesa = new Despesa();
        despesa.setDataGeracao(Data.of());
        despesa.setCodigoExteno(Identificacao.codigoExterno(requisicao.getCodigoExterno()));
        despesa.setValor(Valor.of(requisicao.getValor()));
        despesa.setStatus(PagamentoStatus.GERADO);
        despesa.setEmpresa(empresa);
        despesa.setMensagem(requisicao.getMensagem());
        despesa.setParcela(requisicao.getParcela());
        despesa.setFavorecido(definirParceiro(requisicao.getFavorecido(), empresa));
        despesa.setAplicacao(definirAplicacao(requisicao.getAplicacao()));
        return despesa;
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
    private Parceiro definirParceiro(ParceiroRequest requisicao, Integer empresa){
        return cadastroService.atualizarCadastro(empresa, Parceiro.of(requisicao));
    }

    public List<Despesa> listar(Integer cobranca){
        return repository.listar(requisicaoInfo.getEmpresa(), cobranca);
    }

    public List<Despesa> listar(Filtros filtros){
        String dataInicio = filtros.getStringData(DATA_INICIO);
        String dataFim = filtros.getStringData(DATA_FIM);
        Integer empresa = requisicaoInfo.getEmpresa();
        Integer favorecido = filtros.getInt(FAVORECIDO);
        PagamentoStatus status = filtros.getEnum(STATUS, PagamentoStatus.class);
        return repository.listar(empresa,favorecido,status, dataInicio, dataFim);
    }

}
