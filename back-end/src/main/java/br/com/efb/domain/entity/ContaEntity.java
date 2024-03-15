package br.com.efb.domain.entity;

import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="Conta", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"contaCorrente"},
                name="uk_contaCorrente"
        )})
@SequenceGenerator(name="generator", sequenceName="idConta_seq", allocationSize=1)
@Audited
@Data
public class ContaEntity {
  @Id
  @GeneratedValue( strategy = GenerationType.AUTO )
  @Column(name = "idConta")
  private Long idConta;

  @Column (name ="contaCorrente")
  private String contaCorrente;

  @Column (name ="nome")
  private String nome;

  @Column (name ="dataCadastro")
  private LocalDate dataCadastro;

  @Column (name ="status")
  private Boolean status;

  @OneToMany(cascade=CascadeType.ALL, mappedBy = "contaOrigem",fetch = FetchType.LAZY)
  private List<TransacaoEntity> contaOrigemList;

  @OneToMany(cascade=CascadeType.ALL, mappedBy = "contaDestino",fetch = FetchType.LAZY)
  private List<TransacaoEntity> contaDestinoList;

}
