package br.com.efb.domain.api.impl;

import br.com.efb.domain.excepition.ErroException;
import br.com.efb.domain.service.TipoTransferenciaService;
import com.efb.api.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest
class TipoTransferenciaControllerTest {

    @Autowired
    private TipoTransferenciaController tipoTransferenciaController;

    @Autowired
    private GrupoTransferenciaController grupoTransferenciaController;

    @Autowired
    private TransacaoController transacaoController;


    @Test
    void cadastrarTipoTransferencia() {
        GrupoTransferenciaDTO grupoTransferenciaD = criarGrupoTransferencia("Teste Grupo D", "Teste Grupo D");

        TipoTransferenciaDTO tipoTransferenciaDA = getTipoTransferenciaDTO("DA", "Valores até $1.000 seguem a taxação tipo A", new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(1000.00), new BigDecimal(3), new BigDecimal(3.00), grupoTransferenciaD);

        ResponseEntity<TipoTransferenciaDTO> tipoTransferenciaDTOResponseEntity = tipoTransferenciaController.cadastrarTipoTransferencia(tipoTransferenciaDA);

        TipoTransferenciaDTO tipoTransferenciaDB = getTipoTransferenciaDTO("DB", "Valores de $1.001 até $2.000 seguem a taxação tipo B", new BigDecimal(1), new BigDecimal(10), new BigDecimal(1000.01), new BigDecimal(2000.00), new BigDecimal(12), new BigDecimal(0), grupoTransferenciaD);

        ResponseEntity<TipoTransferenciaDTO> tipoDBResponseEntity = tipoTransferenciaController.cadastrarTipoTransferencia(tipoTransferenciaDB);

        TipoTransferenciaDTO tipoTransferenciaDC10 = getTipoTransferenciaDTO("DC10", "Valores maiores que $2.000 seguem a taxação tipo C, acima de 10 dias da data de agendamento 8.2%", new BigDecimal(11), new BigDecimal(19), new BigDecimal(2000.01), new BigDecimal(0), new BigDecimal(0), new BigDecimal(8.20), grupoTransferenciaD);

        ResponseEntity<TipoTransferenciaDTO> tipoDC10ResponseEntity = tipoTransferenciaController.cadastrarTipoTransferencia(tipoTransferenciaDC10);

        TipoTransferenciaDTO tipoTransferenciaDC20 = getTipoTransferenciaDTO("DC20", "Valores maiores que $2.000 seguem a taxação tipo C,acima de 20 dias da data de agendamento 6.9%", new BigDecimal(20), new BigDecimal(29), new BigDecimal(2000.01), new BigDecimal(0), new BigDecimal(0), new BigDecimal(6.90), grupoTransferenciaD);

        ResponseEntity<TipoTransferenciaDTO> tipoDC20ResponseEntity = tipoTransferenciaController.cadastrarTipoTransferencia(tipoTransferenciaDC20);

        TipoTransferenciaDTO tipoTransferenciaDC30 = getTipoTransferenciaDTO("DC30", "Valores maiores que $2.000 seguem a taxação tipo C,acima de 30 dias da data de agendamento 4.7%", new BigDecimal(30), new BigDecimal(39), new BigDecimal(2000.01), new BigDecimal(0), new BigDecimal(0), new BigDecimal(4.70), grupoTransferenciaD);

        ResponseEntity<TipoTransferenciaDTO> tipoDC30ResponseEntity = tipoTransferenciaController.cadastrarTipoTransferencia(tipoTransferenciaDC30);

        TipoTransferenciaDTO tipoTransferenciaDC40 = getTipoTransferenciaDTO("DC40", "Valores maiores que $2.000 seguem a taxação tipo C,acima de 40 dias da data de agendamento 1.7%", new BigDecimal(40), new BigDecimal(0), new BigDecimal(2000.01), new BigDecimal(0), new BigDecimal(0), new BigDecimal(1.70), grupoTransferenciaD);

        ResponseEntity<TipoTransferenciaDTO> tipoDC40ResponseEntity = tipoTransferenciaController.cadastrarTipoTransferencia(tipoTransferenciaDC40);

        GrupoTransferenciaDTO grupoTransferenciaB = criarGrupoTransferencia("Teste Grupo B", "Teste Grupo B");

        TipoTransferenciaDTO tipoTransferenciaB = getTipoTransferenciaDTO("B", "Transferência até 10 dias da data de agendamento", new BigDecimal(1), new BigDecimal(10), new BigDecimal(0), new BigDecimal(0), new BigDecimal(12), new BigDecimal(0), grupoTransferenciaB);

        ResponseEntity<TipoTransferenciaDTO> tipoBResponseEntity = tipoTransferenciaController.cadastrarTipoTransferencia(tipoTransferenciaB);
        Assertions.assertNotNull(tipoBResponseEntity);
        TipoTransferenciaDTO bResponseEntityBody = tipoBResponseEntity.getBody();
        Assertions.assertNotNull(bResponseEntityBody.getIdTipoTransferencia());
        Assertions.assertEquals("B", bResponseEntityBody.getNome());
        Assertions.assertEquals("Teste Grupo B", bResponseEntityBody.getGrupoTransferencia().getNome());

        GrupoTransferenciaDTO grupoTransferenciaA = criarGrupoTransferencia("Teste Grupo A", "Teste Grupo A");

        Assertions.assertNotNull(grupoTransferenciaD);
        Assertions.assertNotNull(grupoTransferenciaD.getIdGrupoTransferencia());
        Assertions.assertEquals("Teste Grupo D", grupoTransferenciaD.getNome());

        Assertions.assertNotNull(grupoTransferenciaB);
        Assertions.assertNotNull(grupoTransferenciaB.getIdGrupoTransferencia());
        Assertions.assertEquals("Teste Grupo B", grupoTransferenciaB.getNome());

        Assertions.assertNotNull(grupoTransferenciaA);
        Assertions.assertNotNull(grupoTransferenciaA.getIdGrupoTransferencia());
        Assertions.assertEquals("Teste Grupo A", grupoTransferenciaA.getNome());
    }

