package com.izatec.pay.core.cobranca;


import com.izatec.pay.core.cobranca.pagamento.PagamentoConsultaService;
import com.izatec.pay.core.cobranca.pagamento.PagamentoService;
import com.izatec.pay.infra.Entidades;
import com.izatec.pay.infra.response.Response;
import com.izatec.pay.infra.response.ResponseFactory;
import com.izatec.pay.infra.response.ResponseMessage;
import com.izatec.pay.infra.util.Filtros;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("cobrancas")
@Slf4j
public class CobrancaResource {
    private static final Entidades RECURSO = Entidades.COBRANCA;

    @Autowired
    private CobrancaService service;

    @Autowired
    private PagamentoConsultaService consultaService;

    @PostMapping
    public Response gerar(@RequestBody CobrancaRequest requisicao) {
        return ResponseFactory.create(service.gerarCobranca(requisicao), ResponseMessage.geracao(RECURSO.getLegenda()));
    }

    @GetMapping("/{id}/pagamentos")
    public Response listarPagamentos(@PathVariable("id") Integer id) {
        return ResponseFactory.ok(consultaService.listar(id));
    }

    @GetMapping()
    public Response listar(@RequestParam Map<String, Object> criterios) {
        return ResponseFactory.ok(service.listar(Filtros.of(criterios)), ResponseMessage.consulta(RECURSO.getLegenda()));
    }
}
