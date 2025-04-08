package com.izatec.pay.infra.business;

import static com.izatec.pay.infra.business.BusinessMessage.E181;

public class BrindeIndisponivelException extends BusinessException{
    public BrindeIndisponivelException() {
        super(E181);
    }
}
