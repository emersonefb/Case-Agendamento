package br.com.efb.domain.api.impl;

import br.com.efb.domain.excepition.ErroException;
import com.efb.api.model.ContaDTO;
import com.efb.api.model.ContaPageDTO;
import com.efb.api.model.ContaSearchDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest
class ContaControllerTest {

    @Autowired
    private ContaController contaController;

    @Test
    void cadastrarConta() {

        ContaDTO contaDTO = getContaDTO("Conta 1", "100000");

        ContaDTO contaDTO2 = getContaDTO("Conta 2", "200000");

        ContaDTO contaDTO3 = getContaDTO("Conta 3", "300000");

        ContaDTO contaDTO4 = getContaDTO("Conta 4", "444444");


        ContaDTO contaDTO5 = getContaDTO("Conta 5", "555555");

        ResponseEntity<ContaDTO> contaDTOResponseEntity = contaController.cadastrarConta(contaDTO);
        ResponseEntity<ContaDTO> contaDTOResponseEntity2 = contaController.cadastrarConta(contaDTO2);
        ResponseEntity<ContaDTO> contaDTOResponseEntity3 = contaController.cadastrarConta(contaDTO3);
        contaController.cadastrarConta(contaDTO4);
        contaController.cadastrarConta(contaDTO5);

        Assertions.assertNotNull(contaDTOResponseEntity.getBody());
        Assertions.assertNotNull(contaDTOResponseEntity.getBody().getIdConta());
        Assertions.assertEquals( 6L, contaDTOResponseEntity.getBody().getContaCorrente().length());

        Assertions.assertNotNull(contaDTOResponseEntity2.getBody());
        Assertions.assertNotNull(contaDTOResponseEntity2.getBody().getIdConta());
        Assertions.assertEquals( 6L, contaDTOResponseEntity2.getBody().getContaCorrente().length());

        Assertions.assertNotNull(contaDTOResponseEntity3.getBody());
        Assertions.assertNotNull(contaDTOResponseEntity3.getBody().getIdConta());
        Assertions.assertEquals(6L, contaDTOResponseEntity3.getBody().getContaCorrente().length() );


    }

    private static ContaDTO getContaDTO(String nome, String number) {
        ContaDTO contaDTO = new ContaDTO();
        contaDTO.setNome(nome);
        contaDTO.setContaCorrente(number);
        return contaDTO;
    }

    @Test
    void cadastrarContaErro() {

        ContaDTO contaDTO = getContaDTO("Conta 1", "999999");

        ContaDTO contaDTO2 = getContaDTO("Conta 2", "999999");

        ResponseEntity<ContaDTO> contaDTOResponseEntity = contaController.cadastrarConta(contaDTO);
        Assertions.assertNotNull(contaDTOResponseEntity.getBody());
        Assertions.assertNotNull(contaDTOResponseEntity.getBody().getIdConta());

        ErroException thrown = Assertions.assertThrows(ErroException.class, () -> {
            contaController.cadastrarConta(contaDTO2);
        });
        Assertions.assertEquals("conta ja eiste", thrown.getMessage());
    }

    @Test
    void listarContas() {

        ResponseEntity<ContaDTO> contaDTOResponseEntity = contaController.buscarConta(1L);

        Long idConta = Objects.requireNonNull(contaDTOResponseEntity.getBody()).getIdConta();
        if (Objects.isNull(idConta)){
            ContaDTO contaDTO = getContaDTO("Conta busca", "100011");
            contaController.cadastrarConta(contaDTO);
        }
        ContaSearchDTO contaSearchDTO = new ContaSearchDTO();
        contaSearchDTO.setPage(0);
        contaSearchDTO.setSize(10);

        ResponseEntity<ContaPageDTO> contaPageDTOResponseEntity = contaController.listarContas(contaSearchDTO);

        ContaPageDTO contaPageDTO = contaPageDTOResponseEntity.getBody();
        Assertions.assertNotNull(contaPageDTO);
        Assertions.assertTrue(contaPageDTO.getContent().size() > 0);
    }

    @Test
    void buscarConta() {

        ResponseEntity<ContaDTO> conta = contaController.buscarConta(3L);

        Long idConta = conta.getBody().getIdConta();
        if (Objects.isNull(idConta)){
            if (Objects.isNull(idConta)){
                ContaDTO contaDTO = getContaDTO("Conta busca", "100011");
                ResponseEntity<ContaDTO> contaDTOResponseEntity = contaController.cadastrarConta(contaDTO);
                idConta = Objects.requireNonNull(contaDTOResponseEntity.getBody()).getIdConta();
                conta = contaController.buscarConta(idConta);
            }
        }

        Assertions.assertNotNull(conta.getBody());
        Assertions.assertNotNull(conta.getBody().getIdConta());
        Assertions.assertEquals(conta.getBody().getContaCorrente().length() , 6L);
    }

    @Test
    void deletarConta() {
        ContaDTO contaDTO = getContaDTO("Conta Teste Deletar", "919191");
        ResponseEntity<ContaDTO> contaDTOResponseEntity = contaController.cadastrarConta(contaDTO);

        Long idConta = Objects.requireNonNull(contaDTOResponseEntity.getBody()).getIdConta();
        contaController.deletarConta(idConta);
        contaDTOResponseEntity = contaController.buscarConta(idConta);

        Assertions.assertNull(contaDTOResponseEntity.getBody().getIdConta());
    }

    @Test
    void atualizarConta() {
        ContaDTO contaDTO = getContaDTO("Conta Teste Atualizar", "101213");
        ResponseEntity<ContaDTO> contaDTOResponseEntity = contaController.cadastrarConta(contaDTO);

        ContaDTO body = contaDTOResponseEntity.getBody();
        assert body != null;
        body.setNome("Atualizei");
        ResponseEntity<ContaDTO> contaAtual = contaController.atualizarConta(body);
        Long idConta = Objects.requireNonNull(contaAtual.getBody()).getIdConta();

        contaDTOResponseEntity = contaController.buscarConta(idConta);
        Assertions.assertNotNull(contaDTOResponseEntity);
        Assertions.assertNotNull(body.getIdConta());
        Assertions.assertEquals(body.getContaCorrente().length() , 6L);
        Assertions.assertEquals("Atualizei", body.getNome());
    }

    @Test
    void deletarListaContas() {

        List<Long> longs = new ArrayList<>();

        ContaDTO contaDTO = getContaDTO("Conta Teste deletar 1", "101299");
        ResponseEntity<ContaDTO> contaDTOResponseEntity = contaController.cadastrarConta(contaDTO);

        Long idConta1 = Objects.requireNonNull(contaDTOResponseEntity.getBody()).getIdConta();

        ContaDTO contaDTO2 = getContaDTO("deletar 2", "101222");
        ResponseEntity<ContaDTO> contaDTOResponseEntity2 = contaController.cadastrarConta(contaDTO2);

        Long idConta2 = Objects.requireNonNull(contaDTOResponseEntity2.getBody()).getIdConta();

        longs.add(idConta1);
        longs.add(idConta2);

        contaController.deletarListaContas(longs);
        contaDTOResponseEntity = contaController.buscarConta(idConta1);

        Assertions.assertNull(Objects.requireNonNull(contaDTOResponseEntity.getBody()).getIdConta());

    }
}