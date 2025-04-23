package com.izatec.pay.infra.util;

import java.util.Objects;

public class Definicoes {
    public static  <T> T casoNulo(Object valor, Object valorCasoNulo){
        return (T) (Objects.isNull(valor) ? valorCasoNulo : valor);
    }
    public static  <T> T casoNuloVazio(Object valor, Object valorCasoNuloVazio){
        return (T) (Objects.isNull(valor) || valor.toString().isBlank() ? valorCasoNuloVazio : valor);
    }
    public static  String digitos(Object texto){
        return texto.toString().replaceAll("\\D","");
    }
    public static  <T extends Enum<T>> T getEnum(Class<T> classeEnum, String valor){
        return valor==null ? null : Enum.valueOf(classeEnum, valor);
    }
}
