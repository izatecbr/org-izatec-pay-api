package com.izatec.pay.core.notificacao;

import com.izatec.pay.core.comum.Data;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Setter;

@Entity
@Table(name = "tab_notificacao")
@lombok.Data
public class Notificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;
    private String origem;
    private String remetente;
    private String destinatario;
    private String descricao;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dia", column = @Column(name = "data_dia")),
            @AttributeOverride(name = "hora", column = @Column(name = "data_hora"))
    })
    private Data data = new Data();
    @Embedded
    private NotificacaoEnvio envio = new NotificacaoEnvio();
    @Column(name = "vl_custo")
    private Double valorCusto;

}