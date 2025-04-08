package com.izatec.pay.core.previsao.despesa;

import com.izatec.pay.core.comum.CompensacaoManualRequest;
import com.izatec.pay.core.comum.PagamentoStatus;
import com.izatec.pay.infra.Entidades;
import com.izatec.pay.infra.response.Response;
import com.izatec.pay.infra.response.ResponseFactory;
import com.izatec.pay.infra.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("despesas")
public class DespesaResource {
    private static final Entidades RECURSO = Entidades.PAGAMENTO;
    @Autowired
    private DespesaService service;

    @Autowired
    private DespesaCompensacaoService compensacaoService;

    @PostMapping
    public Response gerarPagamento(@RequestBody DespesaRequest requisicao){
        return ResponseFactory.create(service.gerarPagamento(requisicao), ResponseMessage.geracao(RECURSO.getLegenda())) ;
    }

    @PatchMapping("/{id}/compensacao/manual")
    public Response compensarPagamento(@PathVariable("id") Integer id, @RequestBody CompensacaoManualRequest requisicao){
        compensacaoService.confirmarCompensacao(id,requisicao);
        return ResponseFactory.ok(true, "Compensação manual realizada com sucesso") ;
    }

    @GetMapping()
    public Response listar(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim, @RequestParam(required = false) PagamentoStatus status) {
        return ResponseFactory.ok(service.listar(dataInicio, dataFim, status),ResponseMessage.consulta(RECURSO.getLegenda()))  ;
    }

}
