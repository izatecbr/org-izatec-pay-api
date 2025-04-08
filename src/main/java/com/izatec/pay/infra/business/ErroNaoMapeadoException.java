package com.izatec.pay.infra.business;

import static com.izatec.pay.infra.business.BusinessMessage.E500;

public class ErroNaoMapeadoException extends BusinessException{
    public ErroNaoMapeadoException() {
        super(E500);
    }
}
