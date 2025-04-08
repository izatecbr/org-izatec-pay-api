package com.izatec.pay.core.notificacao.template;


import com.izatec.pay.core.cadastro.Cadastro;
import com.izatec.pay.core.cadastro.CadastroRepository;
import com.izatec.pay.core.cobranca.Cobranca;
import com.izatec.pay.core.cobranca.CobrancaRepository;
import com.izatec.pay.core.cobranca.pagamento.Pagamento;
import com.izatec.pay.core.cobranca.pagamento.PagamentoRepository;
import com.izatec.pay.core.empresa.ConfiguracaoService;
import com.izatec.pay.core.empresa.configuracao.EmpresaConfiguracao;
import com.izatec.pay.core.notificacao.NotificacaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class NotificacaoCobrancaPagamentoWhatsapp {
    @Autowired
    private ConfiguracaoService configuracaoService;
    @Autowired
    private NotificacaoRepository repository;
    @Autowired
    private PagamentoRepository pagamentoService;
    @Autowired
    private CadastroRepository cadastroRepository;
    @Autowired
    private CobrancaRepository cobrancaRepository;
    @Transactional
    public void notificarCobrancaPagamento(Integer id){
        Pagamento pagto = pagamentoService.findById(id).orElse(null);
        if (pagto == null || pagto.getSacado()==null || pagto.getSacado().getId() ==null) {
            log.error("Não foi possível notifcar o pagamento  ID: {}", id);
            return;
        }
        Cobranca cob = cobrancaRepository.findById(pagto.getCobranca()).orElse(null);
        Cadastro cadastro = cadastroRepository.findById(pagto.getSacado().getId()).orElse(null);
        EmpresaConfiguracao configuracao = configuracaoService.buscarWhatsappConfiguracao(pagto.getEmpresa());

        StringBuilder conteudo = new StringBuilder();
        conteudo.append(String.format("Olá Sr(a) %s,\nSegue dados e QrCode para que você possa efetuar o pagamento conforme detalhes abaixo:\n\n", cadastro.getNomeCompleto()) );
        conteudo.append(String.format("Contrato: %s\n", cob.getTitulo()));
        conteudo.append(String.format("Parcela: %d de %d\n", pagto.getParcela(), cob.getQuantidadeParcelas()));
        conteudo.append(String.format("Vencimento: %s\n", DateTimeFormatter.ofPattern("dd/MM/yyyy").format(pagto.getDataVencimento().getDia())));

        conteudo.append("\nPIX COPIA-COLA\n");
        conteudo.append(String.format("\n%s\n", pagto.getIntegracao().getConteudo()));

        conteudo.append("\nFAVORECIDO\n");
        conteudo.append(String.format("Nome Completo: %s\n", cob.getConfiguracao().getNomeFantasia()));
        conteudo.append(String.format("CPF/CNPJ: %s\n", cob.getConfiguracao().getCnpj()));
        conteudo.append("Atenciosamente,\n\nIza Pay - Soluções em pagamentos\nemail: equipe@iza.tec.br\n");

    }
}