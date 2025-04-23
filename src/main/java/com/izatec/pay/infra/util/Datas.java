package com.izatec.pay.infra.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Datas {
    public static LocalDateTime inicio(LocalDate dia){
        return dia.atStartOfDay();
    }
    public static LocalDateTime fim(){
        return fim(LocalDate.now());
    }
    public static LocalDateTime fim(LocalDate dia){
        return dia.atTime(23,59,59,999999999);
    }
    public static LocalDateTime hojeSeNulo(LocalDate data, LocalTime hora){
        return LocalDateTime.of(data == null ? LocalDate.now(): data, hora == null ? LocalTime.now() : hora);
    }
    public static LocalDate[] semanal() {
        LocalDate hoje = LocalDate.now();
        Integer diaSemana = hoje.getDayOfWeek().getValue();
        return new LocalDate [] {hoje.minusDays(diaSemana-1),hoje.plusDays(7-diaSemana)};
    }
}
