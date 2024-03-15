package br.com.efb.domain.api.impl;

import br.com.efb.domain.excepition.ErroException;
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
class GrupoTransferenciaControllerTest {


    @Autowired
    private GrupoTransferenciaController grupoTransferenciaController;

    @Test
    void cadastrarGrupoTransferencia() {

        GrupoTransferenciaCadastroDTO grupoD = getGrupoTransferenciaCadastroDTO("Teste Grupo D", "Teste Grupo D");

        List<TipoTransferenciaDTO> tipoD = new ArrayList<>();

        TipoTransferenciaDTO tipoTransferenciaDA = getTipoTransferenciaDTO("DA", "Valores até $1.000 seguem a taxação tipo A", new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(1000.00), new BigDecimal(3), new BigDecimal(3.00));

        tipoD.add(tipoTransferenciaDA);

        TipoTransferenciaDTO tipoTransferenciaDB = getTipoTransferenciaDTO("DB", "Valores de $1.001 até $2.000 seguem a taxação tipo B", new BigDecimal(1), new BigDecimal(10), new BigDecimal(1000.01), new BigDecimal(2000.00), new BigDecimal(12), new BigDecimal(0));

        tipoD.add(tipoTransferenciaDB);

        TipoTransferenciaDTO tipoTransferenciaDC10 = getTipoTransferenciaDTO("DC10", "Valores maiores que $2.000 seguem a taxação tipo C, acima de 10 dias da data de agendamento 8.2%", new BigDecimal(11), new BigDecimal(19), new BigDecimal(2000.01), new BigDecimal(0), new BigDecimal(0), new BigDecimal(8.20));

        tipoD.add(tipoTransferenciaDC10);

        TipoTransferenciaDTO tipoTransferenciaDC20 = getTipoTransferenciaDTO("DC20", "Valores maiores que $2.000 seguem a taxação tipo C,acima de 20 dias da data de agendamento 6.9%", new BigDecimal(20), new BigDecimal(29), new BigDecimal(2000.01), new BigDecimal(0), new BigDecimal(0), new BigDecimal(6.90));

        tipoD.add(tipoTransferenciaDC20);

        TipoTransferenciaDTO tipoTransferenciaDC30 = getTipoTransferenciaDTO("DC30", "Valores maiores que $2.000 seguem a taxação tipo C,acima de 30 dias da data de agendamento 4.7%", new BigDecimal(30), new BigDecimal(39), new BigDecimal(2000.01), new BigDecimal(0), new BigDecimal(0), new BigDecimal(4.70));

        tipoD.add(tipoTransferenciaDC30);

        TipoTransferenciaDTO tipoTransferenciaDC40 = getTipoTransferenciaDTO("DC40", "Valores maiores que $2.000 seguem a taxação tipo C,acima de 40 dias da data de agendamento 1.7%", new BigDecimal(40), new BigDecimal(0), new BigDecimal(2000.01), new BigDecimal(0), new BigDecimal(0), new BigDecimal(1.70));

        tipoD.add(tipoTransferenciaDC30);

        grupoD.setTipoTransferenciaList(tipoD);

        ResponseEntity<GrupoTransferenciaDTO> grupoDResponseEntity = getGrupoAResponseEntity(grupoD);


        GrupoTransferenciaCadastroDTO grupoB = getGrupoTransferenciaCadastroDTO("Teste Grupo B", "Teste Grupo B");

        List<TipoTransferenciaDTO> tipoListaB = new ArrayList<>();

        TipoTransferenciaDTO tipoTransferenciaB = getTipoTransferenciaDTO("B", "Transferência até 10 dias da data de agendamento", new BigDecimal(1), new BigDecimal(10), new BigDecimal(0), new BigDecimal(0), new BigDecimal(12), new BigDecimal(0));

        tipoListaB.add(tipoTransferenciaB);

        grupoB.setTipoTransferenciaList(tipoListaB);
        ResponseEntity<GrupoTransferenciaDTO> grupoBResponseEntity = getGrupoAResponseEntity(grupoB);

        GrupoTransferenciaCadastroDTO grupoA = getGrupoTransferenciaCadastroDTO("Teste Grupo A", "Teste Grupo A");
        ResponseEntity<GrupoTransferenciaDTO> grupoAResponseEntity = getGrupoAResponseEntity(grupoA);

        GrupoTransferenciaDTO grupoTransferencia = grupoDResponseEntity.getBody();
        Assertions.assertNotNull(grupoTransferencia);
        Assertions.assertNotNull(grupoTransferencia.getIdGrupoTransferencia());
        Assertions.assertEquals("Teste Grupo D", grupoTransferencia.getNome());

        GrupoTransferenciaDTO grupoTransferenciaB = grupoBResponseEntity.getBody();
        Assertions.assertNotNull(grupoTransferenciaB);
        Assertions.assertNotNull(grupoTransferenciaB.getIdGrupoTransferencia());
        Assertions.assertEquals("Teste Grupo B", grupoTransferenciaB.getNome());

        GrupoTransferenciaDTO grupoTransferenciaA = grupoAResponseEntity.getBody();
        Assertions.assertNotNull(grupoTransferenciaA);
        Assertions.assertNotNull(grupoTransferenciaA.getIdGrupoTransferencia());
        Assertions.assertEquals("Teste Grupo A", grupoTransferenciaA.getNome());
    }

