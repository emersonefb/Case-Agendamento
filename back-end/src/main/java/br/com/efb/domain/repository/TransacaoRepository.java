package br.com.efb.domain.repository;

import br.com.efb.domain.entity.TransacaoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransacaoRepository extends JpaRepository<TransacaoEntity,Long> {

    @Query("FROM TransacaoEntity c " +
            "WHERE c.contaDestino.contaCorrente = :contaCorrente ")
    Page<TransacaoEntity> searchByDestinoContaCorrente(
            @Param("contaCorrente") String contaCorrente,
            Pageable pageable);

    @Query("FROM TransacaoEntity c " +
            "WHERE c.contaOrigem.contaCorrente = :contaCorrente ")
    Page<TransacaoEntity> searchByOrigemContaCorrente(
            @Param("contaCorrente") String contaCorrente,
            Pageable pageable);

    @Query("FROM TransacaoEntity c " +
            "WHERE c.tipoTransferencia.idTipoTransferencia = :idTipoTransferencia ")
    Page<TransacaoEntity> searchByIdTipoTransferencia(
            @Param("idTipoTransferencia") Long idTipoTransferencia,
            Pageable pageable);

    @Query("FROM TransacaoEntity c ")
    Page<TransacaoEntity> searchAll(
            Pageable pageable);


}
