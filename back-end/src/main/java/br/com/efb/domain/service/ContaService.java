package br.com.efb.domain.service;

import br.com.efb.domain.entity.ContaEntity;
import com.efb.api.model.ContaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ContaService {

    ContaDTO getConta(Long codigo);

    List<ContaDTO> findAll();

    ContaDTO save(ContaEntity contaEntity) ;

    ContaDTO update(ContaEntity contaEntity);

    void deleteById(Long codigo);

    public Page<ContaDTO> search(String nome, PageRequest pageRequest);
    public Page<ContaDTO> searchAll(PageRequest pageRequest);

    List<String> findNomeLike(String nome);

    void deletarLista(List<Long> body);
}
