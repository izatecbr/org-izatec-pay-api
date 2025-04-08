package com.izatec.pay.core.acesso;

import com.izatec.pay.infra.response.Response;
import com.izatec.pay.infra.response.ResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("acessos")
public class AcessoResource {
    @Autowired
    private AcessoService service;
    @PostMapping("integracao")
    public Response autenticarIntegracao(@RequestBody Login login){
        return ResponseFactory.ok(service.autenticarIntegracao(login),"Autenticação realizada com sucesso");
    }
    @PostMapping("empresa")
    public Response autenticarEmpresa(@RequestBody Login login){
        return ResponseFactory.ok(service.autenticarEmpresa(login),"Autenticação realizada com sucesso");
    }
}