    private ResponseEntity<GrupoTransferenciaDTO> getGrupoAResponseEntity(GrupoTransferenciaCadastroDTO grupo) {

        GrupoTransferenciaSearchDTO grupoTransferenciaSearchDTO = new GrupoTransferenciaSearchDTO();
        grupoTransferenciaSearchDTO.setPage(0);
        grupoTransferenciaSearchDTO.setSize(10);
        grupoTransferenciaSearchDTO.setNome(grupo.getNome());
        ResponseEntity<GrupoTransferenciaPageDTO> responseEntity = grupoTransferenciaController.listarGrupoTransferencia(grupoTransferenciaSearchDTO);

        ResponseEntity<GrupoTransferenciaDTO> grupoTransferenciaDTO;
        if (!responseEntity.getBody().getContent().isEmpty()){
            Long idGrupoTransferencia = responseEntity.getBody().getContent().get(0).getIdGrupoTransferencia();
            grupoTransferenciaDTO = grupoTransferenciaController.buscarGrupoTransferencia(idGrupoTransferencia);
        } else {
            grupoTransferenciaDTO = grupoTransferenciaController.cadastrarGrupoTransferencia(grupo);
        }

        Assertions.assertNotNull(responseEntity);
        Assertions.assertNotNull(Objects.requireNonNull(responseEntity.getBody()).getContent());

        return grupoTransferenciaDTO;
    }

    private static GrupoTransferenciaCadastroDTO getGrupoTransferenciaCadastroDTO(String nome, String descricao) {
        GrupoTransferenciaCadastroDTO grupoA = new GrupoTransferenciaCadastroDTO();
        grupoA.setNome(nome);
        grupoA.setDescricao(descricao);
        return grupoA;
    }

    private static TipoTransferenciaDTO getTipoTransferenciaDTO(String nome, String descricao, BigDecimal minDias, BigDecimal maxDias, BigDecimal minValor, BigDecimal maxValor, BigDecimal taxaFixa, BigDecimal taxaVariavel) {
        TipoTransferenciaDTO tipoTransferenciaDA = new TipoTransferenciaDTO();
        tipoTransferenciaDA.setNome(nome);
        tipoTransferenciaDA.setDescricao(descricao);
        tipoTransferenciaDA.setMinDias(minDias);
        tipoTransferenciaDA.setMaxDias(maxDias);
        tipoTransferenciaDA.setMinValor(minValor);
        tipoTransferenciaDA.setMaxValor(maxValor);
        tipoTransferenciaDA.setTaxaFixa(taxaFixa);
        tipoTransferenciaDA.setTaxaVariavel(taxaVariavel);
        tipoTransferenciaDA.setStatus(true);
        return tipoTransferenciaDA;
    }

