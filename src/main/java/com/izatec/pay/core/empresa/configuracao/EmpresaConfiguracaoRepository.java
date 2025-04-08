package com.izatec.pay.core.empresa.configuracao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmpresaConfiguracaoRepository extends JpaRepository<EmpresaConfiguracao,String> {
    @Query("SELECT e FROM EmpresaConfiguracao e WHERE e.empresa.id = :empresa ")
    List<EmpresaConfiguracao> listarPorEmpresa(Integer empresa);
}
