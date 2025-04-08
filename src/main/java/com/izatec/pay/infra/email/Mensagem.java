package com.izatec.pay.infra.email;

import lombok.Data;

@Data
public class Mensagem {
    private String titulo;
    private String corpo;
    private String remetente;
    private String destinatario;
}