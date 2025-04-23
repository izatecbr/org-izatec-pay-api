package com.izatec.pay.infra.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;

import static com.izatec.pay.infra.util.Idiomas.PT_BR;
import static java.time.Month.*;

public enum Meses {
    JAN(JANUARY),
    FEV(FEBRUARY),
    MAR(MARCH),
    ABR(APRIL),
    MAI(MAY),
    JUN(JUNE),
    JUL(JULY),
    AGO(AUGUST),
    SET(SEPTEMBER),
    OUT(OCTOBER),
    NOV(NOVEMBER),
    DEZ(DECEMBER),
    ;

    private Month month;
    private Meses(Month month){
       this.month = month;
    }
    public Integer getNumero() {
        return month.getValue();
    }

    public String getLegenda() {
        return month.getDisplayName(TextStyle.FULL,PT_BR);
    }
    public String getSigla() {
        return name();
    }
    public Mes getMes(Integer ano){
        LocalDate primeiroDia = YearMonth.of(ano,getNumero()).atDay(1);
        LocalDate ultimoDia = YearMonth.of(ano,getNumero()).atEndOfMonth();
        return new Mes(getSigla(),ano, getLegenda(), primeiroDia, ultimoDia,Integer.valueOf(String.format("%tY%<tm", primeiroDia)));
    }
    public static Meses getByNumero(int numero) {
        for (Meses item : Meses.values()) {
            if (item.getNumero() == numero) {
                return item;
            }
        }

        return null;
    }
}
