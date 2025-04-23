package com.izatec.pay.infra.business;

public class RequisicaoInvalidaException extends BusinessException{
    public RequisicaoInvalidaException(String descricao){
        super(BusinessMessage.E128,descricao);
    }

}

