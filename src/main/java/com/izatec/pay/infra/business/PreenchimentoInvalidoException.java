package com.izatec.pay.infra.business;

public class PreenchimentoInvalidoException extends BusinessException{
    public PreenchimentoInvalidoException(String campo, String validacao){
        super(BusinessMessage.E134,campo,validacao);
    }

}
