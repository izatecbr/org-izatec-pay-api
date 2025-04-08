package com.izatec.pay.core.comum;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.izatec.pay.infra.util.JsonUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;
@Embeddable
@Data
public class Negociacao {
    @Column(name = "ngcc_dia_vencto")
    private Integer diaVencimento;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonUtil.DATE)
    @Column(name = "ngcc_dt_prox_vencto")
    private LocalDate proximoVencimento;
    @Column(name = "ngcc_nr_prox_parcela")
    private Integer proximaParcela;
    @Enumerated(EnumType.STRING)
    @Column(name = "ngcc_modelo")
    private PagamentoModelo modelo;
    @Enumerated(EnumType.STRING)
    @Column(name = "ngcc_recorrencia")
    private Recorrencia recorrencia;
}
