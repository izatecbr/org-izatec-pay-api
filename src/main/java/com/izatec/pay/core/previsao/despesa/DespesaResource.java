package com.izatec.pay.core.previsao.despesa;

import com.izatec.pay.core.comum.CompensacaoManualRequest;
import com.izatec.pay.infra.Entidades;
import com.izatec.pay.infra.response.Response;
import com.izatec.pay.infra.response.ResponseFactory;
import com.izatec.pay.infra.response.ResponseMessage;
import com.izatec.pay.infra.util.Filtros;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        return ResponseFactory.create(service.gerarDespesa(requisicao), ResponseMessage.geracao(RECURSO.getLegenda())) ;
    }

    @PatchMapping("/{id}/compensacao/manual")
    public Response compensarPagamento(@PathVariable("id") Integer id, @RequestBody CompensacaoManualRequest requisicao){
        compensacaoService.confirmarCompensacao(id,requisicao);
        return ResponseFactory.ok(true, "Compensação manual realizada com sucesso") ;
    }
    @PatchMapping("/{id}/quitacao/manual")
    public Response quitarPagamento(@PathVariable("id") Integer id, @RequestBody CompensacaoManualRequest requisicao) {
        compensacaoService.quitarDespesa(id, requisicao);
        return ResponseFactory.ok(true, "Quitação manual realizada com sucesso");
    }

    @GetMapping()
    public Response listar(@RequestParam Map<String, Object> criterios) {
        return ResponseFactory.ok(service.listar(Filtros.of(criterios)), ResponseMessage.consulta(RECURSO.getLegenda()));
    }

}
