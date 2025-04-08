package com.izatec.pay.infra.business;

import static com.izatec.pay.infra.business.BusinessMessage.E110;

public class LoginInvalidoException extends BusinessException {
    public LoginInvalidoException() {
        super(E110);
    }
}