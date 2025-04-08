package com.izatec.pay.core.cobranca;

import com.izatec.pay.core.comum.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CobrancaRepository extends JpaRepository <Cobranca, Integer> {
    @Query("SELECT e FROM Cobranca e WHERE e.status = com.izatec.pay.core.comum.Status.ATIVA AND e.negociacao.proximoVencimento=:proximoVencimento ")
    List<Cobranca> listarCobrancasAtivasAVencer(LocalDate proximoVencimento);

    @Query("SELECT e FROM Cobranca e WHERE e.empresa = :empresa " +
            "AND (:status is null OR e.status = :status) " +
            "AND (:sacado is null OR e.sacado.id = :sacado) " +
            "AND e.negociacao.proximoVencimento BETWEEN :dataInicio AND :dataFim " +
            "ORDER BY e.dataGeracao.dia ")
    List<Cobranca> listar(@Param("empresa") Integer empresa,
                          @Param("sacado") Integer sacado,
                          @Param("status") Status status,
                          @Param("dataInicio") LocalDate dataInicio,
                          @Param("dataFim") LocalDate dataFim);

}
