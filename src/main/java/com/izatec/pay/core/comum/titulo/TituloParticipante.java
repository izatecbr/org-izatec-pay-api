package com.izatec.pay.core.comum.titulo;

import lombok.Data;

@Data
public class TituloParticipante {
    private String nome;
    private String cpfCnpj;
    private String telefone;
    private String email;
    private String endereco;
    public static TituloParticipante of(String nome, String cpfCnpj) {
        return of(nome, cpfCnpj, "");
    }
    public static TituloParticipante of(String nome, String cpfCnpj, String telefone) {
        return of(nome, cpfCnpj, telefone,"");
    }
    public static TituloParticipante of(String nome, String cpfCnpj, String telefone, String endereco) {
        TituloParticipante participante = new TituloParticipante();
        participante.nome = nome;
        participante.cpfCnpj = cpfCnpj;
        participante.telefone = telefone;
        participante.email = "";
        participante.endereco = endereco;
        return participante;
    }
}
