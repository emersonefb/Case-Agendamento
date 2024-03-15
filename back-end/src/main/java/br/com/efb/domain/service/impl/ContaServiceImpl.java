package br.com.efb.domain.service.impl;

import br.com.efb.domain.entity.ContaEntity;
import br.com.efb.domain.excepition.ErroException;
import br.com.efb.domain.mapper.ContaMapper;
import br.com.efb.domain.repository.ContaRepository;
import br.com.efb.domain.service.ContaService;
import com.efb.api.model.ContaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ContaServiceImpl implements ContaService {

    @Autowired
    private ContaRepository contaRepository;

   @Autowired
    ContaMapper contaMapper;

    @Override
    public ContaDTO getConta(Long codigo) {

        ContaDTO contaDTO = new ContaDTO();

        Optional<ContaEntity> byId = contaRepository.findById(codigo);
        if (byId.isPresent()){
            contaDTO = contaMapper.entityToDTO(byId.get());
        }

        return contaDTO;
    }

    @Override
    public List<ContaDTO> findAll() {
        return contaMapper.entityListToDTOList(contaRepository.findAll());
    }

    @Override
    public ContaDTO save(ContaEntity contaEntity) {
        ContaEntity conta = null;
        try {
            contaEntity.setDataCadastro(LocalDate.now());
            contaEntity.setStatus(true);
            validarConta(contaEntity);
            conta = contaRepository.save(contaEntity);
        } catch (DataIntegrityViolationException exception) {
           throw new ErroException("conta ja eiste");
        }catch (Exception exception) {
           throw new ErroException(exception.getMessage());
        }
        return contaMapper.entityToDTO(conta);
    }

    private void validarConta(ContaEntity conta) {
        if (conta.getContaCorrente().length() < 6){
            throw new ErroException("Conta de ter 6 digitos");
        }
    }

    @Transactional
    @Override
    public ContaDTO update(ContaEntity contaEntity) {
        ContaEntity conta = contaRepository.save(contaEntity);
        return contaMapper.entityToDTO(conta);
    }


    @Override
    public void deletarLista(List<Long> body) {
        body.forEach(this::deleteById);
    }
    @Override
    public void deleteById(Long codigo) {
        try {
            contaRepository.deleteById(codigo);
        } catch (EmptyResultDataAccessException e){
            return;
        }
    }

    @Override
    public Page<ContaDTO> search(String nome, PageRequest pageRequest) {
        Page<ContaEntity> search = contaRepository.search(nome, pageRequest);
        Page<ContaDTO> contaDTOS = search.map(entity -> {
            ContaDTO dto = contaMapper.entityToDTO(entity);
            return dto;
        });
        return contaDTOS;
    }

    @Override
    public Page<ContaDTO> searchAll(PageRequest pageRequest) {

        Page<ContaEntity> contaEntities = contaRepository.searchAll(pageRequest);

        Page<ContaDTO> contaDTOS = contaEntities.map(entity -> {
            ContaDTO dto = contaMapper.entityToDTO(entity);
            return dto;
        });

        return contaDTOS;
    }

    @Override
    public List<String> findNomeLike(String nome) {
        List<String> nomeLike = new ArrayList<>();
        List<ContaEntity> list = contaRepository.findNomeLike(nome);
        if (Objects.nonNull(list) && !list.isEmpty()){
            list.stream().forEach( contaEntity -> {
                nomeLike.add(contaEntity.getNome());
            });
        }
        return nomeLike;
    }

    public ContaEntity findByContaCorrente(String contaCorrente) {
         return contaRepository.findByContaCorrente(contaCorrente);
    }
}
