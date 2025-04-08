package com.izatec.pay.core.previsao;

import com.izatec.pay.core.comum.Status;
import com.izatec.pay.core.previsao.despesa.DespesaService;
import com.izatec.pay.infra.Entidades;
import com.izatec.pay.infra.response.Response;
import com.izatec.pay.infra.response.ResponseFactory;
import com.izatec.pay.infra.response.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("previsoes")
@Slf4j
public class PrevisaoResource {
    private static final Entidades RECURSO = Entidades.DESPESA;
    @Autowired
    private PrevisaoService service;
    @Autowired
    private DespesaService pagamentoService;
    @PostMapping
    public Response gerar(@RequestBody PrevisaoRequest requisicao){
        return ResponseFactory.create(service.gerar(requisicao), ResponseMessage.geracao(RECURSO.getLegenda())) ;
    }
    @GetMapping()
    public Response listar(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim, @RequestParam(required = false) Status status ) {
        return ResponseFactory.ok(service.listar(dataInicio, dataFim, status),ResponseMessage.consulta(RECURSO.getLegenda()))  ;
    }
    @GetMapping("/{id}/despesas")
    public Response listarDespesas(@PathVariable("id") Integer id){
        return ResponseFactory.ok(pagamentoService.listar(id));
    }
}
