package com.izatec.pay.infra.business;

import com.izatec.pay.infra.Atributos;

import static com.izatec.pay.infra.business.BusinessMessage.E102;

public class RegistroDuplicadoException extends BusinessException {
    public RegistroDuplicadoException(Atributos attributes, String valor) {
        super(E102,attributes.getLegenda(),valor);
    }
}
