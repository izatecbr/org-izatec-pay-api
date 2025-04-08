package com.izatec.pay.infra.business;

import static com.izatec.pay.infra.business.BusinessMessage.E199;

public class PersistenciaException extends BusinessException{
    public PersistenciaException(String sufixo){
        super(E199,sufixo);
    }
    public PersistenciaException(){
        this("");
    }
}
