package com.izatec.pay.infra.business;

public class TamanhoMinimoMaximoException extends BusinessException {
    public TamanhoMinimoMaximoException(String campo,Integer minimo, Integer maximo){
        super(BusinessMessage.E118,campo,minimo.toString(), maximo.toString());
    }
}
