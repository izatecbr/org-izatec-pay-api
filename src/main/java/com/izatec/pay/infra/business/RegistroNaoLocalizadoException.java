package com.izatec.pay.infra.business;

import com.izatec.pay.infra.Atributos;
import com.izatec.pay.infra.Entidades;

public class RegistroNaoLocalizadoException extends BusinessException {
    public RegistroNaoLocalizadoException(Entidades registro, Atributos campo, Object valor) {
        super(BusinessMessage.E404,registro.getLegenda(),campo.getLegenda(), valor.toString());
    }
    public RegistroNaoLocalizadoException(Entidades registro, Object valor) {
        super(BusinessMessage.E404,registro.getLegenda(),Atributos.ID, valor);
    }
    public RegistroNaoLocalizadoException(Entidades registro) {
        super(BusinessMessage.E404,registro.getLegenda(),"Identificação");
    }
    public RegistroNaoLocalizadoException() {
        super(BusinessMessage.E404,"Registro", "Código");
    }

}
