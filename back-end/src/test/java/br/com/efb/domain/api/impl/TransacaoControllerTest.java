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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class TransacaoControllerTest {

    @Autowired
    private TransacaoController controller;


    @Autowired
    private ContaController contaController;

    @Autowired
    private TipoTransferenciaController tipoTransferenciaController;

    @Autowired
    private GrupoTransferenciaController grupoTransferenciaController;

    @Test
    void cadastrarTransacao() {

        criarContas();
        criarRegras();

        TransacaoCadastroDTO transacaoCadastroDTO = getTransacaoCadastroDTO("100000", "200000", LocalDate.now().toString(), BigDecimal.valueOf(100.00));

        ResponseEntity<TransacaoDTO> transacaoDTOResponseEntity = controller.cadastrarTransacao(transacaoCadastroDTO);

        Assertions.assertNotNull(transacaoDTOResponseEntity.getBody());
        TransacaoDTO body = transacaoDTOResponseEntity.getBody();
        Assertions.assertNotNull(body.getIdTransacao());
        Assertions.assertNotNull(body.getValorTransacao());
        Assertions.assertNotNull(body.getDataProgramada());
        Assertions.assertNotNull(body.getTipoTransferencia());
        Assertions.assertEquals(new BigDecimal("3.00"),body.getTaxaFixa().setScale(2, BigDecimal.ROUND_HALF_EVEN));
        Assertions.assertEquals(new BigDecimal("3.00"),body.getTaxaVariavel().setScale(2, BigDecimal.ROUND_HALF_EVEN));
        Assertions.assertEquals(new BigDecimal("106.09"),body.getValorTransacao().setScale(2, BigDecimal.ROUND_HALF_EVEN));

    }
    @Test
    void cadastrarTransacaoContaNaoCadastrada() {

        TransacaoCadastroDTO transacaoCadastroDTO = getTransacaoCadastroDTO("111111", "222222", LocalDate.now().toString(), BigDecimal.valueOf(100.00));

        ErroException thrown = Assertions.assertThrows(ErroException.class, () -> {
            controller.cadastrarTransacao(transacaoCadastroDTO);
        });
        Assertions.assertEquals("Contas não encontrada", thrown.getMessage());

    }

    @Test
    void listarTransacoes() {

        TransacaoCadastroDTO transacaoCadastroDTO = getTransacaoCadastroDTO("100000", "200000", LocalDate.now().toString(), BigDecimal.valueOf(100.00));

        ResponseEntity<TransacaoDTO> transacaoDTOResponseEntity = controller.cadastrarTransacao(transacaoCadastroDTO);

        TransacaoSearchDTO transacaoSearchDTO = new TransacaoSearchDTO();
        transacaoSearchDTO.setPage(0);
        transacaoSearchDTO.setSize(10);
        ResponseEntity<TransacaoPageDTO> transacaoPageDTOResponseEntity = controller.listarTransacoes(transacaoSearchDTO);

        Assertions.assertNotNull(transacaoPageDTOResponseEntity.getBody().getContent().size() > 0);
        Assertions.assertNotNull(transacaoDTOResponseEntity.getBody());
        TransacaoDTO body = transacaoDTOResponseEntity.getBody();
        Assertions.assertNotNull(body.getIdTransacao());
        Assertions.assertNotNull(body.getValorTransacao());
        Assertions.assertNotNull(body.getDataProgramada());
        Assertions.assertNotNull(body.getTipoTransferencia());
    }

    @Test
    void listarTransacoesContaOrigen() {

        TransacaoCadastroDTO transacaoCadastroDTO = getTransacaoCadastroDTO("100000", "200000", LocalDate.now().toString(), BigDecimal.valueOf(100.00));

        ResponseEntity<TransacaoDTO> transacaoDTOResponseEntity = controller.cadastrarTransacao(transacaoCadastroDTO);

        TransacaoSearchDTO transacaoSearchDTO = new TransacaoSearchDTO();
        transacaoSearchDTO.setPage(0);
        transacaoSearchDTO.setSize(10);
        transacaoSearchDTO.setContaOrigem(transacaoDTOResponseEntity.getBody().getContaOrigem());
        ResponseEntity<TransacaoPageDTO> transacaoPageDTOResponseEntity = controller.listarTransacoes(transacaoSearchDTO);

        Assertions.assertNotNull(transacaoPageDTOResponseEntity.getBody().getContent().size() > 0);
        Assertions.assertNotNull(transacaoDTOResponseEntity.getBody());
        TransacaoDTO body = transacaoDTOResponseEntity.getBody();
        Assertions.assertNotNull(body.getIdTransacao());
        Assertions.assertEquals("100000", body.getContaOrigem().getContaCorrente());
        Assertions.assertNotNull(body.getIdTransacao());
        Assertions.assertNotNull(body.getValorTransacao());
        Assertions.assertNotNull(body.getDataProgramada());
        Assertions.assertNotNull(body.getTipoTransferencia());
    }

    @Test
    void listarTransacoesContaDestino() {

        TransacaoCadastroDTO transacaoCadastroDTO = getTransacaoCadastroDTO("100000", "200000", LocalDate.now().toString(), BigDecimal.valueOf(100.00));

        ResponseEntity<TransacaoDTO> transacaoDTOResponseEntity = controller.cadastrarTransacao(transacaoCadastroDTO);

        TransacaoSearchDTO transacaoSearchDTO = new TransacaoSearchDTO();
        transacaoSearchDTO.setPage(0);
        transacaoSearchDTO.setSize(10);
        transacaoSearchDTO.setContaDestino(transacaoDTOResponseEntity.getBody().getContaDestino());
        ResponseEntity<TransacaoPageDTO> transacaoPageDTOResponseEntity = controller.listarTransacoes(transacaoSearchDTO);

        Assertions.assertNotNull(transacaoPageDTOResponseEntity.getBody().getContent().size() > 0);
        Assertions.assertNotNull(transacaoDTOResponseEntity.getBody());
        TransacaoDTO body = transacaoDTOResponseEntity.getBody();
        Assertions.assertNotNull(body.getIdTransacao());
        Assertions.assertEquals("200000", body.getContaDestino().getContaCorrente());
        Assertions.assertNotNull(body.getIdTransacao());
        Assertions.assertNotNull(body.getValorTransacao());
        Assertions.assertNotNull(body.getDataProgramada());
        Assertions.assertNotNull(body.getTipoTransferencia());
    }

    @Test
    void buscarTransacao() {
        TransacaoCadastroDTO transacaoCadastroDTO = getTransacaoCadastroDTO("100000", "200000", LocalDate.now().toString(), BigDecimal.valueOf(100.00));

        ResponseEntity<TransacaoDTO> transacaoDTOResponseEntity = controller.cadastrarTransacao(transacaoCadastroDTO);

        transacaoDTOResponseEntity = controller.buscarTransacao(transacaoDTOResponseEntity.getBody().getIdTransacao());

        Assertions.assertNotNull(transacaoDTOResponseEntity.getBody());
        TransacaoDTO body = transacaoDTOResponseEntity.getBody();
        Assertions.assertNotNull(body.getIdTransacao());
        Assertions.assertNotNull(body.getValorTransacao());
        Assertions.assertNotNull(body.getDataProgramada());
        Assertions.assertNotNull(body.getTipoTransferencia());
    }

    @Test
    void deletarTransacao() {

        TransacaoCadastroDTO transacaoCadastroDTO = getTransacaoCadastroDTO("100000", "200000", LocalDate.now().toString(), BigDecimal.valueOf(100.00));
        ResponseEntity<TransacaoDTO> transacaoDTOResponseEntity = controller.cadastrarTransacao(transacaoCadastroDTO);

        controller.deletarTransacao(transacaoDTOResponseEntity.getBody().getIdTransacao());
        transacaoDTOResponseEntity = controller.buscarTransacao(transacaoDTOResponseEntity.getBody().getIdTransacao());

        TransacaoDTO body = transacaoDTOResponseEntity.getBody();
        Assertions.assertNull(body.getIdTransacao());

    }

    @Test
    void deletarListaTransacao() {

        List<Long> longs = new ArrayList<>();
        TransacaoCadastroDTO transacaoCadastroDTO = getTransacaoCadastroDTO("100000", "200000", LocalDate.now().toString(), BigDecimal.valueOf(100.00));
        ResponseEntity<TransacaoDTO> transacaoDTOResponseEntity = controller.cadastrarTransacao(transacaoCadastroDTO);

        Long idTransacao1 = transacaoDTOResponseEntity.getBody().getIdTransacao();
        longs.add(idTransacao1);
        transacaoCadastroDTO = getTransacaoCadastroDTO("100000", "200000", LocalDate.now().toString(), BigDecimal.valueOf(100.00));
        ResponseEntity<TransacaoDTO> transacaoDTOResponseEntity2 = controller.cadastrarTransacao(transacaoCadastroDTO);
        Long idTransacao2 = transacaoDTOResponseEntity2.getBody().getIdTransacao();
        longs.add(idTransacao2);

        controller.deletarListaTransacao(longs);

        transacaoDTOResponseEntity = controller.buscarTransacao(idTransacao1);

        TransacaoDTO body = transacaoDTOResponseEntity.getBody();
        Assertions.assertNull(body.getIdTransacao());

        transacaoDTOResponseEntity = controller.buscarTransacao(idTransacao2);

        body = transacaoDTOResponseEntity.getBody();
        Assertions.assertNull(body.getIdTransacao());
    }

    private void criarContas() {
        ContaDTO contaDTO = getContaDTO("Conta 1", "100000");

        ContaDTO contaDTO2 = getContaDTO("Conta 2", "200000");

        ContaSearchDTO contaSearchDTO = new ContaSearchDTO();
        contaSearchDTO.setPage(0);
        contaSearchDTO.setSize(10);
        contaSearchDTO.setContaCorrente(contaDTO.getContaCorrente());
        ResponseEntity<ContaPageDTO> contaPageDTOResponseEntity = contaController.listarContas(contaSearchDTO);
        if (contaPageDTOResponseEntity.getBody().getContent().isEmpty()){
            ResponseEntity<ContaDTO> contaDTOResponseEntity = contaController.cadastrarConta(contaDTO);
            contaDTO = contaDTOResponseEntity.getBody();
        } else {
            contaDTO = contaPageDTOResponseEntity.getBody().getContent().get(0);
        }

        contaSearchDTO.setContaCorrente(contaDTO2.getContaCorrente());
        contaPageDTOResponseEntity = contaController.listarContas(contaSearchDTO);
        if (contaPageDTOResponseEntity.getBody().getContent().isEmpty()){
            ResponseEntity<ContaDTO> contaDTOResponseEntity2 = contaController.cadastrarConta(contaDTO2);
            contaDTO2 = contaDTOResponseEntity2.getBody();
        } else {
            contaDTO2 = contaPageDTOResponseEntity.getBody().getContent().get(0);
        }

        Assertions.assertNotNull(contaDTO);
        Assertions.assertNotNull(contaDTO.getIdConta());
        Assertions.assertEquals(contaDTO.getContaCorrente().length() , 6L);

        Assertions.assertNotNull(contaDTO2);
        Assertions.assertNotNull(contaDTO2.getIdConta());
        Assertions.assertEquals(contaDTO2.getContaCorrente().length() , 6L);

    }
    private static ContaDTO getContaDTO(String nome, String number) {
        ContaDTO contaDTO = new ContaDTO();
        contaDTO.setNome(nome);
        contaDTO.setContaCorrente(number);
        return contaDTO;
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

    private static TransacaoCadastroDTO getTransacaoCadastroDTO(String contaOrigem, String contaDestino, String dataProgramada, BigDecimal valorSolicitado) {
        TransacaoCadastroDTO transacaoCadastroDTO = new TransacaoCadastroDTO();
        transacaoCadastroDTO.setContaOrigem(contaOrigem);
        transacaoCadastroDTO.setContaDestino(contaDestino);
        transacaoCadastroDTO.setDataAgendamento(LocalDate.now().toString());
        transacaoCadastroDTO.setDataProgramada(dataProgramada);
        transacaoCadastroDTO.valorSolicitado(valorSolicitado);
        return transacaoCadastroDTO;
    }

    private void criarRegras() {

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

        Assertions.assertNotNull(grupoTransferenciaD);
        Assertions.assertNotNull(grupoTransferenciaD.getIdGrupoTransferencia());
        Assertions.assertEquals("Teste Grupo D", grupoTransferenciaD.getNome());

        Assertions.assertNotNull(grupoTransferenciaB);
        Assertions.assertNotNull(grupoTransferenciaB.getIdGrupoTransferencia());
        Assertions.assertEquals("Teste Grupo B", grupoTransferenciaB.getNome());

    }

}