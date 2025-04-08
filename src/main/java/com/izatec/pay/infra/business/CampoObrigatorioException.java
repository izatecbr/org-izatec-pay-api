package com.izatec.pay.infra.business;

import com.izatec.pay.infra.Atributos;

public class CampoObrigatorioException extends BusinessException{
    public CampoObrigatorioException(Atributos attribute) {
        super(BusinessMessage.E101,attribute.getLegenda());
    }
    public CampoObrigatorioException(String label) {
        super(BusinessMessage.E101,label);
    }

}
