package br.com.efb.domain.api.impl;

import br.com.efb.domain.entity.TipoTransferenciaEntity;
import br.com.efb.domain.mapper.TipoTransferenciaMapper;
import br.com.efb.domain.service.TipoTransferenciaService;
import com.efb.api.TipoTransferenciaApi;
import com.efb.api.model.TipoTransferenciaDTO;
import com.efb.api.model.TipoTransferenciaPageDTO;
import com.efb.api.model.TipoTransferenciaSearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TipoTransferenciaController implements TipoTransferenciaApi {

    @Autowired
    private TipoTransferenciaService tipoTransferenciaService;

    @Autowired
    TipoTransferenciaMapper tipoTransferenciaMapper;

    @Override
    public ResponseEntity<TipoTransferenciaDTO> cadastrarTipoTransferencia(TipoTransferenciaDTO body) {

        TipoTransferenciaEntity tipoTransferenciaEntity = tipoTransferenciaMapper.dtoToEntity(body);
        return ResponseEntity.ok(tipoTransferenciaService.save(tipoTransferenciaEntity));
    }


    @Override
    public ResponseEntity<TipoTransferenciaPageDTO> listarTipostransferencia(TipoTransferenciaSearchDTO body) {
        TipoTransferenciaPageDTO tipoTransferenciaPageDTO = new TipoTransferenciaPageDTO();

        int page = 0;
        if (body.getPage() != 0) {
            page = body.getPage();
        }

        int size = body.getSize();
        if (body.getSize() != 0) {
            size = body.getSize();
        }
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                "nome", "idTipoTransferencia");

        Page<TipoTransferenciaDTO> search;
        if (body.getNome() != null) {
            search = tipoTransferenciaService.search(
                    body.getNome().toLowerCase(),
                    pageRequest);
        } else if (body.getDescricao() != null) {
            search = tipoTransferenciaService.search(
                    body.getDescricao().toLowerCase(),
                    pageRequest);
        } else {
            search = tipoTransferenciaService.searchAll(
                    pageRequest);
        }
        tipoTransferenciaPageDTO.setContent(search.getContent());
        tipoTransferenciaPageDTO.setHasNext(search.hasNext());
        tipoTransferenciaPageDTO.setHasPrev(search.hasPrevious());
        tipoTransferenciaPageDTO.setPage(search.getPageable().getPageNumber());
        tipoTransferenciaPageDTO.setPerPage(search.getPageable().getPageSize());
        tipoTransferenciaPageDTO.setTotalElements(Math.toIntExact(search.getTotalElements()));
        tipoTransferenciaPageDTO.setTotalPages(search.getTotalPages());
        return ResponseEntity.ok(tipoTransferenciaPageDTO);
    }

    @Override
    public ResponseEntity<TipoTransferenciaDTO> buscarTipoTransferencia(Long codigo) {
        TipoTransferenciaDTO tipoTransferencia = tipoTransferenciaService.getTipoTransferencia(codigo);
        return ResponseEntity.ok(tipoTransferencia);
    }


    public ResponseEntity<Void> deletarTipoTransferencia(Long codigo) {
        tipoTransferenciaService.deleteById(codigo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TipoTransferenciaDTO> atualizarTipoTransferencia (TipoTransferenciaDTO body){
        TipoTransferenciaEntity tipoTransferenciaEntity = tipoTransferenciaMapper.dtoToEntity(body);
        return ResponseEntity.ok(tipoTransferenciaService.update(tipoTransferenciaEntity));
    }

    @Override
    public ResponseEntity<Void> deletarListaTipoTransferencia(List<Long> body) {
        tipoTransferenciaService.deletarLista(body);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    public boolean validarRegra(TipoTransferenciaDTO body){
        TipoTransferenciaEntity tipoTransferenciaEntity = tipoTransferenciaMapper.dtoToEntity(body);
        return  tipoTransferenciaService.validarRegra(tipoTransferenciaEntity);
    }
}
