package com.izatec.pay.infra.business;

public class RequisicaoException extends BusinessException{
    public RequisicaoException(String descricao){
        super(BusinessMessage.E127,descricao);
    }

}

