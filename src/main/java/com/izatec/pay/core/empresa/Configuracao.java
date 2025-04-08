package com.izatec.pay.core.empresa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.izatec.pay.core.empresa.configuracao.ConfiguracaoCertificado;
import com.izatec.pay.core.empresa.configuracao.ConfiguracaoCredencial;
import com.izatec.pay.core.empresa.configuracao.Intermediadores;
import jakarta.persistence.*;
import lombok.Data;

@Embeddable
@Data
public class Configuracao {
    @Column(name = "config_cnpj")
    private String cnpj;
    @Column(name = "config_nome_fantasia")
    private String nomeFantasia;
    @JsonIgnore
    @Column(name = "config_identificacao")
    private String codigoIdentificacao;
    @JsonIgnore
    @Transient
    private ConfiguracaoCertificado certificado;
    @JsonIgnore
    @Transient
    private ConfiguracaoCredencial credencial;
    @JsonIgnore
    @Transient
    private String chavePix;
    @JsonIgnore
    @Transient
    private Double custoIntegracao;
    @JsonIgnore
    @Transient
    private String email;
    @JsonIgnore
    @Transient
    private Integer empresa;
    @JsonIgnore
    @Transient
    private Intermediadores intermediador;
}
