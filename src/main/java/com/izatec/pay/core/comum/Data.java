package com.izatec.pay.core.comum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.izatec.pay.infra.util.JsonUtil;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Embeddable
@lombok.Data
public class Data {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonUtil.DATE)
    private LocalDate dia;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonUtil.TIME)
    private LocalTime hora;
    public static Data of(LocalDateTime dataHora){
        return of(dataHora.toLocalDate(), dataHora.toLocalTime());
    }
    public static Data of(){
        return of(LocalDate.now(), LocalTime.now());
    }
    public static Data end(){
        return end(LocalDate.now());
    }
    public static Data end(LocalDate dia){
        return of(dia, LocalTime.of(23,59));
    }
    public static Data of(LocalDate dia, LocalTime hora){
        Data instante = new Data();
        instante.dia = dia ==null ? LocalDate.now() : dia;
        instante.hora = hora==null ? LocalTime.now() : hora;
        return instante;
    }
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonUtil.DATE_TIME)
    public LocalDateTime getDataHora(){
        return LocalDateTime.of(dia,hora);
    }
}