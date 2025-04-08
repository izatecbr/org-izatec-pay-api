package com.izatec.pay.core.cadastro.request;

import lombok.Data;

@Data
public class CadastroNotificacaoRequest {
    private boolean email;
    private boolean whatsapp;
}
