package com.izatec.pay.infra;

public enum Atributos {
    SACADO("sacado","Sacado"),
    DATA_INICIO("dataInicio","Data Inicio"),
    DATA_FIM("dataFim","Data Fim"),
    STATUS("status","Status"),
    ID("id","Id"),
    LOCALIZA("localiza","Localiza"),
    ;
    private String nome;
    private String legenda;
    private Atributos(String nome, String legenda){
        this.nome  =nome;
        this.legenda = legenda;
    }

    public String getNome() {
        return nome;
    }

    public String getLegenda() {
        return legenda;
    }
}
