package com.izatec.pay.core.comum;

import java.time.LocalDate;
import java.time.LocalTime;

@lombok.Data
public class DataRequest {
    private LocalDate dia;
    private LocalTime hora;
}
