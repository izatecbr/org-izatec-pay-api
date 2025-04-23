package com.izatec.pay.core.comum;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Notificacao {
    @Column(name = "notif_email")
    private boolean email;
    @Column(name = "notif_whatsapp")
    private boolean whatsapp;
}
