package com.izatec.pay.core.empresa.configuracao;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Intemediador {
    @Column(name = "intermediador_sigla")
    private String sigla;
    @Column(name = "intermediador_id")
    private String id;
    @Column(name = "intermediador_senha")
    private String senha;
    @Column(name = "intermediador_chave_pix")
    private String chavePix;
}
