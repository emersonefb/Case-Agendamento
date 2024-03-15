package br.com.efb.domain.entity;

import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="Transacao")
@SequenceGenerator(name="generator", sequenceName="idTransacao_seq", allocationSize=1)
@Audited
@Data
public class TransacaoEntity {

  @Id
  @GeneratedValue( strategy = GenerationType.AUTO )
  @Column (name ="idTransacao")
  private Long idTransacao;

  @Column (name ="dataAgendamento")
  private LocalDate dataAgendamento;

  @Column (name ="dataProgramada")
  private LocalDate dataProgramada;

  @Column (name ="valorSolicitado")
  private BigDecimal valorSolicitado;

  @Column (name ="valorTransacao")
  private BigDecimal valorTransacao;

  @OneToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "idContaOrigem", referencedColumnName = "idConta")
  private ContaEntity contaOrigem;

  @OneToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "idContaDestino", referencedColumnName = "idConta")
  private ContaEntity contaDestino;

  @ManyToOne
  @JoinColumn(name = "idTipoTransferencia", referencedColumnName = "idTipoTransferencia")
  private TipoTransferenciaEntity tipoTransferencia;

  @Column (name ="taxaFixa")
  private BigDecimal taxaFixa;

  @Column (name ="taxaVariavel")
  private BigDecimal taxaVariavel;

  @Column(name = "status")
  private String status;

}
