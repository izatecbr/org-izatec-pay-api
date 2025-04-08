package com.izatec.pay.infra.business;

import static com.izatec.pay.infra.business.BusinessMessage.E111;

public class LoginInativoException extends BusinessException {
    public LoginInativoException(String status) {
        super(E111,status);
    }
}