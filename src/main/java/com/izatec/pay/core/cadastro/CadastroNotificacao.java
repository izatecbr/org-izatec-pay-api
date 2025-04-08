package com.izatec.pay.core.cadastro;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class CadastroNotificacao {
    @Column(name = "notif_email")
    private boolean email=true;
    @Column(name = "notif_whatsapp")
    private boolean whatsapp;
}
