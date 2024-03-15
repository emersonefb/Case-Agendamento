package br.com.efb.domain.service.impl;

import br.com.efb.domain.entity.GrupoTransferenciaEntity;
import br.com.efb.domain.entity.TipoTransferenciaEntity;
import br.com.efb.domain.excepition.ErroException;
import br.com.efb.domain.mapper.GrupoTransferenciaMapper;
import br.com.efb.domain.repository.GrupoTransferenciaRepository;
import br.com.efb.domain.service.GrupoTransferenciaService;
import br.com.efb.domain.service.TipoTransferenciaService;
import com.efb.api.model.GrupoTransferenciaDTO;
import com.efb.api.model.GrupoTransferenciaResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GrupoTransferenciaServiceImpl implements GrupoTransferenciaService {

    @Autowired
    private TipoTransferenciaService tipoTransferenciaService;

    @Autowired
    private GrupoTransferenciaRepository grupoTransferenciaRepository;

    @Autowired
    GrupoTransferenciaMapper grupoTransferenciaMapper;


    @Transactional
    @Override
    public GrupoTransferenciaDTO getGrupoTransferencia(Long codigo) {

        GrupoTransferenciaDTO grupoTransferenciaDTO = new GrupoTransferenciaDTO();

        Optional<GrupoTransferenciaEntity> byId = grupoTransferenciaRepository.findById(codigo);
        if (byId.isPresent()){
            GrupoTransferenciaEntity grupoTransferenciaEntity = byId.get();
            grupoTransferenciaDTO = grupoTransferenciaMapper.entityToDTO(grupoTransferenciaEntity);
        }

        return grupoTransferenciaDTO;
    }

    @Transactional
    @Override
    public List<GrupoTransferenciaDTO> findAll() {
        List<GrupoTransferenciaEntity> all = grupoTransferenciaRepository.findAll();
        return grupoTransferenciaMapper.entityListToDTOList(all);
    }

    @Override
    public GrupoTransferenciaDTO findById(Long id) {

        Optional<GrupoTransferenciaEntity> byId = grupoTransferenciaRepository.findById(id);
       if (byId.isPresent()){
           return grupoTransferenciaMapper.entityToDTO(byId.get());
       }
       return null;
    }

    @Transactional
    @Override
    public GrupoTransferenciaDTO save(GrupoTransferenciaEntity grupoTransferenciaEntity) {
        if (Objects.nonNull(grupoTransferenciaEntity.getTipoTransferenciaList()) && !grupoTransferenciaEntity.getTipoTransferenciaList().isEmpty()){
            grupoTransferenciaEntity.getTipoTransferenciaList().stream().map(tipo -> {

                if (!tipoTransferenciaService.validarRegra(tipo)){
                    throw new ErroException("Conflito de regras cadastradas");
                };

                tipo.setGrupoTransferencia(grupoTransferenciaEntity);
                return tipo;
            }).collect(Collectors.toList());
        }
        GrupoTransferenciaEntity grupoTransferencia = grupoTransferenciaRepository.save(grupoTransferenciaEntity);
        return grupoTransferenciaMapper.entityToDTO(grupoTransferencia);
    }

    @Transactional
    @Override
    public GrupoTransferenciaDTO update(GrupoTransferenciaEntity grupoTransferenciaEntity) {
        if (Objects.nonNull(grupoTransferenciaEntity.getTipoTransferenciaList()) && !grupoTransferenciaEntity.getTipoTransferenciaList().isEmpty()){
            List<TipoTransferenciaEntity> list = grupoTransferenciaEntity.getTipoTransferenciaList().stream().map(subTipo -> {
                subTipo.setGrupoTransferencia(grupoTransferenciaEntity);
                return subTipo;
            }).collect(Collectors.toList());
        }
        GrupoTransferenciaEntity grupoTransferencia = grupoTransferenciaRepository.save(grupoTransferenciaEntity);
        return grupoTransferenciaMapper.entityToDTO(grupoTransferencia);
    }

    @Override
    public void deletarLista(List<Long> body) {
        body.forEach(this::deleteById);
    }

    @Transactional
    @Override
    public void deleteById(Long codigo) {
        try {
            grupoTransferenciaRepository.deleteById(codigo);
        } catch (EmptyResultDataAccessException e){
            return;
        }
    }

    @Transactional
    @Override
    public Page<GrupoTransferenciaResultDTO> search(String nome, PageRequest pageRequest) {
        Page<GrupoTransferenciaEntity> search = grupoTransferenciaRepository.search(nome, pageRequest);
        Page<GrupoTransferenciaResultDTO> grupoTransferenciaDTOS = search.map(entity -> {
            GrupoTransferenciaResultDTO dto = grupoTransferenciaMapper.entityToResultDTO(entity);
            return dto;
        });
        return grupoTransferenciaDTOS;
    }

    @Transactional
    @Override
    public Page<GrupoTransferenciaResultDTO> searchAll(PageRequest pageRequest) {
        Page<GrupoTransferenciaEntity> grupoTransferenciaEntities = grupoTransferenciaRepository.searchAll(pageRequest);

        Page<GrupoTransferenciaResultDTO> grupoTransferenciaDTOS = grupoTransferenciaEntities.map(entity -> {
            GrupoTransferenciaResultDTO dto = grupoTransferenciaMapper.entityToResultDTO(entity);
            return dto;
        });

        return grupoTransferenciaDTOS;
    }

    @Override
    public List<String> findNomeLike(String nome) {
        List<String> nomeLike = new ArrayList<>();
        List<GrupoTransferenciaEntity> list = grupoTransferenciaRepository.findNomeLike(nome);
        if (Objects.nonNull(list) && !list.isEmpty()){
            list.stream().forEach( grupoTransferenciaEntity -> {
                nomeLike.add(grupoTransferenciaEntity.getNome());
            });
        }
        return nomeLike;
    }
}
