package com.izatec.pay.core.empresa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Certificado {
    @Column(name = "certificado_nome")
    private String nome;
    @Column(name = "certificado_senha")
    private String senha;
}
