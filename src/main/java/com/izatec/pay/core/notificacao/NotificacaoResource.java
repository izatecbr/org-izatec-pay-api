package com.izatec.pay.core.notificacao;

import com.izatec.pay.core.notificacao.template.NotificacaoCobrancaPagamentoEmail;
import com.izatec.pay.core.notificacao.template.NotificacaoCobrancaPagamentoWhatsapp;
import com.izatec.pay.infra.response.Response;
import com.izatec.pay.infra.response.ResponseFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("notificacoes")
@Slf4j
public class NotificacaoResource {
    @Autowired
    private NotificacaoCobrancaPagamentoEmail service;

    @Autowired
    private NotificacaoCobrancaPagamentoWhatsapp whatsService;
    @GetMapping("/pagamentos/{id}/email")
    public Response notificarPagamento(@PathVariable("id") Integer id){
        service.notificarPagamento(id);
        log.info("Notificando cobrança {}", id);
        return ResponseFactory.ok(true,"Email enviado com sucesso");
    }
    @GetMapping("/pagamentos/{id}/whats")
    public Response notificarPagamentoWhats(@PathVariable("id") Integer id){
        whatsService.notificarCobrancaPagamento(id);
        log.info("Notificando cobrança {}", id);
        return ResponseFactory.ok(true,"Whatsapp enviado com sucesso");
    }
}