    @Test
    void listarTipostransferencia() {

        GrupoTransferenciaDTO grupoTransferenciaB = criarGrupoTransferencia("Teste Grupo B", "Teste Grupo B");

        TipoTransferenciaDTO tipoTransferenciaB = getTipoTransferenciaDTO("B", "Transferência até 10 dias da data de agendamento", new BigDecimal(1), new BigDecimal(10), new BigDecimal(0), new BigDecimal(0), new BigDecimal(12), new BigDecimal(0), grupoTransferenciaB);

        ResponseEntity<TipoTransferenciaDTO> cadastro = tipoTransferenciaController.cadastrarTipoTransferencia(tipoTransferenciaB);


        TipoTransferenciaSearchDTO tipoTransferenciaSearchDTO = new TipoTransferenciaSearchDTO();
        tipoTransferenciaSearchDTO.setPage(0);
        tipoTransferenciaSearchDTO.setSize(10);
        tipoTransferenciaSearchDTO.setNome(cadastro.getBody().getNome());

        ResponseEntity<TipoTransferenciaPageDTO> tipoTransferenciaPageDTOResponseEntity = tipoTransferenciaController.listarTipostransferencia(tipoTransferenciaSearchDTO);

        Assertions.assertNotNull(tipoTransferenciaPageDTOResponseEntity);
        Assertions.assertNotNull(Objects.requireNonNull(tipoTransferenciaPageDTOResponseEntity.getBody()).getContent());
        Assertions.assertEquals("B", Objects.requireNonNull(tipoTransferenciaPageDTOResponseEntity.getBody()).getContent().get(0).getNome());

    }

