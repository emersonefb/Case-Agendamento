package br.com.efb.domain.repository;

import br.com.efb.domain.entity.ContaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContaRepository extends JpaRepository<ContaEntity,Long> {

    @Query("FROM ContaEntity c " +
            "WHERE LOWER(c.nome) like %:nome% ")
    Page<ContaEntity> search(
            @Param("nome") String nome,
            Pageable pageable);

    @Query("FROM ContaEntity c " +
            "WHERE c.contaCorrente = :contaCorrente ")
    ContaEntity findByContaCorrente(
            @Param("contaCorrente") String contaCorrente);

    @Query("FROM ContaEntity c " +
            "WHERE LOWER(c.nome) like %:nome% ")
    List<ContaEntity> findNomeLike(
            @Param("nome") String nome);

    @Query("FROM ContaEntity c ")
    Page<ContaEntity> searchAll(
            Pageable pageable);


}
