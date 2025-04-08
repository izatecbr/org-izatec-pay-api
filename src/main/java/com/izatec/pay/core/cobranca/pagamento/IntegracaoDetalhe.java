package com.izatec.pay.core.cobranca.pagamento;

import lombok.Data;

@Data
public class IntegracaoDetalhe {
    private Integer pagamento;
    private PagamentoIntegracao integracao;
}
