package com.izatec.pay.core.cadastro.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CadastroSimplesRequest {
    private String cpfCnpj;
    private String nomeCompleto;
    private String email;
    private Long whatsapp;
    private LocalDate dataNascimento;
    private String endereco;
}
