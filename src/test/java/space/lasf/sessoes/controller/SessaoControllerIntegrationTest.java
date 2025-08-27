package space.lasf.sessoes.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.util.UriComponentsBuilder;

import space.lasf.sessoes.basicos.AbstractIntegrationTest;
import space.lasf.sessoes.domain.model.SessaoStatus;
import space.lasf.sessoes.domain.repository.VotoRepository;
import space.lasf.sessoes.dto.PautaDto;
import space.lasf.sessoes.dto.SessaoDto;


public class SessaoControllerIntegrationTest extends AbstractIntegrationTest{

    
    public static final String SESSAO_API_ENDPOINT = "/v1/sessoes";

    @Autowired
    private VotoRepository repository;
    

    private SessaoDto sessao;


    @BeforeEach
    public void setUp() {
        // Cria pauta para testes
        PautaDto pauta = new PautaDto();
        pauta.setId(Double.valueOf(Math.random()*100000).longValue());
        pauta.setNome("Nome0");
        pauta.setDescricao("Descricao0");
        
        // Cria sessao para testes
        sessao = new SessaoDto();
        sessao.setId(Double.valueOf(Math.random()*100000).longValue());
        sessao.setPautaDto(pauta);
        sessao.setStatus(SessaoStatus.CREATED);
        sessao.setDataInicioSessao(null);
        sessao.setDataFimSessao(null);
        sessao.setTotalizadores(null);
    }

    @AfterEach
    void clean() {
    }
    
    @Test
    public void dadoPedidoDtoCorreto_entaoSalvaPedido_eRetornaPedidoDto()
      throws Exception {
        List<SessaoDto> items = new ArrayList<>();
        items.add(sessao);
        String endpoint = UriComponentsBuilder
                    .fromUriString(SESSAO_API_ENDPOINT)
                    .queryParam("idAssociado", 1L)
                    .build()
                    .toUriString();

        // when
        SessaoDto savedPedidoDto = performPostRequestExpectedSuccess(
                                    endpoint, items, SessaoDto.class);


        //then
        assertNotNull(savedPedidoDto);
        assertEquals(sessao.getId(), savedPedidoDto.getId());
        assertEquals(sessao.getStatus(), savedPedidoDto.getStatus());
    }
    
}
