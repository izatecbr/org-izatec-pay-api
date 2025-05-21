package com.izatec.pay.core.cobranca.pagamento;

import com.izatec.pay.core.cadastro.ParceiroRequest;
import com.izatec.pay.core.comum.NotificacaoRequest;
import com.izatec.pay.core.comum.VencimentoRequest;
import lombok.Data;

@Data
public class PagamentoRequest {
    private String codigoIdentificacao;
    private String codigoExterno;
    private Double valor;
    private String mensagem;
    private String observacao;
    private VencimentoRequest vencimento;
    private ParceiroRequest sacado;
    private Integer parcela;
    private Integer cobranca;
    private NotificacaoRequest notificacao = new NotificacaoRequest();
    private boolean compensacaoManual;
}
