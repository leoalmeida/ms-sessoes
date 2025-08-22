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
import space.lasf.sessoes.domain.repository.AssociadoRepository;
import space.lasf.sessoes.domain.repository.VotoRepository;
import space.lasf.sessoes.dto.SessaoDto;
import space.lasf.sessoes.basicos.AbstractIntegrationTest;


public class SessaoControllerIntegrationTest extends AbstractIntegrationTest{


    @Autowired
    private VotoRepository repository;
    
    @Autowired
    private AssociadoRepository associadoRepository;
    

    private Associado associado;
    private SessaoDto sessao;


    @BeforeEach
    public void setUp() {
        associado = gerarAssociado("Marta Rocha", "(51) 99999-5555");
        sessao = gerarSessaoDto(associado);
        associadoRepository.save(associado);
    }

    @AfterEach
    void clean() {
        associadoRepository.deleteById(associado.getId());
    }
    
    @Test
    public void dadoPedidoDtoCorreto_entaoSalvaPedido_eRetornaPedidoDto()
      throws Exception {
        List<SessaoDto> items = new ArrayList<>();
        items.add(sessao);
        String endpoint = UriComponentsBuilder
                    .fromUriString(SESSAO_API_ENDPOINT)
                    .queryParam("idAssociado", associado.getId())
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
