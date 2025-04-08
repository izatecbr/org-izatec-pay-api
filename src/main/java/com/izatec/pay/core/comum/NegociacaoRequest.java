package com.izatec.pay.core.comum;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NegociacaoRequest {
    private LocalDate proximoVencimento;
    private Integer proximaParcela;
    private Integer quantidadeParcelas;
    private PagamentoModelo modelo;
    private Recorrencia recorrencia;
}
