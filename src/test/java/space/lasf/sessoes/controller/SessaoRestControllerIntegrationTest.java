package space.lasf.sessoes.controller;

import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.List;


import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import space.lasf.sessoes.domain.model.SessaoStatus;
import space.lasf.sessoes.domain.repository.SessaoRepository;
import space.lasf.sessoes.dto.SessaoDto;
import space.lasf.sessoes.service.SessaoService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SessaoRestControllerIntegrationTest {

  
    public static final String SESSAO_API_ENDPOINT = "/v1/sessoes";
    
    @Autowired
    private MockMvc mvc;

    @Autowired
    private SessaoRepository repository;

    @MockitoBean
    private SessaoService service;

    @Test
    public void testePedido_quandoConsultarTodosSessoes_entaoRetornaSessoesComSucesso()
      throws Exception {
        
        SessaoDto sessao1 = SessaoDto.builder()
                          .status(SessaoStatus.CLOSED)
                          .id(Double.valueOf(Math.random()*100000).longValue()).build();
        SessaoDto sessao2 = SessaoDto.builder()
                        .status(SessaoStatus.OPEN_TO_VOTE)
                        .id(Double.valueOf(Math.random()*100000).longValue()).build();
        List<SessaoDto> todasSessoes = Arrays.asList(sessao1,sessao2);

        doReturn(todasSessoes)
          .when(service).buscarTodasSessoes();

        mvc.perform(get(SESSAO_API_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(sessao1.getId())))
                .andExpect(jsonPath("$[1].id", is(sessao2.getId())));
    }
    
}
