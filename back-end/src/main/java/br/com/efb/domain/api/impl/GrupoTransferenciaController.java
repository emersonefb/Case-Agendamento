package br.com.efb.domain.api.impl;

import br.com.efb.domain.entity.GrupoTransferenciaEntity;
import br.com.efb.domain.entity.TipoTransferenciaEntity;
import br.com.efb.domain.mapper.GrupoTransferenciaMapper;
import br.com.efb.domain.service.GrupoTransferenciaService;
import br.com.efb.domain.service.TipoTransferenciaService;
import com.efb.api.GrupoTransferenciaApi;
import com.efb.api.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
public class GrupoTransferenciaController implements GrupoTransferenciaApi {

    @Autowired
    private GrupoTransferenciaService grupoTransferenciaService;

    @Autowired
    GrupoTransferenciaMapper grupoTransferenciaMapper;

    @Transactional
    @Override
    public ResponseEntity<GrupoTransferenciaDTO> cadastrarGrupoTransferencia(GrupoTransferenciaCadastroDTO body) {
        GrupoTransferenciaEntity grupoTransferenciaEntity = grupoTransferenciaMapper.dtoToEntityCadastro(body);
        return ResponseEntity.ok(grupoTransferenciaService.save(grupoTransferenciaEntity));
    }


    @Transactional
    @Override
    public ResponseEntity<GrupoTransferenciaPageDTO> listarGrupoTransferencia(GrupoTransferenciaSearchDTO body) {
             GrupoTransferenciaPageDTO grupoTransferenciaPageDTO = new GrupoTransferenciaPageDTO();

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
                "nome", "idGrupoTransferencia");

        Page<GrupoTransferenciaResultDTO> search;
        if (body.getNome() != null) {
            search = grupoTransferenciaService.search(
                    body.getNome().toLowerCase(),
                    pageRequest);
        } else {
            search = grupoTransferenciaService.searchAll(
                    pageRequest);
        }
        grupoTransferenciaPageDTO.setContent(search.getContent());
        grupoTransferenciaPageDTO.setHasNext(search.hasNext());
        grupoTransferenciaPageDTO.setHasPrev(search.hasPrevious());
        grupoTransferenciaPageDTO.setPage(search.getPageable().getPageNumber());
        grupoTransferenciaPageDTO.setPerPage(search.getPageable().getPageSize());
        grupoTransferenciaPageDTO.setTotalElements(Math.toIntExact(search.getTotalElements()));
        grupoTransferenciaPageDTO.setTotalPages(search.getTotalPages());
        return ResponseEntity.ok(grupoTransferenciaPageDTO);
    }

    @Transactional
    @Override
    public ResponseEntity<GrupoTransferenciaDTO> buscarGrupoTransferencia(Long codigo) {
        GrupoTransferenciaDTO grupoTransferencia = grupoTransferenciaService.getGrupoTransferencia(codigo);
        return ResponseEntity.ok(grupoTransferencia);
    }


    @Transactional
    @Override
    public ResponseEntity<Void> deletarGrupoTransferencia(Long codigo) {
        grupoTransferenciaService.deleteById(codigo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<GrupoTransferenciaDTO> atualizarGrupoTransferencia (GrupoTransferenciaCadastroDTO body){
        GrupoTransferenciaEntity grupoTransferenciaEntity = grupoTransferenciaMapper.dtoToEntityCadastro(body);
        return ResponseEntity.ok(grupoTransferenciaService.update(grupoTransferenciaEntity));
    }

    @Override
    public ResponseEntity<Void> deletarListaGrupoTransferencia(List<Long> body) {
        grupoTransferenciaService.deletarLista(body);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
