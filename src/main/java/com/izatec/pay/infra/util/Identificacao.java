package com.izatec.pay.infra.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Identificacao {
    public static String codigoExterno(String codigo){
        return codigo==null || codigo.isBlank() ?  UUID.randomUUID().toString().replace("-", "").substring(0, 10) : codigo;
    }
    public static String codigo(String identificacao, String codigoExterno){
        String ce = String.format("%1$" + 10 + "s", codigoExterno).replace(' ', '0');
        return String.format("%s%s%s",identificacao,ce, DateTimeFormatter.ofPattern("yyMMddHHmm").format(LocalDateTime.now()));
    }
    public static String gerarCodigoExterno(Integer cobrancaDespesaId, Integer parcelaId){
        return String.format("%07d%03d",cobrancaDespesaId, parcelaId);
    }
}
