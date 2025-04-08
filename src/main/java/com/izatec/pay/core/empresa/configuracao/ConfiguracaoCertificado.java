package com.izatec.pay.core.empresa.configuracao;

import lombok.Getter;

@Getter
public class ConfiguracaoCertificado {
    private String nome;
    private String senha;
    public static ConfiguracaoCertificado of(String nome, String senha){
        ConfiguracaoCertificado instancia = new ConfiguracaoCertificado();
        instancia.nome = nome;
        instancia.senha = senha;
        return instancia;
    }
}
