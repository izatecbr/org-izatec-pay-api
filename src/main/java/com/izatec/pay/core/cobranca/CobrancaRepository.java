package com.izatec.pay.core.cobranca;

import com.izatec.pay.core.comum.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CobrancaRepository extends JpaRepository <Cobranca, Integer> {

    Optional<Cobranca> findByCodigoExterno(String codigoExterno);

    @Query("SELECT e FROM Cobranca e WHERE e.status = com.izatec.pay.core.comum.Status.ATIVA AND e.negociacao.proximoVencimento=:proximoVencimento AND e.codigoExterno NOT LIKE LOWER(CONCAT('free', '%')) ")
    List<Cobranca> listarCobrancasAtivasAVencer(LocalDate proximoVencimento);

    @Query("SELECT e FROM Cobranca e WHERE (e.notificacao.whatsapp = true OR e.notificacao.email = true) AND e.codigoExterno LIKE LOWER(CONCAT('free', '%')) ")
    List<Cobranca> listarNotificacoesPromocionais();

    @Query("SELECT e FROM Cobranca e WHERE e.empresa = :empresa " +
            "AND (:status is null OR e.status = :status) " +
            "AND (:sacado is null OR e.sacado.id = :sacado) " +
            "AND (:dataInicio is null or e.negociacao.proximoVencimento >= TO_DATE(cast(:dataInicio as text), 'yyyy-MM-dd')) " +
            "AND (:dataFim is null or e.negociacao.proximoVencimento <= TO_DATE(cast(:dataFim as text), 'yyyy-MM-dd')) " +
            "ORDER BY e.negociacao.proximoVencimento ")
    List<Cobranca> listar(@Param("empresa") Integer empresa,
                          @Param("sacado") Integer sacado,
                          @Param("status") Status status,
                          @Param("dataInicio") String dataInicio,
                          @Param("dataFim") String dataFim);

}
