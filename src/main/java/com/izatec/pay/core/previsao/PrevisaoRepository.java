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

    @Query("SELECT e FROM Previsao e WHERE e.empresa = :empresa AND e.negociacao.proximoVencimento BETWEEN :dataInicio AND :dataFim " +
            "AND (:status is null OR e.status = :status) ORDER BY e.dataGeracao.dia ")
    List<Previsao> listar(@Param("empresa") Integer empresa, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim, @Param("status") Status status);

}
