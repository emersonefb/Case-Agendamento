package br.com.efb.domain.service;

import br.com.efb.domain.entity.TransacaoEntity;
import com.efb.api.model.TransacaoCadastroDTO;
import com.efb.api.model.TransacaoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface TransacaoService {

    TransacaoDTO getTransacao(Long codigo);

    TransacaoDTO save(TransacaoEntity TransacaoEntity);

    TransacaoDTO update(TransacaoEntity TransacaoEntity);

    void deleteById(Long codigo);

    TransacaoDTO cadastrarTransacao(TransacaoCadastroDTO body);
    public Page<TransacaoDTO> searchAll(PageRequest pageRequest);
    public Page<TransacaoDTO> searchByIdTipoTransferencia(Long idTipoTransferencia, PageRequest pageRequest);
    Page<TransacaoDTO> searchDestino(String lowerCase, PageRequest pageRequest);
    Page<TransacaoDTO> searchOrigem(String lowerCase, PageRequest pageRequest);
    void deletarListaTransacao(List<Long> body);
}
