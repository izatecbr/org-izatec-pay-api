package com.izatec.pay.core.acesso;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.izatec.pay.infra.util.JsonUtil;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Sessao {
    private String token;
    private String cnpj;
    private String nomeFantasia;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonUtil.DATE_TIME)
    private LocalDateTime dataHoraExpiracao;
}
