package br.com.efb.domain.repository;

import br.com.efb.domain.entity.TipoTransferenciaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TipoTransferenciaRepository extends JpaRepository<TipoTransferenciaEntity,Long> {

    @Query("FROM TipoTransferenciaEntity c " +
            "WHERE LOWER(c.nome) = :nome ")
    Page<TipoTransferenciaEntity> search(
            @Param("nome") String nome,
            Pageable pageable);

    @Query("FROM TipoTransferenciaEntity c " +
            "WHERE LOWER(c.nome) like %:nome% ")
    List<TipoTransferenciaEntity> findNomeLike(
            @Param("nome") String nome);

    @Query("FROM TipoTransferenciaEntity c ")
    Page<TipoTransferenciaEntity> searchAll(
            Pageable pageable);


    @Query("FROM TipoTransferenciaEntity tptransf WHERE :dias BETWEEN tptransf.minDias AND tptransf.maxDias " +
            "or (tptransf.minDias <> 0  and tptransf.minDias < :dias  and tptransf.maxDias = 0)" +
            "and tptransf.status = true" +
            " ORDER BY tptransf.idTipoTransferencia")
    List<TipoTransferenciaEntity> findTpTransByDiasAndValor(@Param("dias") BigDecimal dias);

    @Query("FROM TipoTransferenciaEntity tptransf " +
            "WHERE (:dias BETWEEN tptransf.minDias AND tptransf.maxDias " +
            "or (tptransf.minDias <> 0  and tptransf.minDias < :dias  and tptransf.maxDias = 0))" +
            "and ((:valor BETWEEN tptransf.minValor AND tptransf.maxValor) " +
            "or (:valor >= tptransf.minValor  AND tptransf.maxValor =0 ) )" +
            "and tptransf.status = :status" +
            " ORDER BY tptransf.idTipoTransferencia")
    List<TipoTransferenciaEntity> findTpTransByDiasAndValor(@Param("dias")BigDecimal dias, @Param("valor") BigDecimal valor, @Param("status") Boolean status);
}
