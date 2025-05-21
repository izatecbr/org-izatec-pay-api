package com.izatec.pay.core.comum.titulo;

import lombok.Data;

@Data
public class Titulo {
    private String dataEmissao;
    private String dataVencimento;
    private String parcela;
    private String documento;
    private String numeroCobranca;
    private String instrucoes = "Pagamento deve ser realizado até a data de vencimento. Após essa data, juros de 1% ao mês e multa de 2% sobre o valor total.";
    private byte[] qrCode;
    private byte[] codigoBarras;
    private TituloValor valor;
    private TituloParticipante pagador;
    private TituloParticipante favorecido;
    private TituloParticipante emitente;
    private String especie="Pix";
}
