package com.izatec.pay.core.previsao;

import com.izatec.pay.core.comum.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PrevisaoRepository extends JpaRepository <Previsao, Integer> {
    @Query("SELECT e FROM Previsao e WHERE e.status = com.izatec.pay.core.comum.Status.ATIVA AND e.negociacao.proximoVencimento=:proximoVencimento ")
    List<Previsao> listarCobrancasAtivasAVencer(LocalDate proximoVencimento);

    @Query("SELECT e FROM Previsao e WHERE e.empresa = :empresa " +
            "AND (:status is null OR e.status = :status) " +
            "AND (:favorecido is null OR e.favorecido.id = :favorecido) " +
            "AND (:dataInicio is null or e.negociacao.proximoVencimento >= TO_DATE(cast(:dataInicio as text), 'yyyy-MM-dd')) " +
            "AND (:dataFim is null or e.negociacao.proximoVencimento <= TO_DATE(cast(:dataFim as text), 'yyyy-MM-dd')) " +
            "ORDER BY e.negociacao.proximoVencimento ")
    List<Previsao> listar(@Param("empresa") Integer empresa,
                          @Param("favorecido") Integer favorecido,
                          @Param("status") Status status,
                          @Param("dataInicio") String dataInicio,
                          @Param("dataFim") String dataFim);



}
