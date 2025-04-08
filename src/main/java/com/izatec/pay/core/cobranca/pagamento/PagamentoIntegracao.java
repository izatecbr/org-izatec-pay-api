package com.izatec.pay.core.cobranca.pagamento;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.izatec.pay.infra.util.JsonUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
@lombok.Data
public class PagamentoIntegracao {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonUtil.DATE_TIME)
    @Column(name = "int_dh_integracao")
    private LocalDateTime dataHora;
    @Column(name = "int_num_tentativas")
    private int numeroTentativas=0;
    @Column(name = "int_link")
    private String link;
    @Column(name = "int_conteudo")
    private String conteudo;
    @Column(name = "int_intermediador")
    private String intermediador;
    @Column(name = "int_erro")
    private boolean erro;
    public void incrementarTentativas(){
        numeroTentativas++;
    }
}