    @Test
    void buscarTipoTransferencia() {
        GrupoTransferenciaDTO grupoTransferenciaB = criarGrupoTransferencia("Teste Grupo B", "Teste Grupo B");

        TipoTransferenciaDTO tipoTransferenciaB = getTipoTransferenciaDTO("B", "Transferência até 10 dias da data de agendamento", new BigDecimal(1), new BigDecimal(10), new BigDecimal(0), new BigDecimal(0), new BigDecimal(12), new BigDecimal(0), grupoTransferenciaB);

        ResponseEntity<TipoTransferenciaDTO> cadastro = tipoTransferenciaController.cadastrarTipoTransferencia(tipoTransferenciaB);

        ResponseEntity<TipoTransferenciaDTO> response = tipoTransferenciaController.buscarTipoTransferencia(cadastro.getBody().getIdTipoTransferencia());

        TipoTransferenciaDTO responseBody = response.getBody();
        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getIdTipoTransferencia());
        Assertions.assertEquals("B", responseBody.getNome());
        Assertions.assertEquals("Teste Grupo B", responseBody.getGrupoTransferencia().getNome());


    }

    @Test
    void deletarTipoTransferencia() {
        GrupoTransferenciaDTO grupoTransferenciaB = criarGrupoTransferencia("Teste Grupo B", "Teste Grupo B");

        TipoTransferenciaDTO tipoTransferenciaB = getTipoTransferenciaDTO("B", "Transferência até 10 dias da data de agendamento", new BigDecimal(1), new BigDecimal(10), new BigDecimal(0), new BigDecimal(0), new BigDecimal(12), new BigDecimal(0), grupoTransferenciaB);

        ResponseEntity<TipoTransferenciaDTO> tipoBResponseEntity = tipoTransferenciaController.cadastrarTipoTransferencia(tipoTransferenciaB);

        Long idTipoTransferencia = tipoBResponseEntity.getBody().getIdTipoTransferencia();
        tipoTransferenciaController.deletarTipoTransferencia(idTipoTransferencia);
        ResponseEntity<TipoTransferenciaDTO> response = tipoTransferenciaController.buscarTipoTransferencia(idTipoTransferencia);

        Assertions.assertNull(Objects.requireNonNull(response.getBody()).getIdTipoTransferencia());;

    }

    @Test
    void atualizarTipoTransferencia() {

        GrupoTransferenciaDTO grupoTransferenciaB = criarGrupoTransferencia("Teste Grupo B atualizar", "Teste Grupo B");

        TipoTransferenciaDTO tipoTransferenciaB = getTipoTransferenciaDTO("B", "Transferência até 10 dias da data de agendamento", new BigDecimal(1), new BigDecimal(10), new BigDecimal(0), new BigDecimal(0), new BigDecimal(12), new BigDecimal(0), grupoTransferenciaB);

        ResponseEntity<TipoTransferenciaDTO> cadastro = tipoTransferenciaController.cadastrarTipoTransferencia(tipoTransferenciaB);

//        TipoTransferenciaDTO tipoTransferenciaBAtualizar = getTipoTransferenciaDTO("Tipo B", "Transferência até 10 dias da data de agendamento", new BigDecimal(10), new BigDecimal(11), new BigDecimal(0), new BigDecimal(0), new BigDecimal(12), new BigDecimal(0), grupoTransferenciaB);
        TipoTransferenciaDTO tipoTransferenciaBAtualizar = cadastro.getBody();
        tipoTransferenciaBAtualizar.setNome("B");

        ResponseEntity<TipoTransferenciaDTO> tipoBResponseEntity = tipoTransferenciaController.atualizarTipoTransferencia(tipoTransferenciaBAtualizar);

        Assertions.assertNotNull(tipoBResponseEntity);
        TipoTransferenciaDTO bResponseEntityBody = tipoBResponseEntity.getBody();
        Assertions.assertNotNull(bResponseEntityBody.getIdTipoTransferencia());
        Assertions.assertEquals("B", bResponseEntityBody.getNome());
        Assertions.assertEquals("Teste Grupo B", bResponseEntityBody.getGrupoTransferencia().getNome());

    }

    @Test
    void deletarListaTipoTransferencia() {

        List<Long> longs = new ArrayList<>();

        GrupoTransferenciaDTO grupoTransferenciaB = criarGrupoTransferencia("Teste Grupo B", "Teste Grupo B");

        TipoTransferenciaDTO tipoTransferenciaB = getTipoTransferenciaDTO("B", "Transferência até 10 dias da data de agendamento", new BigDecimal(1), new BigDecimal(10), new BigDecimal(0), new BigDecimal(0), new BigDecimal(12), new BigDecimal(0), grupoTransferenciaB);

        ResponseEntity<TipoTransferenciaDTO> cadastro = tipoTransferenciaController.cadastrarTipoTransferencia(tipoTransferenciaB);

        Long idConta1 = Objects.requireNonNull(cadastro.getBody()).getIdTipoTransferencia();

        longs.add(idConta1);

        TipoTransferenciaSearchDTO tipoTransferenciaSearchDTO = new TipoTransferenciaSearchDTO();
        tipoTransferenciaSearchDTO.setPage(0);
        tipoTransferenciaSearchDTO.setSize(10);
        tipoTransferenciaSearchDTO.setNome(cadastro.getBody().getNome());
        ResponseEntity<TipoTransferenciaPageDTO> listarTipostransferencia = tipoTransferenciaController.listarTipostransferencia(tipoTransferenciaSearchDTO);

        TransacaoSearchDTO transacaoSearchDTO = new TransacaoSearchDTO();
        transacaoSearchDTO.setPage(0);
        transacaoSearchDTO.setSize(10);
        List<TipoTransferenciaDTO> content = listarTipostransferencia.getBody().getContent();
        if (!content.isEmpty()){
            transacaoSearchDTO.setTipoTransferencia(content.get(0));
        }
        ResponseEntity<TransacaoPageDTO> transacaoPageDTOResponseEntity = transacaoController.listarTransacoes(transacaoSearchDTO);

        if (!transacaoPageDTOResponseEntity.getBody().getContent().isEmpty()){
            longs.remove(transacaoPageDTOResponseEntity.getBody().getContent().get(0).getTipoTransferencia().getIdTipoTransferencia());
        }

        GrupoTransferenciaDTO grupoTransferenciaD = criarGrupoTransferencia("Teste Grupo D", "Teste Grupo D");

        TipoTransferenciaDTO tipoTransferenciaDA = getTipoTransferenciaDTO("DA", "Valores até $1.000 seguem a taxação tipo A", new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(1000.00), new BigDecimal(3), new BigDecimal(3.00), grupoTransferenciaD);

        ResponseEntity<TipoTransferenciaDTO> tipoTransferenciaDTOResponseEntity = tipoTransferenciaController.cadastrarTipoTransferencia(tipoTransferenciaDA);
        Long idConta2 = Objects.requireNonNull(tipoTransferenciaDTOResponseEntity.getBody()).getIdTipoTransferencia();
        longs.add(idConta2);


        tipoTransferenciaSearchDTO = new TipoTransferenciaSearchDTO();
        tipoTransferenciaSearchDTO.setPage(0);
        tipoTransferenciaSearchDTO.setSize(10);
        tipoTransferenciaSearchDTO.setNome(tipoTransferenciaDTOResponseEntity.getBody().getNome());
        listarTipostransferencia = tipoTransferenciaController.listarTipostransferencia(tipoTransferenciaSearchDTO);

        transacaoSearchDTO = new TransacaoSearchDTO();
        transacaoSearchDTO.setPage(0);
        transacaoSearchDTO.setSize(10);
        content = listarTipostransferencia.getBody().getContent();
        if (!content.isEmpty()){
            transacaoSearchDTO.setTipoTransferencia(content.get(0));
        }
        transacaoPageDTOResponseEntity = transacaoController.listarTransacoes(transacaoSearchDTO);

        if (!transacaoPageDTOResponseEntity.getBody().getContent().isEmpty()){
            longs.remove(transacaoPageDTOResponseEntity.getBody().getContent().get(0).getTipoTransferencia().getIdTipoTransferencia());
        }

        tipoTransferenciaController.deletarListaTipoTransferencia(longs);

        longs.stream().forEach( id ->{
            ResponseEntity<TipoTransferenciaDTO> response = tipoTransferenciaController.buscarTipoTransferencia(id);
            Assertions.assertNull(Objects.requireNonNull(response.getBody()).getIdTipoTransferencia());
        });


    }

    private TipoTransferenciaDTO getTipoTransferenciaDTO(String nome, String descricao, BigDecimal minDias, BigDecimal maxDias, BigDecimal minValor, BigDecimal maxValor, BigDecimal taxaFixa, BigDecimal taxaVariavel, GrupoTransferenciaDTO grupoTransferencia) {
        TipoTransferenciaDTO tipoTransferenciaDTO = new TipoTransferenciaDTO();
        tipoTransferenciaDTO.setNome(nome);
        tipoTransferenciaDTO.setDescricao(descricao);
        tipoTransferenciaDTO.setMinDias(minDias);
        tipoTransferenciaDTO.setMaxDias(maxDias);
        tipoTransferenciaDTO.setMinValor(minValor);
        tipoTransferenciaDTO.setMaxValor(maxValor);
        tipoTransferenciaDTO.setTaxaFixa(taxaFixa);
        tipoTransferenciaDTO.setTaxaVariavel(taxaVariavel);
        tipoTransferenciaDTO.setGrupoTransferencia(grupoTransferencia);
        tipoTransferenciaDTO.setStatus(true);
        try {
            boolean validarRegra = tipoTransferenciaController.validarRegra(tipoTransferenciaDTO);
        } catch (ErroException erroException){
            TipoTransferenciaSearchDTO tipoTransferenciaSearchDTO = new TipoTransferenciaSearchDTO();
            tipoTransferenciaSearchDTO.setPage(0);
            tipoTransferenciaSearchDTO.setSize(10);
            tipoTransferenciaSearchDTO.setNome(tipoTransferenciaDTO.getNome());
            ResponseEntity<TipoTransferenciaPageDTO> tipoTransferenciaPageDTOResponseEntity = tipoTransferenciaController.listarTipostransferencia(tipoTransferenciaSearchDTO);
            List<TipoTransferenciaDTO> content = tipoTransferenciaPageDTOResponseEntity.getBody().getContent();
            if (!content.isEmpty()){
                return content.get(0);
            }
        }

        return tipoTransferenciaDTO;
    }

    private GrupoTransferenciaDTO criarGrupoTransferencia(String nome, String descricao) {
        GrupoTransferenciaCadastroDTO grupoTransferenciaCadastroDTO = new GrupoTransferenciaCadastroDTO();
        grupoTransferenciaCadastroDTO.setNome(nome);
        grupoTransferenciaCadastroDTO.setDescricao(descricao);

        GrupoTransferenciaSearchDTO grupoTransferenciaSearchDTO = new GrupoTransferenciaSearchDTO();
        grupoTransferenciaSearchDTO.setPage(0);
        grupoTransferenciaSearchDTO.setSize(10);
        grupoTransferenciaSearchDTO.setNome(grupoTransferenciaCadastroDTO.getNome());
        ResponseEntity<GrupoTransferenciaPageDTO> grupoTransferenciaPageDTOResponseEntity = grupoTransferenciaController.listarGrupoTransferencia(grupoTransferenciaSearchDTO);

        if (grupoTransferenciaPageDTOResponseEntity.getBody().getTotalElements() > 0){
            GrupoTransferenciaResultDTO grupoTransferenciaResultDTO = grupoTransferenciaPageDTOResponseEntity.getBody().getContent().get(0);
            ResponseEntity<GrupoTransferenciaDTO> grupoTransferencia = grupoTransferenciaController.buscarGrupoTransferencia(grupoTransferenciaResultDTO.getIdGrupoTransferencia());
            return grupoTransferencia.getBody();
        }
        ResponseEntity<GrupoTransferenciaDTO> grupoTransferencia = grupoTransferenciaController.cadastrarGrupoTransferencia(grupoTransferenciaCadastroDTO);

        GrupoTransferenciaDTO grupoTransferenciaDTO = grupoTransferencia.getBody();
        return grupoTransferenciaDTO;
    }
}