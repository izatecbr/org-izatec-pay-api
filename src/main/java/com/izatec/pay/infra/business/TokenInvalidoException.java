package com.izatec.pay.infra.business;

import static com.izatec.pay.infra.business.BusinessMessage.E900;

public class TokenInvalidoException extends BusinessException {
    public TokenInvalidoException() {
        super(E900);
    }
}
