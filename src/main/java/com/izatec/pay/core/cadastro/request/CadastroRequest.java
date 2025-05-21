package com.izatec.pay.core.cadastro.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CadastroRequest {
    private Integer avalista;
    private String documento;
    private String nomeCompleto;
    private String email;
    private String whatsapp;
    private LocalDate dataNascimento;
    private EnderecoRequest endereco;
    private CadastroNotificacaoRequest notificacao;
    private String informacoesAdicionais;
}
