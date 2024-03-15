package br.com.efb.domain.service.impl;

import br.com.efb.domain.entity.ContaEntity;
import br.com.efb.domain.entity.GrupoTransferenciaEntity;
import br.com.efb.domain.entity.TipoTransferenciaEntity;
import br.com.efb.domain.excepition.ErroException;
import br.com.efb.domain.service.ContaService;
import br.com.efb.domain.service.GrupoTransferenciaService;
import br.com.efb.domain.service.TipoTransferenciaService;
import br.com.efb.domain.service.TransacaoService;
import com.efb.api.model.*;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TransacaoServiceImplTest {

    @Autowired
    private TransacaoService transacaoService;

    @Autowired
    private ContaService contaService;

    @Autowired
    private GrupoTransferenciaService grupoTransferenciaService;

    @Autowired
    private TipoTransferenciaService tipoTransferenciaService;


    @Test
    public void cadastrarTransacao() {

        criarContas();
        try {
            criarRegras();
        } catch (ErroException erroException){

        }

        TransacaoDTO transacaoDTO = transacaoService.cadastrarTransacao(getTransacaoCadastroDTO());
        Assertions.assertNotNull(Objects.nonNull(transacaoDTO));
    }

    private static TransacaoCadastroDTO getTransacaoCadastroDTO() {
        TransacaoCadastroDTO transacaoCadastroDTO = new TransacaoCadastroDTO();
        transacaoCadastroDTO.setContaOrigem("111111");
        transacaoCadastroDTO.setContaDestino("222222");
        transacaoCadastroDTO.setDataAgendamento(LocalDate.now().toString());
        transacaoCadastroDTO.setDataProgramada(LocalDate.now().toString());
        transacaoCadastroDTO.valorSolicitado(BigDecimal.valueOf(100.00));
        return transacaoCadastroDTO;
    }

    private void criarRegras() {
        GrupoTransferenciaEntity grupoTransferenciaEntity = new GrupoTransferenciaEntity();
        grupoTransferenciaEntity.setNome("Grupo A");
        grupoTransferenciaEntity.setDescricao("Transferência no mesmo dia do agendamento tem uma taxa de $3 mais 3% do valor a ser transferido");
        List<TipoTransferenciaEntity> tipoTransferenciaEntities = new ArrayList<>();
        grupoTransferenciaEntity.setTipoTransferenciaList(tipoTransferenciaEntities);

        TipoTransferenciaEntity tipoTransferenciaEntity = new TipoTransferenciaEntity();

        tipoTransferenciaEntity.setNome("A");
        tipoTransferenciaEntity.setDescricao("Transferência no mesmo dia do agendamento");
        tipoTransferenciaEntity.setMinDias(BigDecimal.ZERO);
        tipoTransferenciaEntity.setMaxDias(BigDecimal.ZERO);
        tipoTransferenciaEntity.setMinValor(BigDecimal.ZERO);
        tipoTransferenciaEntity.setMaxValor(new BigDecimal("1000.0"));
        tipoTransferenciaEntity.setTaxaFixa(new BigDecimal("3.0"));
        tipoTransferenciaEntity.setTaxaVariavel(new BigDecimal("3.0"));
        tipoTransferenciaEntity.setStatus(true);

        tipoTransferenciaService.validarRegra(tipoTransferenciaEntity);
        try {
            boolean validarRegra = tipoTransferenciaService.validarRegra(tipoTransferenciaEntity);
        } catch (ErroException erroException) {
            PageRequest pageRequest = PageRequest.of(
                    0,
                    1,
                    Sort.Direction.ASC,
                    "nome", "idTipoTransferencia");

            Page<TipoTransferenciaDTO> search = tipoTransferenciaService.search(tipoTransferenciaEntity.getNome(), pageRequest);
            List<TipoTransferenciaDTO> content = search.getContent();
            if (!content.isEmpty()) {
                TipoTransferenciaDTO tipoTransferenciaDTO = content.get(0);
                tipoTransferenciaEntity.setIdTipoTransferencia(tipoTransferenciaDTO.getIdTipoTransferencia());
            }
        }

        tipoTransferenciaEntities.add(tipoTransferenciaEntity);

        grupoTransferenciaService.save(grupoTransferenciaEntity);
    }

    private void criarContas() {
        ContaEntity conta = new ContaEntity();
        conta.setNome("Conta 1");
        conta.setContaCorrente("111111");
        contaService.save(conta);

        conta = new ContaEntity();
        conta.setNome("Conta 2");
        conta.setContaCorrente("222222");
        contaService.save(conta);
    }

}