package com.izatec.pay.core.cobranca.ativacao;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tab_cobranca_ativacao")
//@lombok.Data
public class CobrancaAtivacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "cobranca_id")
    private Integer cobranca;
    private String ip;
    @Column(name = "data_hora")
    private LocalDateTime dataHora;
    @Column(name = "qtd_ativacoes")
    private int quantidadeAtivacoes;

    public static CobrancaAtivacao of(Integer cobranca, String ip) {
        CobrancaAtivacao cobrancaAtivacao = new CobrancaAtivacao();
        cobrancaAtivacao.cobranca=cobranca;
        cobrancaAtivacao.ip=ip;
        cobrancaAtivacao.dataHora=LocalDateTime.now();
        cobrancaAtivacao.quantidadeAtivacoes=0;
        return cobrancaAtivacao;
    }

    public void incrementarAtivacao() {
        dataHora = LocalDateTime.now();
        this.quantidadeAtivacoes++;
    }

    public int getQuantidadeAtivacoes() {
        return quantidadeAtivacoes;
    }
}