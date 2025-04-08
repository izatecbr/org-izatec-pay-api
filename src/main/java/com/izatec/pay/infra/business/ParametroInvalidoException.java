package com.izatec.pay.infra.business;

public class ParametroInvalidoException extends BusinessException{
    public ParametroInvalidoException(String validacao){
        super(BusinessMessage.E135,validacao);
    }

}
