package br.com.efb.domain.service;

import br.com.efb.domain.entity.TipoTransferenciaEntity;
import com.efb.api.model.TipoTransferenciaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

public interface TipoTransferenciaService {

    TipoTransferenciaDTO getTipoTransferencia(Long codigo);

    List<TipoTransferenciaDTO> findAll();

    TipoTransferenciaDTO save(TipoTransferenciaEntity tipoTransferenciaEntity);

    TipoTransferenciaDTO update(TipoTransferenciaEntity tipoTransferenciaEntity);

    void deleteById(Long codigo);

    public Page<TipoTransferenciaDTO> search(String nome, PageRequest pageRequest);
    public Page<TipoTransferenciaDTO> searchAll(PageRequest pageRequest);

    List<String> findNomeLike(String nome);

    public boolean validarRegra(TipoTransferenciaEntity tipoTransferenciaEntity);
    public TipoTransferenciaEntity getTipoTransferenciaEntityAtivo(Long dias, BigDecimal valor);

    void deletarLista(List<Long> body);

}
