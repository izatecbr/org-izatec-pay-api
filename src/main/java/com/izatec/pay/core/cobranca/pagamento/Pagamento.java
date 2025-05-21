package com.izatec.pay.core.cobranca.pagamento;

import com.izatec.pay.core.cadastro.Parceiro;
import com.izatec.pay.core.comum.*;
import com.izatec.pay.core.empresa.Configuracao;
import jakarta.persistence.*;

@Entity
@Table(name = "tab_pagamento")
@lombok.Data
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "empresa_id")
    private Integer empresa;
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "scd_id")),
            @AttributeOverride(name = "documento", column = @Column(name = "scd_documento")),
            @AttributeOverride(name = "nomeCompleto", column = @Column(name = "scd_nome_completo"))
    })
    private Parceiro sacado;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dia", column = @Column(name = "geracao_dia")),
            @AttributeOverride(name = "hora", column = @Column(name = "geracao_hora"))
    })
    private Data dataGeracao;
    private Configuracao configuracao = new Configuracao();
    @Column(name = "cod_externo")
    private String codigoExteno;
    @Column(name = "cod_identificacao")
    private String codigoIdentificacao;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dia", column = @Column(name = "prev_pag_dia")),
            @AttributeOverride(name = "hora", column = @Column(name = "prev_pag_hora"))
    })
    private Data dataPrevista;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dia", column = @Column(name = "vencto_dia")),
            @AttributeOverride(name = "hora", column = @Column(name = "vencto_hora"))
    })
    private Data dataVencimento;

    @Embedded
    private PagamentoIntegracao integracao = new PagamentoIntegracao();
    @Embedded
    private Valor valor;
    private String mensagem;
    private String observacao;
    @Enumerated(EnumType.STRING)
    private PagamentoStatus status;
    private Integer parcela=1;
    @Column(name = "cobranca_id")
    private Integer cobranca;
    @Embedded
    private PagamentoCompensacao compensacao;
    @Embedded
    private Notificacao notificacao = new Notificacao();

}
