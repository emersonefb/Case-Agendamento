package br.com.efb.domain.repository;

import br.com.efb.domain.entity.GrupoTransferenciaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GrupoTransferenciaRepository extends JpaRepository<GrupoTransferenciaEntity,Long> {

    @Query("FROM GrupoTransferenciaEntity c " +
            "WHERE LOWER(c.nome) = :nome ")
    Page<GrupoTransferenciaEntity> search(
            @Param("nome") String nome,
            Pageable pageable);

    @Query("FROM GrupoTransferenciaEntity c " +
            "WHERE LOWER(c.nome) like %:nome% ")
    List<GrupoTransferenciaEntity> findNomeLike(
            @Param("nome") String nome);

    @Query("FROM GrupoTransferenciaEntity c ")
    Page<GrupoTransferenciaEntity> searchAll(
            Pageable pageable);


}
