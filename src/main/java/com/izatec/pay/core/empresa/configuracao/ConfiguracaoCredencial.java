package com.izatec.pay.core.empresa.configuracao;

import lombok.Getter;

@Getter
public class ConfiguracaoCredencial {
    private String usuario;
    private String senha;
    public static ConfiguracaoCredencial of(String usuario, String senha){
        ConfiguracaoCredencial instancia = new ConfiguracaoCredencial();
        instancia.usuario = usuario;
        instancia.senha = senha;
        return instancia;
    }
}
