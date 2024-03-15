package br.com.efb.domain.service.impl;

import br.com.efb.domain.entity.GrupoTransferenciaEntity;
import br.com.efb.domain.entity.TipoTransferenciaEntity;
import br.com.efb.domain.excepition.ErroException;
import br.com.efb.domain.mapper.TipoTransferenciaMapper;
import br.com.efb.domain.repository.GrupoTransferenciaRepository;
import br.com.efb.domain.repository.TipoTransferenciaRepository;
import br.com.efb.domain.service.TipoTransferenciaService;
import com.efb.api.model.TipoTransferenciaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TipoTransferenciaServiceImpl implements TipoTransferenciaService {

    public static final Boolean STATUS_ATIVO = true;

    @Autowired
    private GrupoTransferenciaRepository grupoTransferenciaRepository;

    @Autowired
    private TipoTransferenciaRepository tipoTransferenciaRepository;

    @Autowired
    private TipoTransferenciaMapper tipoTransferenciaMapper;

    @Override
    public TipoTransferenciaDTO getTipoTransferencia(Long codigo) {

        TipoTransferenciaDTO tipoTransferenciaDTO = new TipoTransferenciaDTO();

        Optional<TipoTransferenciaEntity> byId = tipoTransferenciaRepository.findById(codigo);
        if (byId.isPresent()){
            tipoTransferenciaDTO = tipoTransferenciaMapper.entityToDTO(byId.get());
        }

        return tipoTransferenciaDTO;
    }

    @Override
    public List<TipoTransferenciaDTO> findAll() {
        return tipoTransferenciaMapper.entityListToDTOList(tipoTransferenciaRepository.findAll());
    }

    @Transactional
    @Override
    public TipoTransferenciaDTO save(TipoTransferenciaEntity tipoTransferenciaEntity) {

        if (Objects.isNull(tipoTransferenciaEntity.getGrupoTransferencia())){
            throw new ErroException("Grupo não informado");
        }else {
            Optional<GrupoTransferenciaEntity> byId = grupoTransferenciaRepository.findById(tipoTransferenciaEntity.getGrupoTransferencia().getIdGrupoTransferencia());
            if (byId.isPresent()){
                tipoTransferenciaEntity.setGrupoTransferencia(byId.get());
            }
        }
        if (!validarRegra(tipoTransferenciaEntity)){
            throw new ErroException("Conflito de regras cadastradas");
        };
        TipoTransferenciaEntity tipoTransferencia = tipoTransferenciaRepository.save(tipoTransferenciaEntity);
        return tipoTransferenciaMapper.entityToDTO(tipoTransferencia);
    }

    /*
    retorna true se regra não tem conflito.
     */
    @Override
    public boolean validarRegra(TipoTransferenciaEntity tipoTransferenciaEntity) {

        BigDecimal minValor = tipoTransferenciaEntity.getMinValor();
        BigDecimal maxValor = tipoTransferenciaEntity.getMaxValor();
        Long diasMin = tipoTransferenciaEntity.getMinDias().longValue();
        Long diasMax = tipoTransferenciaEntity.getMaxDias().longValue();

        Long dias ;
        BigDecimal valor;

        dias = diasMax;
        valor = maxValor;
        TipoTransferenciaEntity tipoTransferenciaNew;
        if (dias != 0 && !valor.equals(BigDecimal.ZERO)) {
            tipoTransferenciaNew = getTipoTransferenciaEntityAtivo(dias, valor);
            if (Objects.nonNull(tipoTransferenciaNew)) {
                if (tipoTransferenciaNew.getIdTipoTransferencia() != tipoTransferenciaEntity.getIdTipoTransferencia()){
                    throw new ErroException("Conflito entre regras "+tipoTransferenciaEntity.getNome()+ " e " + tipoTransferenciaNew.getNome() +" no valor maximo "+valor+" e data maxima "+dias+" verifique a tipo de transferencia: "+ tipoTransferenciaNew.getNome());
                }
            }
        }

        dias = diasMin;
        valor = maxValor;
        tipoTransferenciaNew = getTipoTransferenciaEntityAtivo(dias, valor);
        if (Objects.nonNull(tipoTransferenciaNew)) {
            if (tipoTransferenciaNew.getIdTipoTransferencia() != tipoTransferenciaEntity.getIdTipoTransferencia()){
                throw new ErroException("Conflito entre regras "+tipoTransferenciaEntity.getNome()+ " e " + tipoTransferenciaNew.getNome() +" no valor maximo "+valor+" e data minima "+dias+" verifique a tipo de transferencia: "+ tipoTransferenciaNew.getNome());
            }
        }

        dias = diasMax;
        valor = minValor;
        tipoTransferenciaNew = getTipoTransferenciaEntityAtivo(dias, valor);
        if (Objects.nonNull(tipoTransferenciaNew)) {
            if (tipoTransferenciaNew.getIdTipoTransferencia() != tipoTransferenciaEntity.getIdTipoTransferencia()){
                throw new ErroException("Conflito entre regras "+tipoTransferenciaEntity.getNome()+ " e " + tipoTransferenciaNew.getNome() +" no valor minimo "+valor+" e data maxima "+dias+" verifique a tipo de transferencia: "+ tipoTransferenciaNew.getNome());
            }
        }
        return true;

    }
    @Override
    public TipoTransferenciaEntity getTipoTransferenciaEntityAtivo(Long dias, BigDecimal valor) {
        List<TipoTransferenciaEntity> tipoTransferenciaEntities = tipoTransferenciaRepository.findTpTransByDiasAndValor(new BigDecimal(dias), valor, STATUS_ATIVO);
        if (tipoTransferenciaEntities.isEmpty()){
                return null;
        }
        return tipoTransferenciaEntities.get(0);
    }

    @Transactional
    @Override
    public TipoTransferenciaDTO update(TipoTransferenciaEntity tipoTransferenciaEntity) {

        Optional<GrupoTransferenciaEntity> byId = grupoTransferenciaRepository.findById(tipoTransferenciaEntity.getGrupoTransferencia().getIdGrupoTransferencia());
        byId.ifPresent(tipoTransferenciaEntity::setGrupoTransferencia);
        TipoTransferenciaEntity tipoTransferencia = tipoTransferenciaRepository.save(tipoTransferenciaEntity);

        return tipoTransferenciaMapper.entityToDTO(tipoTransferencia);
    }

    @Override
    public void deletarLista(List<Long> body) {
        body.forEach(this::deleteById);
    }


    @Override
    public void deleteById(Long codigo) {
        try {
            tipoTransferenciaRepository.deleteById(codigo);
        } catch (EmptyResultDataAccessException e){
            return;
        }
    }

    @Override
    public Page<TipoTransferenciaDTO> search(String nome, PageRequest pageRequest) {
        Page<TipoTransferenciaEntity> search = tipoTransferenciaRepository.search(nome, pageRequest);
        Page<TipoTransferenciaDTO> tipoTransferenciaDTOS = search.map(entity -> {
            return tipoTransferenciaMapper.entityToDTO(entity);
        });
        return tipoTransferenciaDTOS;
    }

    @Transactional
    @Override
    public Page<TipoTransferenciaDTO> searchAll(PageRequest pageRequest) {
        Page<TipoTransferenciaEntity> tipoTransferenciaEntities = tipoTransferenciaRepository.searchAll(pageRequest);

        Page<TipoTransferenciaDTO> tipoTransferenciaDTOS = tipoTransferenciaEntities.map(entity -> {
            TipoTransferenciaDTO dto = tipoTransferenciaMapper.entityToDTO(entity);
            return dto;
        });

        return tipoTransferenciaDTOS;
    }

    @Override
    public List<String> findNomeLike(String nome) {
        List<String> nomeLike = new ArrayList<>();
        List<TipoTransferenciaEntity> list = tipoTransferenciaRepository.findNomeLike(nome);
        if (Objects.nonNull(list) && !list.isEmpty()){
            list.stream().forEach( tipoTransferenciaEntity -> {
                nomeLike.add(tipoTransferenciaEntity.getNome());
            });
        }
        return nomeLike;
    }
}
