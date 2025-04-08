package com.izatec.pay.core.cobranca;

import com.izatec.pay.core.cadastro.Parceiro;
import com.izatec.pay.core.comum.Status;
import com.izatec.pay.core.comum.Data;
import com.izatec.pay.core.comum.Negociacao;
import com.izatec.pay.core.empresa.Configuracao;
import jakarta.persistence.*;
//job contratos com pagamento hj de 5 e 5 minutos
//job contratos com pagamento 03 dias antes, diariamente as 08:00

@Entity
@Table(name = "tab_cobranca")
@lombok.Data
public class Cobranca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "empresa_id")
    private Integer empresa;
    @Embedded
    private Configuracao configuracao=new Configuracao();
    private String titulo;
    private String descricao;
    private String observacao;
    private String endereco;
    @Embedded
    private Negociacao negociacao;
    @Column(name = "qtd_parcelas")
    private Integer quantidadeParcelas;
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "scd_id")),
            @AttributeOverride(name = "documento", column = @Column(name = "scd_documento")),
            @AttributeOverride(name = "nomeCompleto", column = @Column(name = "scd_nome_completo"))
    })
    private Parceiro sacado;
    @Enumerated(EnumType.STRING)
    private Status status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dia", column = @Column(name = "geracao_dia")),
            @AttributeOverride(name = "hora", column = @Column(name = "geracao_hora"))
    })
    private Data dataGeracao;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dia", column = @Column(name = "vigencia_dia")),
            @AttributeOverride(name = "hora", column = @Column(name = "vigencia_hora"))
    })
    private Data dataVigencia;
    @Column(name = "vl_cobranca")
    private Double valorCobranca;
    @Column(name = "vl_cobrado")
    private Double valorCobrado;
    @Column(name = "cod_externo")
    private String codigoExterno;
}
