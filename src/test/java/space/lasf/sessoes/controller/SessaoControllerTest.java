package space.lasf.sessoes.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import space.lasf.sessoes.controller.SessaoController;

import space.lasf.sessoes.controller.SessaoController;
import space.lasf.sessoes.core.util.ObjectsValidator;
import space.lasf.sessoes.dto.SessaoDto;
import space.lasf.sessoes.dto.VotoDto;
import space.lasf.sessoes.service.SessaoService;

import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessaoController.class)
class SessaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SessaoService sessaoService;

    // Mock validators to avoid NullPointerException as they are not initialized in this slice test
    @MockitoBean
    private ObjectsValidator<SessaoDto> itemPedidoValidator;
    @MockitoBean
    private ObjectsValidator<VotoDto> sessaoValidator;

    @Test
    void validarPedido_shouldReturnNotFound_whenPedidoDoesNotExist() throws Exception {
        Long sessaoId = Double.valueOf(Math.random()*100000).longValue();
        when(sessaoService.buscarSessaoPorId(sessaoId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/sessoes/{idPedido}/validar", sessaoId))
                .andExpect(status().isNotFound());
    }

    // Add more tests for other endpoints...

}