package com.izatec.pay.core.notificacao;

import jakarta.persistence.*;

@Embeddable
@lombok.Data
public class NotificacaoEnvio {
    @Column(name = "env_entregue")
    private boolean entregue;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dia", column = @Column(name = "env_dia")),
            @AttributeOverride(name = "hora", column = @Column(name = "env_hora"))
    })
    private com.izatec.pay.core.comum.Data data = new com.izatec.pay.core.comum.Data();
    @Column(name = "env_nr_protocolo")
    private String numeroProtocolo;
    @Column(name = "env_resposta")
    private String resposta;
}