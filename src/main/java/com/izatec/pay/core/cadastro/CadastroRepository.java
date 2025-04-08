package com.izatec.pay.core.cadastro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CadastroRepository extends JpaRepository<Cadastro, Integer> {
    //@Query("SELECT e FROM Cadastro e WHERE e.empresa = :empresa AND e.documento = :documento")
    Optional<Cadastro> findFirstByEmpresaAndDocumento(Integer empresa, String documento);

    //@Query("SELECT e FROM Cadastro e WHERE e.empresa = :empresa AND e.whatsapp = :whatsapp")
    Optional<Cadastro> findFirstByEmpresaAndWhatsapp(Integer empresa, String whatsapp);

    @Query("SELECT e FROM Cadastro e WHERE e.empresa = :empresa " +
            "AND (:filtro is null OR e.nomeCompleto ILIKE %:filtro% " +
            "OR e.email ILIKE %:filtro% " +
            "OR e.whatsapp LIKE %:filtro% " +
            "OR e.documento LIKE %:filtro%) " +
            "ORDER BY e.nomeCompleto")
    List<Cadastro> listar(Integer empresa, String filtro);

    /** PARA MULT BANCOS NA MAIORIA
     * @Query("SELECT e FROM Cadastro e WHERE e.empresa = :empresa " +
     *             "AND (:nomeCompleto is null OR LOWER(e.nomeCompleto) LIKE LOWER(CONCAT('%', :nomeCompleto, '%'))) " +
     *             "ORDER BY e.nomeCompleto")
     */

    //ILIKE do postgres
}
