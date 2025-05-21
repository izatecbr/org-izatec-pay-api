package com.izatec.pay.core.comum.titulo;

import lombok.Getter;

@Getter
public class TituloValor {
    private String original;
    private String descontos;
    private String acrescimos;
    private String total;
    public static TituloValor of(String original) {
        TituloValor tituloValor = new TituloValor();
        tituloValor.original = original;
        tituloValor.descontos = "0,00";
        tituloValor.acrescimos = "0,00";
        tituloValor.total = original;
        return tituloValor;
    }
}
