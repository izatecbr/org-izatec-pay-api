package com.izatec.pay.core.previsao.despesa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.izatec.pay.core.cadastro.ParceiroRequest;
import com.izatec.pay.core.comum.VencimentoRequest;
import com.izatec.pay.core.previsao.aplicacao.AplicacaoRequest;
import lombok.Data;

@Data
public class DespesaRequest {
    private String codigoExterno;
    private Double valor;
    private String mensagem;
    private VencimentoRequest vencimento;
    private ParceiroRequest favorecido;
    private Integer parcela;
    private AplicacaoRequest aplicacao;
    @JsonIgnore
    private Integer previsao;
}
