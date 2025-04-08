package com.izatec.pay.infra.business;

import static com.izatec.pay.infra.business.BusinessMessage.E141;

public class IpNaoLocalizadoException extends BusinessException{
    public IpNaoLocalizadoException() {
        super(E141);
    }
}
