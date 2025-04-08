package com.izatec.pay.core.cobranca.pagamento;

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
@RequestMapping("pagamentos")
public class PagamentoResource {
    private static final Entidades RECURSO = Entidades.PAGAMENTO;
    @Autowired
    private PagamentoService service;

    @Autowired
    private PagamentoConsultaService consultaService;

    @Autowired
    private PagamentoCompensacaoService compensacaoService;

    @PostMapping
    public Response gerarPagamento(@RequestBody PagamentoRequest requisicao) {
        return ResponseFactory.create(service.gerarPagamento(requisicao), ResponseMessage.geracao(RECURSO.getLegenda()));
    }

    @PostMapping("compensacao")
    public Response compensar(@RequestHeader("webhook-origem") String origem,
                              @RequestHeader("webhook-token") String token,
                              @RequestBody String pagamento) {
        return ResponseFactory.ok(compensacaoService.confirmarCompensacao(origem, token, pagamento));
    }

    @PostMapping("compensacao/lytex")
    public Response compensarLytex(@RequestBody String pagamento) {
        return ResponseFactory.ok(compensacaoService.confirmarCompensacao("LYTEX", "", pagamento));
    }

    @PatchMapping("/{id}/compensacao/manual")
    public Response compensarPagamento(@PathVariable("id") Integer id, @RequestBody CompensacaoManualRequest requisicao) {
        compensacaoService.confirmarCompensacao(id, requisicao);
        return ResponseFactory.ok(true, "Compensação manual realizada com sucesso");
    }


    @GetMapping("/id/{id}")
    public Response buscar(@PathVariable("id") Integer id) {
        return ResponseFactory.ok(consultaService.buscarAutenticada(id), ResponseMessage.busca(RECURSO.getLegenda()));
    }

    @GetMapping("/identificacao/{identificacao}")
    public Response buscar(@PathVariable("identificacao") String identificacao) {
        return ResponseFactory.ok(consultaService.buscarAutenticada(identificacao), ResponseMessage.busca(RECURSO.getLegenda()));
    }

    @GetMapping()
    public Response listar(@RequestParam Map<String, Object> criterios) {
        return ResponseFactory.ok(consultaService.listar(Filtros.of(criterios)), ResponseMessage.consulta(RECURSO.getLegenda()));
    }



}
