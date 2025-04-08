package com.izatec.pay.core.cobranca.pagamento;

import com.izatec.pay.core.comum.PagamentoStatus;
import lombok.Data;

@Data
public class IntegracaoResponse {
    private PagamentoStatus status;
    private String conteudo;
    private String link;
}
