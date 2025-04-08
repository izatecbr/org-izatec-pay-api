package com.izatec.pay.core.comum;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Valor {
    @Column(name = "vl_original")
    private Double original;
    @Column(name = "vl_pago")
    private Double pago;
    public static Valor of(Double original){
        Valor valor = new Valor();
        valor.original = original;
        valor.pago=0.0;
        return valor;
    }
}
