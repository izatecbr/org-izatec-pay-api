package com.izatec.pay.core.cobranca.pagamento;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PagamentoIntegracaoRequest {
    private String cpfCnpj;
    private String nomeCliente;
    private String mensagem;
    private String observacao;
    private Double valor;
    private LocalDate dataVencimento;
    private String codigoIdentificacao;
    private Integer numeroParcelas;
}
