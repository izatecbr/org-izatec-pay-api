package com.izatec.pay.core.cadastro.request;

import lombok.Data;

@Data
public class EnderecoRequest {
    private String cep;
    private String numero;
    private String logradouro;
}
