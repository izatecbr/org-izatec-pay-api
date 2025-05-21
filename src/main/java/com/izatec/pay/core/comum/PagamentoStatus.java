package com.izatec.pay.core.comum;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PagamentoStatus {
    GERADO("Gerado"),
    //INTEGRADO("Integrado"),
    COMPENSADO("Pago"),
    EXPIRADO("Expirado"),
    CANCELADO("Cancelado"),
    AGUARDANDO("Aguardando");
    private String nome;
    private PagamentoStatus(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
    public String getId() {
        return name();
    }
}
