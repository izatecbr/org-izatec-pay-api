package com.izatec.pay.core.previsao.despesa;

import com.izatec.pay.core.comum.Status;
import com.izatec.pay.core.comum.*;
import com.izatec.pay.core.previsao.Previsao;
import com.izatec.pay.core.previsao.PrevisaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class DespesaCompensacaoService {
    @Autowired
    private DespesaRepository repository;
    @Autowired
    private PrevisaoRepository despesaRepository;
    @Transactional
    public void confirmarCompensacao(Integer id, CompensacaoManualRequest requisicao){
        Despesa despesa = repository.findById(id).orElse(null);
        despesa.setStatus(PagamentoStatus.COMPENSADO);
        Data data =  requisicao.getData() ==null ? Data.of() : Data.of(requisicao.getData().getDia(), requisicao.getData().getHora());
        despesa.setCompensacao(PagamentoCompensacao.manual(data, requisicao.getObservacao()));
        despesa.getValor().setPago(requisicao.getValorPago());
        if(despesa.getPrevisao()!=null){
            Previsao previsao = despesaRepository.findById(despesa.getPrevisao()).orElse(null);
            previsao.setValorPago(previsao.getValorPago() + despesa.getValor().getPago());
            despesaRepository.save(previsao);
        }
        repository.save(despesa);
    }
    private void atualizarStatus( Previsao despesa){
        Negociacao negociacao = despesa.getNegociacao();
        if(PagamentoModelo.UNICO==negociacao.getModelo())
            despesa.setStatus(Status.QUITADA);
        else if(PagamentoModelo.PROGRAMADO == negociacao.getModelo() && despesa.getQuantidadeParcelas().equals(negociacao.getProximaParcela()))
            despesa.setStatus(Status.QUITADA);
        else if(PagamentoModelo.PARCELADO == negociacao.getModelo() && despesa.getValorPago().compareTo(despesa.getValorDespesa()) >= 0)
            despesa.setStatus(Status.QUITADA);
    }
}
