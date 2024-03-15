package br.com.efb.domain.api.impl;

import br.com.efb.domain.mapper.TransacaoMapper;
import br.com.efb.domain.service.TransacaoService;
import com.efb.api.TransacaoApi;
import com.efb.api.model.TransacaoCadastroDTO;
import com.efb.api.model.TransacaoDTO;
import com.efb.api.model.TransacaoPageDTO;
import com.efb.api.model.TransacaoSearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransacaoController implements TransacaoApi {

    @Autowired
    private TransacaoService transacaoService;

    @Autowired
    TransacaoMapper transacaoMapper;

    @Override
    public ResponseEntity<TransacaoDTO> cadastrarTransacao(TransacaoCadastroDTO body) {

        return ResponseEntity.ok(transacaoService.cadastrarTransacao(body));
    }


    @Override
    public ResponseEntity<TransacaoPageDTO> listarTransacoes(TransacaoSearchDTO body) {
        TransacaoPageDTO transacaoPageDTO = new TransacaoPageDTO();

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
                "dataAgendamento","dataProgramada", "idTransacao");

        Page<TransacaoDTO> search;
        if (body.getContaOrigem() != null) {
            search = transacaoService.searchOrigem(
                    body.getContaOrigem().getContaCorrente().toLowerCase(),
                    pageRequest);
        } else if (body.getContaDestino() != null) {
            search = transacaoService.searchDestino(
                    body.getContaDestino().getContaCorrente().toLowerCase(),
                    pageRequest);
        } else if (body.getTipoTransferencia() != null) {
            search = transacaoService.searchByIdTipoTransferencia(
                    body.getTipoTransferencia().getIdTipoTransferencia(),
                    pageRequest);
        } else {
            search = transacaoService.searchAll(
                    pageRequest);
        }
        transacaoPageDTO.setContent(search.getContent());
        transacaoPageDTO.setHasNext(search.hasNext());
        transacaoPageDTO.setHasPrev(search.hasPrevious());
        transacaoPageDTO.setPage(search.getPageable().getPageNumber());
        transacaoPageDTO.setPerPage(search.getPageable().getPageSize());
        transacaoPageDTO.setTotalElements(Math.toIntExact(search.getTotalElements()));
        transacaoPageDTO.setTotalPages(search.getTotalPages());
        return ResponseEntity.ok(transacaoPageDTO);
    }

    @Override
    public ResponseEntity<TransacaoDTO> buscarTransacao(Long codigo) {
        TransacaoDTO transacao = transacaoService.getTransacao(codigo);
        return ResponseEntity.ok(transacao);
    }


    public ResponseEntity<Void> deletarTransacao(Long codigo) {
        transacaoService.deleteById(codigo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deletarListaTransacao(List<Long> body) {
        transacaoService.deletarListaTransacao(body);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
