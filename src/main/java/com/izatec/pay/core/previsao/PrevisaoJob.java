package com.izatec.pay.core.previsao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class PrevisaoJob {
    @Autowired
    private PrevisaoRepository repository;
    @Autowired
    private PrevisaoService service;
    @Scheduled(cron = "0 * * * * *") // A expressão cron aqui significa: 0 segundos, a cada minuto
    public void gerarPagamentoParcelarRecorrente(){
        //LocalDate dataVencimento =  LocalDate.of(2025,4,10);
        LocalDate dataVencimento =  LocalDate.now();
        processarCobrancas(dataVencimento);
    }
    @Scheduled(cron = "0 0 6 * * *") // A expressão cron aqui significa: 0 segundos, 0 minutos, 8 horas
    public void gerarPagamentoParcelaRecorrenteFutura(){
        LocalDate dataVencimento = LocalDate.now();
        processarCobrancas(dataVencimento);
    }
    private void processarCobrancas(LocalDate dataVencimento){
        List<Previsao> cobrancas = repository.listarCobrancasAtivasAVencer(dataVencimento);
        log.info("Localizando cobrancas do dia: {}, total:{}", dataVencimento.toString(), cobrancas.size());
        service.processarCobranca(cobrancas);
    }
}
