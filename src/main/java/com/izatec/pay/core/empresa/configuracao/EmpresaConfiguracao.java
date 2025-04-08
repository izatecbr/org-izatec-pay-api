package com.izatec.pay.core.empresa.configuracao;

import com.izatec.pay.core.empresa.Certificado;
import com.izatec.pay.core.empresa.Empresa;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tab_configuracao")
@Data
public class EmpresaConfiguracao {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Empresa empresa;
    @Column(name = "custo_integracao")
    private Double custoIntegracao;
    @Embedded
    private Intemediador intermediador;
    @Embedded
    private Certificado certificado;
}
