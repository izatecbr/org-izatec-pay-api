package com.izatec.pay.core.cadastro;

import com.izatec.pay.core.cadastro.request.CadastroRequest;
import com.izatec.pay.infra.Entidades;
import com.izatec.pay.infra.response.Response;
import com.izatec.pay.infra.response.ResponseFactory;
import com.izatec.pay.infra.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cadastros")
public class CadastroResource {
    @Autowired
    private CadastroService service;

    private static final Entidades ENTITY = Entidades.CADASTRO;

    @PostMapping()
    public Response post(@RequestBody CadastroRequest request){
        return ResponseFactory.create(service.incluir(request), ResponseMessage.inclusao(ENTITY.getLegenda()));
    }
    @PutMapping(value = "/{id}")
    public Response put(@PathVariable ("id") Integer id, @RequestBody CadastroRequest request ){
        return ResponseFactory.create(service.alterar(id, request), ResponseMessage.alteracao(ENTITY.getLegenda()));
    }
    @GetMapping("/{id}")
    public Response buscar(@PathVariable("id") Integer id) {
        return ResponseFactory.ok(service.buscar(id), ResponseMessage.busca(ENTITY.getLegenda()));
    }

    @GetMapping()
    public Response listar(@RequestParam(required = false, value = "filtro") String filtro){
        return ResponseFactory.ok(service.listar(filtro));
    }

    @GetMapping("/documento/{documento}")
    public Response buscar(@PathVariable String documento){
        return ResponseFactory.ok(service.buscar(documento));
    }

/*
    @PostMapping("/bot/{codigoIdentificacao}")
    public Response cadastrar(@PathVariable("codigoIdentificacao") String codigoIdentificacao, @RequestBody CadastroBotRequest cadastroBotRequest) {
        service.cadastrar(codigoIdentificacao,cadastroBotRequest);
        return ResponseFactory.ok(true,"Cadastro realizado com sucesso");
    }*/

    /*@GetMapping("/bot/{empresa}/{whatsapp}")
    public Response bucar(@PathVariable("empresa") Integer empresa, @PathVariable("whatsapp") Long whatsapp)  {;
        return ResponseFactory.okOrNotFound(service.buscar(empresa, whatsapp));
    }*/
}
