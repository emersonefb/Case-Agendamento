package br.com.efb.domain.service.impl;

import br.com.efb.domain.entity.ContaEntity;
import br.com.efb.domain.entity.TipoTransferenciaEntity;
import br.com.efb.domain.entity.TransacaoEntity;
import br.com.efb.domain.excepition.ErroException;
import br.com.efb.domain.mapper.TransacaoMapper;
import br.com.efb.domain.repository.GrupoTransferenciaRepository;
import br.com.efb.domain.repository.TipoTransferenciaRepository;
import br.com.efb.domain.repository.TransacaoRepository;
import br.com.efb.domain.service.TipoTransferenciaService;
import br.com.efb.domain.service.TransacaoService;
import com.efb.api.model.StatusEnumDTO;
import com.efb.api.model.TransacaoCadastroDTO;
import com.efb.api.model.TransacaoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransacaoServiceImpl implements TransacaoService {

    @Autowired
    private TransacaoRepository TransacaoRepository;

    @Autowired
    private ContaServiceImpl contaService;

    @Autowired
    private TipoTransferenciaService tipoTransferenciaService;

    @Autowired
    private GrupoTransferenciaRepository grupoTransferenciaRepository;

    @Autowired
    private TipoTransferenciaRepository tipoTransferenciaRepository;

    @Autowired
    TransacaoMapper TransacaoMapper;

    @Transactional
    @Override
    public TransacaoDTO cadastrarTransacao(TransacaoCadastroDTO body) {
        TransacaoEntity transacao = new TransacaoEntity();
        transacao.setContaOrigem(validarConta(body.getContaOrigem()));
        transacao.setContaDestino(validarConta(body.getContaDestino()));
        if (transacao.getContaDestino().equals(transacao.getContaOrigem())){
            throw new ErroException("Contas origem e destino não podem ser iguais");
        }
        transacao.setDataAgendamento(LocalDate.now());
        transacao.setDataProgramada(LocalDate.parse(body.getDataProgramada()));

        if (transacao.getDataProgramada().isBefore(transacao.getDataAgendamento())){
            throw new ErroException("Data não pode ser antes da data atual.");
        }
        calcularTransacao(body, transacao);

        TransacaoEntity save = TransacaoRepository.save(transacao);

        return TransacaoMapper.entityToDTO(save);
    }

    private TransacaoEntity calcularTransacao(TransacaoCadastroDTO cadastroDTO, TransacaoEntity transacao) {

        TipoTransferenciaEntity tipoTransferenciaEntitie = getTipoTransferenciaEntitie(cadastroDTO);

        BigDecimal vlFixo = cadastroDTO.getValorSolicitado().add(tipoTransferenciaEntitie.getTaxaFixa());
        BigDecimal taxaVariavel = tipoTransferenciaEntitie.getTaxaVariavel().divide(new BigDecimal(100));
        BigDecimal valorTrans = vlFixo.add(vlFixo.multiply(taxaVariavel));

        transacao.setTipoTransferencia(tipoTransferenciaEntitie);
        transacao.setValorSolicitado(cadastroDTO.getValorSolicitado());
        transacao.setTaxaFixa(tipoTransferenciaEntitie.getTaxaFixa());
        transacao.setTaxaVariavel(tipoTransferenciaEntitie.getTaxaVariavel());
        transacao.setValorTransacao( valorTrans);
        transacao.setStatus(StatusEnumDTO.ATIVO.name());

        return transacao;

    }
    private TipoTransferenciaEntity getTipoTransferenciaEntitie(TransacaoCadastroDTO body) {
        LocalDate dtProgamada = LocalDate.parse(body.getDataProgramada());
        LocalDate dtAgendamento = LocalDate.parse(body.getDataAgendamento());
        Long dias = getDiasParaProcessar(dtAgendamento, dtProgamada);

        TipoTransferenciaEntity tipoTransferenciaEntity = tipoTransferenciaService.getTipoTransferenciaEntityAtivo(dias, body.getValorSolicitado());
        if (Objects.isNull(tipoTransferenciaEntity)){
            throw new ErroException("Regra de taxa para transação não encontrada");
        }
        return tipoTransferenciaEntity;
    }

    private Long getDiasParaProcessar(LocalDate dtAgendamento, LocalDate dtProgamada) {
        return dtAgendamento.until(dtProgamada, ChronoUnit.DAYS);
    }

    private ContaEntity validarConta(String contaCorrente) {
        ContaEntity byContaCorrente = contaService.findByContaCorrente(contaCorrente);
        if (Objects.isNull(byContaCorrente)){
            throw new ErroException("Contas não encontrada");
        }
        return byContaCorrente;
    }

    @Override
    public TransacaoDTO getTransacao(Long codigo) {

        TransacaoDTO TransacaoDTO = new TransacaoDTO();

        Optional<TransacaoEntity> byId = TransacaoRepository.findById(codigo);
        if (byId.isPresent()){
            TransacaoDTO = TransacaoMapper.entityToDTO(byId.get());
        }
        return TransacaoDTO;
    }

    @Transactional
    @Override
    public TransacaoDTO save(TransacaoEntity TransacaoEntity) {
        TransacaoEntity Transacao = TransacaoRepository.save(TransacaoEntity);
        return TransacaoMapper.entityToDTO(Transacao);
    }

    @Transactional
    @Override
    public TransacaoDTO update(TransacaoEntity TransacaoEntity) {
        TransacaoEntity Transacao = TransacaoRepository.save(TransacaoEntity);
        return TransacaoMapper.entityToDTO(Transacao);
    }


    @Override
    public void deleteById(Long codigo) {
        try {
            TransacaoRepository.deleteById(codigo);
        } catch (EmptyResultDataAccessException e){
            return;
        }
    }
    @Transactional
    @Override
    public Page<TransacaoDTO> searchOrigem(String nome, PageRequest pageRequest) {
        Page<TransacaoEntity> search = TransacaoRepository.searchByOrigemContaCorrente(nome, pageRequest);
    return search.map(this::apply);
    }
    @Transactional
    @Override
    public Page<TransacaoDTO> searchByIdTipoTransferencia(Long idTipoTransferencia, PageRequest pageRequest) {
        Page<TransacaoEntity> search = TransacaoRepository.searchByIdTipoTransferencia(idTipoTransferencia, pageRequest);
    return search.map(this::apply);
    }

    @Override
    public void deletarListaTransacao(List<Long> body) {
        body.forEach(this::deleteById);
    }

    @Transactional
    @Override
    public Page<TransacaoDTO> searchDestino(String nome, PageRequest pageRequest) {
        Page<TransacaoEntity> search = TransacaoRepository.searchByDestinoContaCorrente(nome, pageRequest);
        return search.map(this::apply);
    }

    @Transactional
    @Override
    public Page<TransacaoDTO> searchAll(PageRequest pageRequest) {
        Page<TransacaoEntity> TransacaoEntities = TransacaoRepository.searchAll(pageRequest);
        return TransacaoEntities.map(this::apply);
    }

    private TransacaoDTO apply(TransacaoEntity entity) {
        return TransacaoMapper.entityToDTO(entity);
    }

}
