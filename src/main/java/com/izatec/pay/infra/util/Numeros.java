package com.izatec.pay.infra.util;

import java.time.LocalDateTime;

public class Numeros {
    public static final long NL0 = 0L;
    public static final int N0 = 0;
    public static final int N1 = 1;
    public static final int N2 = 2;
    public static final int N5 = 5;
    public static final int N6 = 6;
    public static final int N8 = 8;
    public static final int N10 = 10;
    public static final int N11 = 11;
    public static final int N12 = 12;
    public static final int N15 = 15;
    public static final int N16 = 16;
    public static final int N20 = 20;
    public static final int N32 = 32;
    public static final int N40 = 40;
    public static final int N50 = 50;
    public static final int N60 = 60;
    public static final int N80 = 80;
    public static final int N100 = 100;
    public static final int N150 = 150;

    public static String aleatorio(){
        return aleatorio(4);
    }
    public static String aleatorio(int maximo){
        return Textos.maximo(LocalDateTime.now().getNano(), maximo);
    }

}
