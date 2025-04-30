package com.izatec.pay.core.comum;

import com.izatec.pay.core.cadastro.CadastroService;
import com.izatec.pay.core.cadastro.request.CadastroSimplesRequest;
import com.izatec.pay.core.cobranca.CobrancaRequest;
import com.izatec.pay.core.cobranca.CobrancaService;
import com.izatec.pay.core.cobranca.pagamento.*;
import com.izatec.pay.infra.Entidades;
import com.izatec.pay.infra.response.Response;
import com.izatec.pay.infra.response.ResponseFactory;
import com.izatec.pay.infra.response.ResponseMessage;
import com.izatec.pay.infra.util.GoogleImagem;
import com.izatec.pay.infra.util.Ip;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("publico")
@Slf4j
public class PublicoResource {
    @Autowired
    private CadastroService cadastroService;

    @Autowired
    private CobrancaService cobrancaService;

    @Autowired
    private PagamentoService pagamentoService;

    @Autowired
    private PagamentoConsultaService consultaService;

    //EM TESE SERÁ NECESSÁRIO O USO DE AUTENTICAÇÃO

    @PostMapping("/cobrancas")
    public Response gerarCobranca(@RequestBody CobrancaRequest requisicao) {

        if(requisicao.getNotificacao()==null)
            requisicao.setNotificacao(new NotificacaoRequest());

        requisicao.getNotificacao().setEmail(true);
        return ResponseFactory.create(cobrancaService.gerarCobranca(requisicao), ResponseMessage.geracao( Entidades.COBRANCA.getLegenda()));
    }
    @PostMapping("/pagamentos")
    public Response gerarPagamento(@RequestBody PagamentoRequest requisicao) {
        return ResponseFactory.create(pagamentoService.gerarPagamento(requisicao), ResponseMessage.geracao( Entidades.PAGAMENTO.getLegenda()));
    }

    @GetMapping("/pagamentos/{codigoIdentificacao}/integracao")
    public IntegracaoDetalhe buscarIntegracao(@PathVariable("codigoIdentificacao") String codigoIdentificacao) {
        Pagamento pagamento = consultaService.buscarPublica(codigoIdentificacao);
        IntegracaoDetalhe detalhe = new IntegracaoDetalhe();
        detalhe.setPagamento(pagamento.getId());
        detalhe.setIntegracao(pagamento.getIntegracao());
        return detalhe;
    }


    @GetMapping("/pagamentos/{id}/conteudo")
    public String validarVigencia(@PathVariable("id") Integer id) {
        return consultaService.buscarPublica(id).getIntegracao().getConteudo();
    }

    @GetMapping("/pagamentos/{id}/status")
    public String buscarStatus(@PathVariable("id") Integer id) {
        return consultaService.buscarPublica(id).getStatus().name();
    }

    @GetMapping("/pagamentos/{id}/link")
    public String buscarLink(@PathVariable("id") Integer id) {
        return consultaService.buscarPublica(id).getIntegracao().getLink();
    }

    @GetMapping("/pagamentos/{id}/qrcode")
    public ResponseEntity<byte[]> qrCodeAnexo(@PathVariable("id") Integer id) {
        try {
            byte[] imagemBytes = GoogleImagem.gerarQRCode(consultaService.buscarPublica(id).getIntegracao().getConteudo());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/png");
            return new ResponseEntity<>(imagemBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/empresa/{empresa}/cadastros")
    public Integer incluirCadastro(@PathVariable("empresa") Integer empresa, @RequestBody CadastroSimplesRequest requisicao) {
        return cadastroService.incluir(requisicao,empresa);
    }

    @GetMapping("/codigo-externo/{prefixo}")
    public Response  codigoExterno(@PathVariable("prefixo") String prefixo) {
        String str = UUID.randomUUID().toString();
        return ResponseFactory.ok(prefixo + str.replace("-", "").substring(0, 10-prefixo.length())) ;
    }

    @GetMapping("/{codigoExterno}/vigencia")
    public Response validarVigencia(@PathVariable("codigoExterno") String codigoExterno, HttpServletRequest request) {
        String ip = Ip.pegarIpUsuario(request);
        return ResponseFactory.ok(cobrancaService.validarVigencia(codigoExterno, ip),"Ativação realizada com sucesso \uD83D\uDE04 \uD83D\uDE80");
    }

}
