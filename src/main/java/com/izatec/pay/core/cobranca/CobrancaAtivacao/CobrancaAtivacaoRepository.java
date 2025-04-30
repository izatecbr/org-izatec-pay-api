package com.izatec.pay.core.cobranca.CobrancaAtivacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CobrancaAtivacaoRepository extends JpaRepository<CobrancaAtivacao, Integer> {

    @Query("SELECT DISTINCT COUNT(ca.ip) FROM CobrancaAtivacao ca WHERE ca.idCobranca = :idCobranca")
    Integer quantidadeIps(@Param("idCobranca") Integer idCobranca);

}
