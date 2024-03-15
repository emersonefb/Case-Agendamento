package br.com.efb.domain.service;

import br.com.efb.domain.entity.GrupoTransferenciaEntity;
import com.efb.api.model.GrupoTransferenciaDTO;
import com.efb.api.model.GrupoTransferenciaResultDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface GrupoTransferenciaService {

    GrupoTransferenciaDTO getGrupoTransferencia(Long codigo);

    GrupoTransferenciaDTO findById(Long id);

    List<GrupoTransferenciaDTO> findAll();

    GrupoTransferenciaDTO save(GrupoTransferenciaEntity grupoTransferenciaEntity);

    GrupoTransferenciaDTO update(GrupoTransferenciaEntity grupoTransferenciaEntity);

    void deleteById(Long codigo);

    public Page<GrupoTransferenciaResultDTO> search(String nome, PageRequest pageRequest);
    public Page<GrupoTransferenciaResultDTO> searchAll(PageRequest pageRequest);

    List<String> findNomeLike(String nome);

    void deletarLista(List<Long> body);
}
