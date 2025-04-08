package com.izatec.pay.core.cadastro;

import lombok.Data;

@Data
public class ParceiroRequest {
    private Integer id;
    private String documento;
    private String nomeCompleto;
    private String email;
    private String whatsapp;
}
