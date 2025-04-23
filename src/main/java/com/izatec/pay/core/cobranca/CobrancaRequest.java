package com.izatec.pay.core.cobranca;

import com.izatec.pay.core.cadastro.ParceiroRequest;
import com.izatec.pay.core.comum.NegociacaoRequest;
import com.izatec.pay.core.comum.NotificacaoRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CobrancaRequest {
    private String codigoIdentificacao;
    private String codigoExterno;
    private Double valor;
    private String titulo;
    private String descricao;
    private String endereco;
    private ParceiroRequest sacado;
    private NegociacaoRequest negociacao;
    private LocalDate dataVigencia;
    private NotificacaoRequest notificacao = new NotificacaoRequest();
}
