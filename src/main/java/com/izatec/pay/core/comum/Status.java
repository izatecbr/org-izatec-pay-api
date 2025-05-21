package com.izatec.pay.core.comum;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Status {
    ATIVA("Ativa"), //GERANDO PARCELAS
    FINALIZADA("Finalizada"),//QUANDO TODAS AS PARCELAS FORAM GERADAS
    QUITADA("Quitada"), //QUANDO A COBRANÇA MODELO PARCELADO NÃO TEM MAIS PARCELAS
    //ENCERRADA("Encerrada"), //QUANDO A VIGENCIA DA COBRANÇA ACABOU
    CANCELADA("Cancelada"); //QUANDO HOUVE A SOLICITAÇÃO DE CANCELAMENTO PELO CLIENTE

    private String nome;
    private Status(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
    public String getId() {
        return name();
    }
}
