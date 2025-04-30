package com.izatec.pay.core.cobranca.CobrancaAtivacao;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tab_cobranca_ativacao")
@lombok.Data
public class CobrancaAtivacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "id_cobranca")
    private Integer idCobranca;
    private String ip;
    @Column(name = "data_hora")
    private LocalDateTime dataHora;
}
