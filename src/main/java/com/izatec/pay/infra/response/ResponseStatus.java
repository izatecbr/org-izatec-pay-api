package com.izatec.pay.infra.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(name="Status da requisição", description="Representação padrão do status das respostas HTTP disponíveis na API")
public class ResponseStatus {

    @Schema(description="Data\\Hora da resposta", nullable = false,example = "2022-06-30 16:10:21")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime dateTime = LocalDateTime.now();

    @Schema(description="Confirmação de sucesso da resposta da requisição", nullable = false,example = "true", allowableValues ={"true","false"} )
    boolean success;
    @Schema(description="Mensagem que detalha a resposta devolvida", nullable = false,example = "Operação realizada com sucesso" )
    String message;
    @Schema(description="Código de sucesso ou baseado ao dicionário de erros da aplicação", nullable = false,example = "200" )
    Serializable code;
    @Schema(description="Mensagem que representa uma sugestão em caso de erro na requisição", nullable = false,example = "O campo: Nome é obrigatório" )
    String suggestion;
}
