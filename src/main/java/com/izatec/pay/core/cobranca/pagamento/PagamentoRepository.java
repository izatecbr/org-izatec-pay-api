package com.izatec.pay.core.cobranca.pagamento;

import com.izatec.pay.core.comum.PagamentoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
    Optional<Pagamento> findByEmpresaAndCodigoIdentificacao(@Param("empresa") Integer empresa, String codigoIdentificacao);

    Optional<Pagamento> findByCodigoIdentificacao(String codigoIdentificacao);

    Optional<Pagamento> findByEmpresaAndId(@Param("empresa") Integer empresa, @Param("id") Integer id);

    @Query("SELECT e FROM Pagamento e WHERE e.empresa = :empresa AND e.cobranca = :cobranca ORDER BY e.id")
    List<Pagamento> listar(@Param("empresa") Integer empresa, @Param("cobranca") Integer cobranca);


    @Query("SELECT COALESCE(c.quantidadeParcelas, 1) FROM Cobranca c WHERE c.id = :id")
    Integer buscarQuantidadeParcelas(@Param("id") Integer id);

    @Query("SELECT e FROM Pagamento e WHERE e.empresa = :empresa " +
            "AND (:status is null OR e.status = :status) " +
            "AND (:sacado is null OR e.sacado.id = :sacado) " +
            "AND e.dataVencimento.dia BETWEEN :dataInicio AND :dataFim " +
            "ORDER BY e.dataGeracao.dia ")
    List<Pagamento> listar(@Param("empresa") Integer empresa,
                           @Param("sacado") Integer sacado,
                           @Param("status") PagamentoStatus status,
                           @Param("dataInicio") LocalDate dataInicio,
                           @Param("dataFim") LocalDate dataFim);
}
