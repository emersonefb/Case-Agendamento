package br.com.efb.domain.entity;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="TipoTransferencia",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"nome"},
                        name="uk_nome"
                )})
@SequenceGenerator(name="generator", sequenceName="idTipoTransferencia_seq", allocationSize=1)
@Audited
@Data
public class TipoTransferenciaEntity {
  @Id
  @GeneratedValue( strategy = GenerationType.AUTO )
  @Column (name ="idTipoTransferencia")
  private Long idTipoTransferencia;

  @Column (name ="nome")
  private String nome;

  @Column (name ="descricao")
  private String descricao;

  @Column (name ="minDias")
  private BigDecimal minDias;

  @Column (name ="maxDias")
  private BigDecimal maxDias;

  @Column (name ="minValor")
  private BigDecimal minValor;

  @Column (name ="maxValor")
  private BigDecimal maxValor;

  @Column (name ="taxaFixa")
  private BigDecimal taxaFixa;

  @Column (name ="taxaVariavel")
  private BigDecimal taxaVariavel;

  @Column(name ="status")
  private Boolean status;

  @ManyToOne
  @JoinColumn(name = "id_grupo_Transferencia", nullable=false, updatable = false)
  private GrupoTransferenciaEntity grupoTransferencia;

  @OneToMany(cascade=CascadeType.REFRESH, mappedBy = "tipoTransferencia", fetch=FetchType.LAZY)
  @Fetch(value = FetchMode.SUBSELECT)
  private List<TransacaoEntity> transacaoList;
}
