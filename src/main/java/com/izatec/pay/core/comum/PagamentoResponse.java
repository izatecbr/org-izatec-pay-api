package com.izatec.pay.core.comum;

import lombok.Data;

@Data
public class PagamentoResponse {
    private Integer id;
    private String numeroProtocolo;
    private String status;
    private String mensagem;
}
