package com.izatec.pay.core.cadastro;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @Setter(AccessLevel.NONE)
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
    private CadastroNotificacao notificacao = new CadastroNotificacao();
    @Column(name = "empresa_id")
    private Integer empresa;

    public void setIdentificacao(Integer empresa, String documento) {
        String token = "cadastro";
        this.documento = documento;
        if(documento==null || documento.isBlank()){
            try {
                if(whatsapp!=null)
                    this.documento=Criptografia.criptografar(whatsapp.toString(), token ).substring(0, 11);
                else
                    this.documento=Criptografia.criptografar(nomeCompleto, token ).substring(0, 11);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        this.empresa = empresa;
    }

}
