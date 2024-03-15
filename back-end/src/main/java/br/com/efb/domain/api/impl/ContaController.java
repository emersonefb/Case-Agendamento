package br.com.efb.domain.api.impl;

import br.com.efb.domain.entity.ContaEntity;
import br.com.efb.domain.mapper.ContaMapper;
import br.com.efb.domain.service.ContaService;
import com.efb.api.ContaApi;
import com.efb.api.model.ContaDTO;
import com.efb.api.model.ContaPageDTO;
import com.efb.api.model.ContaSearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ContaController implements ContaApi {

    @Autowired
    private ContaService contaService;

    @Autowired
    ContaMapper contaMapper;

    @Override
    public ResponseEntity<ContaDTO> cadastrarConta(ContaDTO body) {
        body.setDataCadastro(LocalDate.now().toString());
        body.setStatus(true);
        ContaEntity contaEntity = contaMapper.dtoToEntity(body);
        return ResponseEntity.ok(contaService.save(contaEntity));
    }


    public ResponseEntity<ContaPageDTO> listarContas(ContaSearchDTO body) {
        ContaPageDTO contaPageDTO = new ContaPageDTO();

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
                "nome", "idConta");

        Page<ContaDTO> search;
        if (body.getNome() != null) {
            search = contaService.search(
                    body.getNome().toLowerCase(),
                    pageRequest);
        } else {
            search = contaService.searchAll(
                    pageRequest);
        }
        contaPageDTO.setContent(search.getContent());
        contaPageDTO.setHasNext(search.hasNext());
        contaPageDTO.setHasPrev(search.hasPrevious());
        contaPageDTO.setPage(search.getPageable().getPageNumber());
        contaPageDTO.setPerPage(search.getPageable().getPageSize());
        contaPageDTO.setTotalElements(Math.toIntExact(search.getTotalElements()));
        contaPageDTO.setTotalPages(search.getTotalPages());
        return ResponseEntity.ok(contaPageDTO);
    }

    @Override
    public ResponseEntity<ContaDTO> buscarConta(Long codigo) {
        ContaDTO conta = contaService.getConta(codigo);
        return ResponseEntity.ok(conta);
    }

    @Override
    public ResponseEntity<Void> deletarConta(Long codigo) {
        contaService.deleteById(codigo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ContaDTO> atualizarConta (ContaDTO body){
        ContaEntity contaEntity = contaMapper.dtoToEntity(body);
        return ResponseEntity.ok(contaService.update(contaEntity));
    }

    @Override
    public ResponseEntity<Void> deletarListaContas(List<Long> body) {
        contaService.deletarLista(body);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
