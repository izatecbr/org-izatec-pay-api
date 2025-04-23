package com.izatec.pay.infra.util;


import com.izatec.pay.infra.Atributos;
import com.izatec.pay.infra.business.ParametroInvalidoException;
import com.izatec.pay.infra.business.PreenchimentoInvalidoException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validacoes {
    private static final int[] weightCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] weightCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-z0-9]+(\\.[a-z0-9]+)*@"
                    + "[a-z0-9]+(\\.[a-z0-9]+)*(\\.[a-z]{2,})$",
            Pattern.CASE_INSENSITIVE);


    public static boolean eVazio(Object object) {
        return object == null || "".equals(object.toString().trim());
    }

    public static boolean eNuloOuVazio(Object object) {
        return object == null || eVazio(object);
    }

    public static boolean ePreenchido(String valor) {
        return !eVazio(valor);
    }

    public static boolean eZero(Number value) {
        return BigDecimal.ZERO.compareTo(new BigDecimal(value.toString())) == 0;
    }

    private static int cpfCnpjCalculationDigits(String str, int[] peso) {
        int soma = 0;
        for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
            digito = Integer.parseInt(str.substring(indice, indice + 1));
            soma += digito * peso[peso.length - str.length() + indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }

    public static boolean eCpf(String cpf) {
        cpf = cpf.replaceAll("\\D", "");
        if ((cpf == null) || (cpf.length() != 11)) return false;

        if (eMesmoDigito(cpf)) return false;

        Integer digito1 = cpfCnpjCalculationDigits(cpf.substring(0, 9), weightCPF);
        Integer digito2 = cpfCnpjCalculationDigits(cpf.substring(0, 9) + digito1, weightCPF);
        return cpf.equals(cpf.substring(0, 9) + digito1.toString() + digito2.toString());
    }

    public static boolean eCnpj(String cnpj) {
        cnpj = cnpj.replaceAll("\\D", "");
        if ((cnpj == null) || (cnpj.length() != 14)) return false;

        if (eMesmoDigito(cnpj)) return false;

        Integer digito1 = cpfCnpjCalculationDigits(cnpj.substring(0, 12), weightCNPJ);
        Integer digito2 = cpfCnpjCalculationDigits(cnpj.substring(0, 12) + digito1, weightCNPJ);
        return cnpj.equals(cnpj.substring(0, 12) + digito1.toString() + digito2.toString());
    }

    public static void cpfCnpj(String cpfCnpj) {
        cpfCnpj(cpfCnpj, false);
    }

    public static void cpfCnpj(String cpfCnpj, boolean requerido) {
        if (!eCpfCnpj(cpfCnpj, requerido))
            throw new PreenchimentoInvalidoException("CPF/CNPJ", " é Inválido");
    }

    public static void email(String email) {
        if (!eEmail(email))
            throw new PreenchimentoInvalidoException("E-mail", "é Inválido");
    }

    public static boolean eCpfCnpj(String cpfCnpj) {
        return eCpfCnpj(cpfCnpj, false);
    }

    public static boolean eCpfCnpj(String cpfCnpj, boolean required) {
        if (cpfCnpj == null)
            return false;

        if (required && !ePreenchido(cpfCnpj))
            return false;

        cpfCnpj = cpfCnpj.replaceAll("\\D", "");
        if (cpfCnpj.length() < 11) return false;
        else {
            if (cpfCnpj.length() == 11)
                return eCpf(cpfCnpj);
            else
                return eCnpj(cpfCnpj);
        }
    }

    public static void telefone(Object numero, boolean requerido) {
        if (!eTelefone(numero, TelefoneTipo.AMBOS, requerido))
            throw new PreenchimentoInvalidoException("Telefone ou Celular", "é Inválido");
    }

    public static void cep(Object numero, boolean requerido) {
        if (!eCep(numero, requerido))
            throw new PreenchimentoInvalidoException("Cep", "é Inválido");
    }

    public static boolean eTelefone(Object numero, boolean required) {
        return eTelefone(numero, TelefoneTipo.TELEFONE, required);
    }

    public static boolean eCelular(Object numero, boolean required) {
        return eTelefone(numero, TelefoneTipo.CELULAR, required);
    }

    public static boolean eTelefone(Object numero, TelefoneTipo tipo, boolean required) {
        if (numero == null)
            return false;

        if (required && !ePreenchido(numero.toString()))
            return false;

        return numero.toString().replaceAll("\\D", "").matches(tipo.getPadrao());
    }

    public static boolean eCep(Object numero, boolean required) {
        if (numero == null)
            return false;

        if (required && !ePreenchido(numero.toString()))
            return false;

        return numero.toString().replaceAll("\\D", "").matches("\\d{8}");
    }

    public static boolean eEmail(String email, boolean required) {
        if (required && !ePreenchido(email))
            return false;
        if (email == null)
            return true;
        else {
            Matcher matcher = EMAIL_PATTERN.matcher(email);
            return matcher.matches();
        }
    }

    public static boolean eEmail(String email) {
        return eEmail(email, false);
    }

    private static boolean eMesmoDigito(String cpfCnpj) {
        for (int x = 0; x <= 9; x++) {
            Pattern p = Pattern.compile(String.format("^[%d]{11}$", x));
            Matcher m = p.matcher(cpfCnpj);
            if (m.matches())
                return true;
        }
        return false;
    }

    public static void obrigatorio(String rotulo, Object valor) {
        obrigatorio(rotulo, valor, null, null);
    }

    public static void obrigatorio(Object valor, Comprimento comprimento, Integer numeroCaracteres, String rotulo) {
        obrigatorio(rotulo, valor, Comprimento.MINIMO == comprimento ? numeroCaracteres : null, Comprimento.MAXIMO == comprimento ? numeroCaracteres : null);
    }

    public static void obrigatorio(Atributos atributo, Object valor, Integer comprimentoMinimo, Integer comprimentoMaximo) {
        obrigatorio(atributo.getLegenda(), valor, comprimentoMinimo, comprimentoMaximo);
    }

    public static void obrigatorio(String rotulo, Object valor, Integer comprimentoMinimo, Integer comprimentoMaximo) {
        if (valor == null)
            throw new PreenchimentoInvalidoException(rotulo, "é obrigatório");
        String v = valor.toString();
        if (v.isBlank())
            throw new PreenchimentoInvalidoException(rotulo, "não pode ser vazio");
        if (comprimentoMinimo != null && eComprimentoMinimo(valor, comprimentoMinimo))
            throw new PreenchimentoInvalidoException(rotulo, "deve possuir no mínimo " + comprimentoMinimo + " caracteres");
        if (comprimentoMaximo != null && eComprimentoMaximo(valor, comprimentoMaximo))
            throw new PreenchimentoInvalidoException(rotulo, "deve possuir no máximo " + comprimentoMaximo + " caracteres");
    }

    public static void comprimentoMinimo(String rotulo, Object valor, int comprimentoMinimo) {
        if (eComprimentoMinimo(valor, comprimentoMinimo))
            throw new PreenchimentoInvalidoException(rotulo, "deve possuir no mínimo " + comprimentoMinimo + " caracteres");
    }

    public static void comprimentoMaximo(String rotulo, Object valor, int comprimentoMaximo) {
        if (eComprimentoMaximo(valor, comprimentoMaximo))
            throw new PreenchimentoInvalidoException(rotulo, "deve possuir no máximo " + comprimentoMaximo + " caracteres");
    }

    public static void comprimento(String rotulo, Object valor, int comprimentoMinimo, int comprimentoMaximo) {
        comprimentoMinimo(rotulo, valor, comprimentoMinimo);
        comprimentoMaximo(rotulo, valor, comprimentoMaximo);

    }

    public static boolean eComprimentoMinimo(Object valor, int comprimentoMinimo) {
        return valor == null || valor.toString().length() < comprimentoMinimo;
    }

    public static boolean eComprimentoMaximo(Object valor, int comprimentoMinimo) {
        return valor == null || valor.toString().length() > comprimentoMinimo;
    }

    public static void intervalo(LocalDate dataInicial, LocalDate dataFinal) {
        long count = Arrays.asList(new LocalDate[]{dataInicial, dataFinal}).stream().filter(Objects::nonNull).count();
        if (count == 1) throw new ParametroInvalidoException("Data Inicial e Data Final Precisam ser preenchidos");
        else if (count == 2) {
            if (dataFinal.isBefore(dataInicial))
                throw new ParametroInvalidoException("Data Final inferior a Data Inicial");
            if (Period.between(dataInicial, dataFinal).getMonths() > 0)
                throw new ParametroInvalidoException("O período entre a data inicial e final é superior a 30 dias");
        }

    }
}