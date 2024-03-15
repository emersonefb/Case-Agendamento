package br.com.efb.domain.entity;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="GrupoTransferencia",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"nome"},
                        name="uk_nome"
                )})
@SequenceGenerator(name="generator", sequenceName="idGrupoTransferencia_seq", allocationSize=1)
@Audited
@Data
public class GrupoTransferenciaEntity {
  
  @Id
  @GeneratedValue( strategy = GenerationType.AUTO )
  @Column (name ="idGrupoTransferencia")
  private Long idGrupoTransferencia;

  @Column (name ="nome")
  private String nome;

  @Column (name ="descricao")
  private String descricao;

  @Column (name ="status")
  private Boolean status;

  @OneToMany(cascade=CascadeType.ALL, mappedBy = "grupoTransferencia",fetch = FetchType.LAZY)
  @Fetch(value = FetchMode.SUBSELECT)
  private List<TipoTransferenciaEntity> tipoTransferenciaList;



}
