package com.izatec.pay.core.cobranca.ativacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CobrancaAtivacaoRepository extends JpaRepository<CobrancaAtivacao, Integer> {

    @Query("SELECT DISTINCT COUNT(ca.id) FROM CobrancaAtivacao ca WHERE ca.cobranca = :idCobranca")
    Integer quantidadeAtivacoes(@Param("idCobranca") Integer idCobranca);

    @Query("SELECT ca FROM CobrancaAtivacao ca WHERE ca.cobranca = :idCobranca and ca.ip = :ip")
    Optional<CobrancaAtivacao> localizar(@Param("idCobranca") Integer idCobranca, @Param("ip") String ip);

}