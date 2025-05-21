package com.izatec.pay.core.cadastro;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.izatec.pay.core.comum.Notificacao;
import com.izatec.pay.infra.security.Criptografia;
import com.izatec.pay.infra.util.JsonUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Table(name = "tab_cadastro")
@Data
public class Cadastro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "avalista_id")
    private Integer avalista;
    //@Setter(AccessLevel.NONE)
    private String documento;
    @Column(name = "nome_completo")
    private String nomeCompleto;
    private String email;
    private String whatsapp;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonUtil.DATE)
    @Column(name = "dt_nascto")
    private LocalDate dataNascimento;
    @Embedded
    private Endereco endereco = new Endereco();
    @Embedded
    private Notificacao notificacao = new Notificacao();
    @Column(name = "empresa_id")
    private Integer empresa;
    @Column(name = "info_adicionais")
    private String informacoesAdicionais;



}
