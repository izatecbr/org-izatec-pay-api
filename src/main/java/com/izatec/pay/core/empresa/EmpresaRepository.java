package com.izatec.pay.core.empresa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa,Integer> {
    Optional <Empresa> findByCpfCnpj(String cpfCnpj);
}
