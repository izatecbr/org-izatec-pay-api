package com.izatec.pay.infra.util;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
public class Formatacao {
    public static String data(LocalDate data){
        return data.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.getDefault()));
    }
    public static String hora(LocalTime hora){
        return hora.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault()));
    }
    public static String horario(LocalTime hora){
        return hora.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).withLocale(Locale.getDefault()));
    }
    public static String dataHora(LocalDateTime dataHora){
        return dataHora.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.getDefault()));
    }
    private static final int ESCALA_PADRAO=2;

    public static String numero(Double numero){
        return numero(numero, ESCALA_PADRAO,false);
    }
    public static String moeda(Double numero){
        return numero(numero, ESCALA_PADRAO,true);
    }
    public static String numero(Double numero, int escala,boolean exibirMoeda){
        return numero(new BigDecimal(numero),escala, exibirMoeda);
    }
    public static String numero(BigDecimal numero){
        return numero(numero,ESCALA_PADRAO, false);
    }
    public static String moeda(BigDecimal numero){
        return numero(numero,ESCALA_PADRAO, true);
    }
    public static String numero(BigDecimal numero,boolean exibirMoeda){
        return numero(numero,ESCALA_PADRAO, exibirMoeda);
    }
    public static String numero(BigDecimal numero, int escala, boolean exibirMoeda){
        BigDecimal valor = numero.setScale(escala, RoundingMode.HALF_EVEN);
        return (exibirMoeda ? NumberFormat.getCurrencyInstance():NumberFormat.getNumberInstance()).format(valor);
    }
    public static String cpfCnpj(String cpfCnpj) {
        String texto = Textos.digitos(cpfCnpj);
        if(texto.length()==11)
            return texto.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        else if (texto.length()==14)
            return texto.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        else
            return texto;
    }

}