    @Test
    void cadastrarContaErro() {

        GrupoTransferenciaSearchDTO grupoTransferenciaSearchDTO = new GrupoTransferenciaSearchDTO();
        grupoTransferenciaSearchDTO.setPage(0);
        grupoTransferenciaSearchDTO.setSize(10);
        grupoTransferenciaSearchDTO.setNome("B");
        ResponseEntity<GrupoTransferenciaPageDTO> grupoTransferenciaPageDTOResponseEntity = grupoTransferenciaController.listarGrupoTransferencia(grupoTransferenciaSearchDTO);

        GrupoTransferenciaPageDTO entityBody = grupoTransferenciaPageDTOResponseEntity.getBody();
        if (entityBody.getTotalElements() != 0){
            GrupoTransferenciaCadastroDTO grupoB = new GrupoTransferenciaCadastroDTO();

            List<TipoTransferenciaDTO> tipoListaB = new ArrayList<>();

            TipoTransferenciaDTO tipoTransferenciaB = getTipoTransferenciaDTO("B", "Transferência até 10 dias da data de agendamento", new BigDecimal(1), new BigDecimal(10), new BigDecimal(0), new BigDecimal(0), new BigDecimal(12), new BigDecimal(0));

            tipoListaB.add(tipoTransferenciaB);

            grupoB.setNome("Teste Grupo B");
            grupoB.setDescricao("Teste Grupo B");
            grupoB.setTipoTransferenciaList(tipoListaB);
            ResponseEntity<GrupoTransferenciaDTO> grupoBResponseEntity = getGrupoAResponseEntity(grupoB);

            Assertions.assertNotNull(grupoBResponseEntity.getBody());
            Assertions.assertNotNull(grupoBResponseEntity.getBody().getIdGrupoTransferencia());

            ErroException thrown = Assertions.assertThrows(ErroException.class, () -> {
                grupoTransferenciaController.cadastrarGrupoTransferencia(grupoB);
            });
            Assertions.assertEquals("Conflito entre regras B e B no valor maximo 0 e data minima 1 verifique a tipo de transferencia: B", thrown.getMessage());
        }

    }

    @Test
    void listarGrupoTransferencia() {

        GrupoTransferenciaSearchDTO grupoTransferenciaSearchDTO = new GrupoTransferenciaSearchDTO();
        grupoTransferenciaSearchDTO.setPage(0);
        grupoTransferenciaSearchDTO.setSize(10);
        ResponseEntity<GrupoTransferenciaPageDTO> grupoTransferenciaPageDTOResponseEntity = grupoTransferenciaController.listarGrupoTransferencia(grupoTransferenciaSearchDTO);
        Assertions.assertNotNull(grupoTransferenciaPageDTOResponseEntity);
        Assertions.assertNotNull(Objects.requireNonNull(grupoTransferenciaPageDTOResponseEntity.getBody()).getContent());
    }

    @Test
    void buscarGrupoTransferencia() {
        ResponseEntity<GrupoTransferenciaDTO> grupoTransferencia = grupoTransferenciaController.buscarGrupoTransferencia(3L);

        Long idGrupoTransferencia = grupoTransferencia.getBody().getIdGrupoTransferencia();
        if (Objects.isNull(idGrupoTransferencia)){
            if (Objects.isNull(idGrupoTransferencia)){
                GrupoTransferenciaCadastroDTO grupoTransferenciaDTO = new GrupoTransferenciaCadastroDTO();
                grupoTransferenciaDTO.setNome("Grupo busca");
                grupoTransferenciaDTO.nome("grupo");
                grupoTransferenciaDTO.setDescricao("grupo");
                ResponseEntity<GrupoTransferenciaDTO> grupoTransferenciaDTOResponseEntity = getGrupoAResponseEntity(grupoTransferenciaDTO);
                idGrupoTransferencia = Objects.requireNonNull(grupoTransferenciaDTOResponseEntity.getBody()).getIdGrupoTransferencia();
                grupoTransferencia = grupoTransferenciaController.buscarGrupoTransferencia(idGrupoTransferencia);

            }
        }

        Assertions.assertNotNull(grupoTransferencia.getBody());
        Assertions.assertNotNull(grupoTransferencia.getBody().getIdGrupoTransferencia());
    }

