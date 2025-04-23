package com.izatec.pay.core.comum;

import lombok.Data;

@Data
public class NotificacaoRequest {
    private boolean email;
    private boolean whatsapp;
}
