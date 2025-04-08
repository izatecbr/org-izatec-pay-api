package com.izatec.pay.infra.business;

import com.izatec.pay.infra.Atributos;
import com.izatec.pay.infra.Entidades;

public class RegistroNaoLocalizadoException extends BusinessException {
    public RegistroNaoLocalizadoException(Entidades registro, Atributos campo) {
        super(BusinessMessage.E404,registro.getLegenda(),campo.getLegenda());
    }
    public RegistroNaoLocalizadoException(Entidades registro) {
        super(BusinessMessage.E404,registro.getLegenda(),"Identificação");
    }
    public RegistroNaoLocalizadoException() {
        super(BusinessMessage.E404,"Registro", "Código");
    }

}
