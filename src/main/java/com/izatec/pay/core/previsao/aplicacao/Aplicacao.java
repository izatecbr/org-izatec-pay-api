package com.izatec.pay.core.previsao.aplicacao;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Aplicacao {
    @Column(name = "aplc_grupo")
    private String grupo;
    @Column(name = "aplc_categoria")
    private String categoria;
}
