package com.izatec.pay.infra.business;

import static com.izatec.pay.infra.business.BusinessMessage.E116;

public class TamanhoMinimoException extends BusinessException{
    public TamanhoMinimoException(String campo, Integer minimo){
        super(E116,campo,minimo.toString());
    }
}
