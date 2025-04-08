package com.izatec.pay.core.cadastro;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Endereco {
    @Column(name = "end_cep")
    private String cep;
    @Column(name = "end_numero")
    private String numero;
    @Column(name = "end_logradouro")
    private String logradouro;
}