    @Test
    void deletarGrupoTransferencia() {

        GrupoTransferenciaCadastroDTO grupoTransferenciaDTO = getGrupoTransferenciaCadastroDTO("Grupo deletarGrupoTransferencia", "grupo");
        ResponseEntity<GrupoTransferenciaDTO> grupoTransferenciaDTOResponseEntity = getGrupoAResponseEntity(grupoTransferenciaDTO);
        Long idGrupoTransferencia = Objects.requireNonNull(grupoTransferenciaDTOResponseEntity.getBody()).getIdGrupoTransferencia();

        grupoTransferenciaController.deletarGrupoTransferencia(idGrupoTransferencia);
        ResponseEntity<GrupoTransferenciaDTO> grupoTransferenciaDTOResponseEntity1 = grupoTransferenciaController.buscarGrupoTransferencia(idGrupoTransferencia);

        Assertions.assertNull(grupoTransferenciaDTOResponseEntity1.getBody().getIdGrupoTransferencia());
    }

    @Test
    void atualizarGrupoTransferencia() {

        GrupoTransferenciaCadastroDTO grupoTransferenciaDTO = getGrupoTransferenciaCadastroDTO("Grupo atualizarGrupoTransferencia", "grupo");
        ResponseEntity<GrupoTransferenciaDTO> grupoTransferenciaDTOResponseEntity = getGrupoAResponseEntity(grupoTransferenciaDTO);

        GrupoTransferenciaDTO grupoTransferenciaOriginal = grupoTransferenciaDTOResponseEntity.getBody();
        GrupoTransferenciaCadastroDTO grupoTransferenciaAtualizar = new GrupoTransferenciaCadastroDTO();

        grupoTransferenciaAtualizar.setIdGrupoTransferencia(grupoTransferenciaOriginal.getIdGrupoTransferencia());
        grupoTransferenciaAtualizar.setNome("Atualizei");
        grupoTransferenciaAtualizar.setDescricao(grupoTransferenciaOriginal.getDescricao());

        ResponseEntity<GrupoTransferenciaDTO> grupoAtualizado = grupoTransferenciaController.atualizarGrupoTransferencia(grupoTransferenciaAtualizar);;

        Assertions.assertEquals("Atualizei",grupoAtualizado.getBody().getNome());
    }

    @Test
    void deletarListaGrupoTransferencia() {

        List<Long> longs = new ArrayList<>();

        GrupoTransferenciaCadastroDTO grupoTransferenciaDTO = getGrupoTransferenciaCadastroDTO("Grupo deletarListaGrupoTransferencia", "grupo");
        ResponseEntity<GrupoTransferenciaDTO> grupoTransferenciaDTOResponseEntity = getGrupoAResponseEntity(grupoTransferenciaDTO);
        Long idGrupoTransferencia = Objects.requireNonNull(grupoTransferenciaDTOResponseEntity.getBody()).getIdGrupoTransferencia();

        longs.add(idGrupoTransferencia);

        GrupoTransferenciaCadastroDTO grupoTransferenciaDTO1 = getGrupoTransferenciaCadastroDTO("Grupo deletarListaGrupoTransferencia1", "grupo");
        getGrupoAResponseEntity(grupoTransferenciaDTO1);
        Long idGrupoTransferencia1 = Objects.requireNonNull(grupoTransferenciaDTOResponseEntity.getBody()).getIdGrupoTransferencia();

        longs.add(idGrupoTransferencia1);

        grupoTransferenciaController.deletarListaGrupoTransferencia(longs);
        ResponseEntity<GrupoTransferenciaDTO> grupoTransferenciaDTOResponseEntity2 = grupoTransferenciaController.buscarGrupoTransferencia(idGrupoTransferencia1);

        Assertions.assertNull(grupoTransferenciaDTOResponseEntity2.getBody().getIdGrupoTransferencia());
    }
}