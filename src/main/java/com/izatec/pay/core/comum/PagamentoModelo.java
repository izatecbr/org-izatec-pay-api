package com.izatec.pay.core.comum;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PagamentoModelo {
    UNICO("Único"),//GERAÇÃO DE UM UNICO PAGAMENTO
    RECORRENTE("Recorrente"),//SEM DATA DE ENCERRAMENTO - A PESSOA PRECISA ENCERRAR O CONTRATO
    PROGRAMADO("Programado"), //ENCERRAMENTO COM BASE NO TOTAL DE PARCELAS,
    PARCELADO("Parcelado");//PAGA O VALOR TOTAL E DIVIDE AS PARCELAS

    private String nome;
    private PagamentoModelo(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
    public String getId() {
        return name();
    }
    }
