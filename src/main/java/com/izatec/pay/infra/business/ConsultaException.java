package com.izatec.pay.infra.business;

import static com.izatec.pay.infra.business.BusinessMessage.E199;

public class ConsultaException extends BusinessException{
    public ConsultaException(String sufixo){
        super(E199,sufixo);
    }
}
