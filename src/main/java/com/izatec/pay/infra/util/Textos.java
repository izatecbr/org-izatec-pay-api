package com.izatec.pay.infra.util;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.izatec.pay.infra.util.Numeros.*;

public class Textos {
    public static String normaliza(String texto) {
        return texto ==null ? null : normaliza(texto, texto.length());
    }
    public static String normaliza(String texto, int comprimento) {
        return texto ==null ? null : maximo(java.text.Normalizer.normalize(texto, java.text.Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""),comprimento);
    }

    public static String nomeHumano(String nome) {
        return Optional.ofNullable(nome).map(n -> String.join(" ", Arrays.stream(n.toLowerCase().split(" ")).map(p -> StringUtils.capitalize(p)).collect(Collectors.toList()))).orElse(null);
    }

    public static String ajusta(String texto, int comprimento) {
        String format = "%-".concat(String.format("%d.%<ds", comprimento));
        return texto == null ? null : texto.length() == 0 ? "" : String.format(format, texto);
    }

    public static String maximo(Object texto, int comprimento) {
        return texto == null ? null : ajusta(texto.toString(), comprimento).trim().length() == 0 ? "" : ajusta(texto.toString(), comprimento).trim();
    }

    public static String localiza(Object... campos) {
        return localiza(null, campos);
    }

    public static String localiza(Integer comprimento, Object... campos) {
        String[] strings = Arrays.stream(campos).map(c -> Objects.toString(c, "")).toArray(String[]::new);
        String palavra = normaliza(String.join(" ", strings));
        return maximo(palavra, Integer.valueOf(Objects.toString(comprimento, palavra.length() + ""))).toUpperCase();
    }

    public static String maiusculo(String palavra) {
        return maiusculo(palavra, false);
    }

    public static String maiusculo(String palavra, boolean padraoNulo) {
        return Objects.nonNull(palavra) ? palavra.toUpperCase() : padraoNulo ? null : "";
    }

    public static String casoVazio(String texto, String outroTexto) {
        return texto == null || texto.isBlank() ? outroTexto : texto;
    }

    public static String digitos(String texto) {
        return texto==null ? null: texto.replaceAll("\\D", "");
    }

    public static String inibe(String texto) {
        if (texto == null || texto.isBlank())
            return "**";
        else {
            texto = digitos(texto);
            //sintaxe return
            return String.join("**", texto.substring(0, 3), texto.substring(texto.length() - 3, texto.length()));
        }
    }
    public static String une(Object ... campos) {
        return uniao("",campos);
    }
    public static String uniao(String simbolo, Object ... campos){
        String[] strings = Arrays.stream(campos).map(c -> Objects.toString(c, "")).toArray(String[]::new);
        return String.join(simbolo, strings);
    }
    public static String uuid32() {
        return uuid(N32);
    }
    public static String uuid16() {
        return uuid(N16);
    }
    public static String dateUuid16() {
        return uuid(true, N16);
    }
    public static String uuid12() {
        return uuid(N12);
    }
    public static String uuid(Integer comprimento) {
        return uuid(false, comprimento);
    }
    public static String uuid(boolean dia, Integer comprimento) {
        String uuid = UUID.randomUUID().toString().replaceAll("\\-", "");
        uuid = dia ? une(String.format("%ty%<tm%<td", LocalDate.now()), uuid) : uuid;
        uuid = uuid.substring(0,comprimento ==null || comprimento == 0 ? uuid.length() : Math.min(uuid.length(), comprimento));
        return uuid;
    }

}
