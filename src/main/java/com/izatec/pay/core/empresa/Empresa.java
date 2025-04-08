package com.izatec.pay.core.empresa;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tab_empresa")
@Data
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "cpf_cnpj")
    private String cpfCnpj;
    @Column(name = "nome_fantasia")
    private String nomeFantasia;
    @Column(name = "razao_social")
    private String razaoSocial;
    private String email;
    private Long whatsapp;
    private String senha;
}
