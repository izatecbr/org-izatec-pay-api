package com.izatec.pay.core.previsao;

import com.izatec.pay.core.cadastro.Parceiro;
import com.izatec.pay.core.comum.Data;
import com.izatec.pay.core.comum.Negociacao;
import com.izatec.pay.core.comum.Status;
import com.izatec.pay.core.previsao.aplicacao.Aplicacao;
import jakarta.persistence.*;

@Entity
@Table(name = "tab_previsao")
@lombok.Data
public class Previsao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dia", column = @Column(name = "geracao_dia")),
            @AttributeOverride(name = "hora", column = @Column(name = "geracao_hora"))
    })
    private Data dataGeracao;
    @Column(name = "empresa_id")
    private Integer empresa;
    private String titulo;
    private String descricao;
    private String observacao;
    @Embedded
    private Negociacao negociacao;
    @Column(name = "qtd_parcelas")
    private Integer quantidadeParcelas; @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "fav_id")),
            @AttributeOverride(name = "documento", column = @Column(name = "fav_documento")),
            @AttributeOverride(name = "nomeCompleto", column = @Column(name = "fav_nome_completo"))
    })
    private Parceiro favorecido;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "vl_despesa")
    private Double valorDespesa;
    @Column(name = "vl_pago")
    private Double valorPago;
    @Column(name = "cod_externo")
    private String codigoExteno;
    @Embedded
    private Aplicacao aplicacao;
}
