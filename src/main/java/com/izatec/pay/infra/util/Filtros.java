package com.izatec.pay.infra.util;

import com.izatec.pay.infra.Atributos;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

//https://www.baeldung.com/spring-request-param
//https://stackoverflow.com/questions/56468760/how-to-collect-all-fields-annotated-with-requestparam-into-one-object
public class Filtros {
    private Map<String, Object> criterios = new LinkedHashMap<>();
    public static Filtros ofLocaliza(Object valor){
        return of(Atributos.LOCALIZA,valor);
    }
    public static Filtros of(Atributos atributo, Object valor){
        return Objects.isNull(valor) ? new Filtros() : of(Map.of(atributo.getNome(),valor));
    }
    public static Filtros of(Map<String, Object> criterios) {
        Filtros filtroRequisicao = new Filtros();
        filtroRequisicao.criterios = criterios;
        return filtroRequisicao;
    }

    public void adicionar(Atributos atributo, Object valor){
        if(criterios==null)
            criterios = new HashMap<>();

        criterios.put(atributo.getNome(),valor.toString());
    }

    public Map<String, Object> getCriterios() {
        return criterios;
    }
    public <T extends Enum<T>> T getEnum(Atributos atributo, Class <T> classeEnum){
        return  Objects.isNull(get(atributo)) ? null : Definicoes.getEnum(classeEnum, get(atributo));
    }

    public String get(Atributos atributo) {
        Object valor = criterios.get(atributo.getNome());
        return Objects.toString(valor, null);
    }

    public Integer getInt(Atributos atributo) {
        return Objects.isNull(get(atributo)) ? null : Integer.valueOf(get(atributo));
    }

    public String getLocaliza() {
        Atributos atributo = Atributos.LOCALIZA;
        return Objects.isNull(get(atributo)) ? null : Textos.localiza(get(atributo));
    }

    public LocalDate getData(Atributos atributo) {
        return Objects.isNull(get(atributo)) ? null : LocalDate.parse(get(atributo));
    }

    public String getStringData(Atributos atributo) {
        return Objects.isNull(get(atributo)) ? null : LocalDate.parse(get(atributo)).toString();
    }

    public String toString(){
        return this.criterios.toString();
    }

}
