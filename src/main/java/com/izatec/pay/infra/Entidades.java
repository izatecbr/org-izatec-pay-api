package com.izatec.pay.infra;

public enum Entidades {
    EMPRESA("Empresa", "Empresa"),
    CONFIGURACAO("EmpresaConfiguracao", "Configuração"),

    CADASTRO("Cadastro", "Cadastro"),
    PAGAMENTO("Pagamento", "Pagamento"),
    COBRANCA("Cobranca", "Cobrança"),
    DESPESA("Despesa", "Despesa"),

    ;
    private String nome;
    private String legenda;

    private Entidades(String nome, String legenda) {
        this.nome = nome;
        this.legenda = legenda;
    }

    public String getNome() {
        return nome;
    }

    public String getLegenda() {
        return legenda;
    }
}
