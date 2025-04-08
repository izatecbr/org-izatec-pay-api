package com.izatec.pay.core.previsao;

import com.izatec.pay.core.cadastro.ParceiroRequest;
import com.izatec.pay.core.comum.NegociacaoRequest;
import com.izatec.pay.core.previsao.aplicacao.AplicacaoRequest;
import lombok.Data;

@Data
public class PrevisaoRequest {
    private String codigoExteno;
    private Double valor;
    private String titulo;
    private String descricao;
    private ParceiroRequest favorecido;
    private NegociacaoRequest negociacao;
    private AplicacaoRequest aplicacao;
}
