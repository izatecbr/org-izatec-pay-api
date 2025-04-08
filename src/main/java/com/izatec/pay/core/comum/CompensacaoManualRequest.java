package com.izatec.pay.core.comum;

import lombok.Data;
@Data
public class CompensacaoManualRequest {
    private DataRequest data;
    private Double valorPago;
    private String observacao;
}
