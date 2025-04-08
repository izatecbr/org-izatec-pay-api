package com.izatec.pay.infra.business;

import static com.izatec.pay.infra.business.BusinessMessage.E204;

public class ConsultaSemRegistrosException extends BusinessException {
    public ConsultaSemRegistrosException() {
        super(E204);
    }
}
