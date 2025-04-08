package com.izatec.pay.core.cadastro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Embeddable
public class Parceiro {
    private Integer id;
    private String documento;
    private String nomeCompleto;
    @Transient
    @JsonIgnore
    private String email;
    @Transient
    @JsonIgnore
    private String whatsapp;
    public static Parceiro of(ParceiroRequest requisicao) {
        Parceiro parceiro = new Parceiro();
        if(requisicao!=null){
            parceiro.nomeCompleto = requisicao.getNomeCompleto();
            parceiro.email = requisicao.getEmail();
            parceiro.documento = requisicao.getDocumento();
            parceiro.whatsapp = requisicao.getWhatsapp();
        }
        return parceiro;
    }
    public boolean valirdarCpfCnpj() {
        return true;
    }
}
