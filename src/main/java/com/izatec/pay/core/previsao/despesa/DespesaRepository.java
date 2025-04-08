package com.izatec.pay.core.previsao.despesa;

import com.izatec.pay.core.comum.PagamentoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DespesaRepository extends JpaRepository<Despesa, Integer> {
    /*@Query("SELECT e FROM DespesaPagamento e WHERE e.empresa = :empresa AND e.dataGeracao.dia BETWEEN :dataInicio AND :dataFim AND e.status = :status")
    List<DespesaPagamento> listarPorDataGeracao(@Param("empresa") Integer empresa, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim, @Param("status") PagamentoStatus status);
*/
    @Query("SELECT e FROM Despesa e WHERE e.empresa = :empresa AND e.dataVencimento.dia BETWEEN :dataInicio AND :dataFim " +
            "AND (:status is null OR e.status = :status) ORDER BY e.dataVencimento.dia")
    List<Despesa> listar(@Param("empresa") Integer empresa, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim, @Param("status") PagamentoStatus status);

    @Query("SELECT e FROM Despesa e WHERE e.empresa = :empresa AND e.previsao = :despesa ORDER BY e.id")
    List<Despesa> listar(@Param("empresa") Integer empresa, @Param("despesa") Integer despesa);

}
