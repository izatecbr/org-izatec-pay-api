package com.izatec.pay.infra.util;

public class Logicos {
    public static final String TODOS = "T";
    public static final String SIM = "S";
    public static final String NAO = "N";
    public Boolean of(String opcao){
        return TODOS.equalsIgnoreCase(opcao)?null:SIM.equalsIgnoreCase(opcao);
    }
}
