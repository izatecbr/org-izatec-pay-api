package com.izatec.pay.core.comum;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Embeddable
@lombok.Data
public class PagamentoCompensacao {
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dia", column = @Column(name = "cpsc_dia")),
            @AttributeOverride(name = "hora", column = @Column(name = "cpsc_hora"))
    })
    private Data data;
    @Column(name = "cpsc_comprovante")
    private String comprovante;
    @Column(name = "cpsc_observacao")
    private String observacao;
    @Column(name = "cpsc_custo")
    private Double custo;
    public static PagamentoCompensacao manual(LocalDate data, LocalTime hora, String observacao){
        return manual(Data.of(data,hora), observacao);
    }
    public static PagamentoCompensacao manual(Data data, String observacao){
        PagamentoCompensacao compensacao=new PagamentoCompensacao();
        compensacao.setData(data);
        compensacao.comprovante = "MANUAL";
        compensacao.observacao = observacao;
        compensacao.setCusto(0.0);
        return compensacao;
    }
}

