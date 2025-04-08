package com.izatec.pay.core.previsao.despesa;


import com.izatec.pay.core.cadastro.Parceiro;
import com.izatec.pay.core.comum.Data;
import com.izatec.pay.core.comum.PagamentoCompensacao;
import com.izatec.pay.core.comum.PagamentoStatus;
import com.izatec.pay.core.comum.Valor;
import com.izatec.pay.core.previsao.aplicacao.Aplicacao;
import jakarta.persistence.*;

@Entity
@Table(name = "tab_despesa")
@lombok.Data
public class Despesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "empresa_id")
    private Integer empresa;
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "fav_id")),
            @AttributeOverride(name = "documento", column = @Column(name = "fav_documento")),
            @AttributeOverride(name = "nomeCompleto", column = @Column(name = "fav_nome_completo"))
    })
    private Parceiro favorecido;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dia", column = @Column(name = "geracao_dia")),
            @AttributeOverride(name = "hora", column = @Column(name = "geracao_hora"))
    })
    private Data dataGeracao;
    @Column(name = "cod_externo")
    private String codigoExteno;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dia", column = @Column(name = "vencto_dia")),
            @AttributeOverride(name = "hora", column = @Column(name = "vencto_hora"))
    })
    private Data dataVencimento;
    @Embedded
    private Valor valor;
    private String mensagem;
    @Enumerated(EnumType.STRING)
    private PagamentoStatus status;
    private Integer parcela=1;
    @Column(name = "previsao_id")
    private Integer previsao;
    private PagamentoCompensacao compensacao;
    @Embedded
    private Aplicacao aplicacao;
}

