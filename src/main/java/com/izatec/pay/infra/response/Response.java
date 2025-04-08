package com.izatec.pay.infra.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="Resposta da requisição", description="Representação padrão do conteúdo das respostas HTTP disponíveis na API")
public class Response {
	ResponseStatus status;
	@Schema(description="Corpo da resposta da requisição que pode ser uma lista, um objeto ou um elemento", nullable = false,example = "{\"id\":1,\"nome\":\"ADMINISTRADOR\"}" )
	Object body;
}
