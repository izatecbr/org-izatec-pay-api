package com.izatec.pay.core.comum;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.izatec.pay.infra.util.JsonUtil;

import java.time.LocalDate;
import java.time.LocalTime;

@lombok.Data
public class DataRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonUtil.DATE)
    private LocalDate dia;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonUtil.SHORT_TIME)
    private LocalTime hora;
}
