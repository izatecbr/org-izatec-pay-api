package com.izatec.pay.infra.security;

import lombok.Data;

@Data
public class RequisicaoInfo {
    private String cpfCnpj;
    private Integer empresa;
}
