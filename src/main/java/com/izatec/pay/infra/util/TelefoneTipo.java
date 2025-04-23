package com.izatec.pay.infra.util;

public enum TelefoneTipo {
    AMBOS("\\d{10,11}"),
    TELEFONE("\\d{10}"),
    CELULAR("\\d{11}");

    private String padrao;

    private TelefoneTipo(String padrao){
        this.padrao = padrao;
    }

    public String getPadrao() {
        return padrao;
    }
}
