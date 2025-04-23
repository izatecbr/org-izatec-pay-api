package com.izatec.pay.infra.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculos {
    public static final double ZERO = 0.0;
    public static final int ESCALA4 = 4;
    public static final int ESCALA2 = 2;
    public static final RoundingMode PADRAO= RoundingMode.HALF_UP;
    enum Operacao{
        SOMAR,
        SUBTRAIR,
        MULTIPLICAR,
        DIVIDIR
    }
    private static final int escalaPadrao = 2;
    public static Double somar(Double valorUm, Double valorDois) {
        return somar(escalaPadrao, valorUm, valorDois);
    }
    public static Double somar(int escala, Double valorUm, Double valorDois) {
        return calcular(Operacao.SOMAR, escala, valorUm, valorDois);
    }
    public static Double subtrair(Double valorUm, Double valorDois) {
        return subtrair(escalaPadrao, valorUm, valorDois);
    }
    public static Double subtrair(int escala, Double valorUm, Double valorDois) {
        return calcular(Operacao.SUBTRAIR, escala, valorUm, valorDois);
    }
    public static Double multiplicar(Double valorUm, Double valorDois) {
        return multiplicar(escalaPadrao, valorUm, valorDois);
    }
    public static Double multiplicar(int escala, Double valorUm, Double valorDois) {
        return calcular(Operacao.MULTIPLICAR, escala, valorUm, valorDois);
    }
    public static Double porcentagem(Double valor, Double aliquota) {
        return porcentagem(new BigDecimal(valor), new BigDecimal(aliquota)).doubleValue();
    }
    public static BigDecimal porcentagem(BigDecimal valor, BigDecimal aliquota) {
        return  valor.multiply(aliquota).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
    }
    public static Double dividir(Double valorUm, Double valorDois) {
        return dividir(escalaPadrao, valorUm, valorDois);
    }
    public static Double dividir(int escala, Double valorUm, Double valorDois) {
        return calcular(Operacao.DIVIDIR, escala, valorUm, valorDois);
    }
    public static Double calcular(Operacao operacao, int escala, Double valorUm, Double valorDois){
        BigDecimal resultado = BigDecimal.ZERO;
        BigDecimal numeroUm = new BigDecimal(valorUm.toString());
        BigDecimal numeroDois = new BigDecimal(valorDois.toString());
        if(Operacao.SOMAR==operacao)
            resultado = numeroUm.add(numeroDois);
        else if(Operacao.SUBTRAIR==operacao)
            resultado = numeroUm.subtract(numeroDois);
        else if(Operacao.MULTIPLICAR==operacao)
            resultado = numeroUm.multiply(numeroDois);
        else
            resultado = numeroUm.divide(numeroDois, escala,RoundingMode.HALF_EVEN);

        return resultado.setScale(escala, RoundingMode.HALF_EVEN).doubleValue();
    }
    public static boolean compararIgualdade(Double valorUm, Double valorDois){
        return comparar(valorUm,valorDois)==0;
    }
    public static boolean compararMenorQue(Double valorUm, Double valorDois){
        return comparar(valorUm,valorDois)<0;
    }
    public static boolean compararMaiorQue(Double valorUm, Double valorDois){
        return comparar(valorUm,valorDois)>0;
    }
    public static boolean compararMaiorQueZero(Double valor){
        return compararMaiorQue(valor,ZERO);
    }
    public static boolean compararIgualZero(Double valor){
        return compararIgualdade(valor,ZERO);
    }
    public static boolean compararMenorQueZero(Double valor){
        return compararMenorQue(valor,ZERO);
    }
    public static boolean compararIgualMenorZero(Double valor){
        return compararIgualZero(valor) || compararMenorQueZero(valor);
    }
    private static int comparar(Double valorUm, Double valorDois){
        return Double.compare(valorUm, valorDois);
    }
    public static Double aplicarEscala(Double valor){
        return aplicarEscala(escalaPadrao, valor);
    }
    public static Double aplicarEscala4(Double valor){
        return aplicarEscala(ESCALA4, valor);
    }
    public static Double aplicarEscala(int escalaPadrao, Double valor){
        return new BigDecimal(valor.toString()).setScale(escalaPadrao, RoundingMode.HALF_EVEN).doubleValue();
    }
    public static BigDecimal aplicarEscala(BigDecimal valor){
        return aplicarEscala(escalaPadrao, valor);
    }
    public static BigDecimal aplicarEscala4(BigDecimal valor){
        return valor.setScale(ESCALA4, RoundingMode.HALF_EVEN);
    }
    public static BigDecimal aplicarEscala(int escalaPadrao, BigDecimal valor){
        return valor.setScale(escalaPadrao, RoundingMode.HALF_EVEN);
    }
    public static Double seNuloOuZero(Double valor, Double valorPadrao){
        return valor == null || valor.compareTo(0.0)==0 ? valorPadrao : valor;
    }
    public static Integer seNuloOuZero(Integer valor, Integer valorPadrao){
        return valor == null || valor.compareTo(0)==0 ? valorPadrao : valor;

    }
    public static Double seNuloZera(Double valor){
        return valor == null ? 0.0 : valor;
    }
    public static Integer seNuloZera(Integer valor){
        return valor == null ? 0 : valor;
    }
    public static Integer seNuloUm(Integer valor){
        return valor == null ? 1 : valor;
    }
    public static BigDecimal seNuloOuZero(BigDecimal valor, BigDecimal valorPadrao){
        return valor ==null || valor == BigDecimal.ZERO ? valorPadrao : valor;
    }
    public static Double negativar(Double valor, boolean negativar){
        return negativar ? valor * -1 : valor;
    }

    public static Long centavos(Double reais){
        return  BigDecimal.valueOf(reais).multiply(BigDecimal.valueOf(100.0)).setScale(0).longValue();
    }
    public static Double reais(Long centavos){
        return  BigDecimal.valueOf(centavos).divide(BigDecimal.valueOf(100.0)).setScale(2).doubleValue();
    }

}
