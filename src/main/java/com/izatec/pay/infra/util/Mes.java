package com.izatec.pay.infra.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
@Data
public class Mes {
    /*
    LocalDate.now().atTime(LocalTime.MIDNIGHT); //00:00:00.000000000
    LocalDate.now().atTime(LocalTime.MIN);      //00:00:00.000000000
    LocalDate.now().atTime(LocalTime.NOON);     //12:00:00.000000000
    LocalDate.now().atTime(LocalTime.MAX);      //23:59:59.999999999
     */
    private String id;
    private Integer ano;
    private String nome;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonUtil.DATE)
    private LocalDate primeiroDia;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonUtil.DATE)
    private LocalDate ultimoDia;
    private Integer periodo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonUtil.TIMESTAMP)
    public LocalDateTime getPrimeiraDataHora(){
        return primeiroDia.atStartOfDay();
    }
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonUtil.TIMESTAMP)
    public LocalDateTime getUltimaDataHora(){
        return ultimoDia.atTime(LocalTime.MAX);  //23:59:59.999999999
    }

    public String getSigla() {
        return id.concat(String.format("/%d", ano));
    }
}
