package com.izatec.pay.core.comum;

import java.time.LocalDate;

public enum Recorrencia {
    MENSAL("Mensal",30),
    SEMANAL("Semanal",7),
    DIARIA("Diária",1),
    QUINDIAL("Quindial",5),
    SEMESTRAL("Semestral",180),
    ANUAL("Anual",365),

    UNICA("Única",1);
    private String descricao;
    private int dias;
    private Recorrencia(String descricao,int dias){
        this.descricao=descricao;
        this.dias = dias;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDate gerarProximaData( LocalDate ultimoVencimento, int diaVencimento){
        if(this !=MENSAL)
            return ultimoVencimento.plusDays(this.dias);
        else{
            ultimoVencimento= ultimoVencimento.plusMonths(1);
            return diaVencimento>ultimoVencimento.lengthOfMonth()? ultimoVencimento.withDayOfMonth(ultimoVencimento.lengthOfMonth()) : ultimoVencimento.withDayOfMonth(diaVencimento);
        }

    }

}
