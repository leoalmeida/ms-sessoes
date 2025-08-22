package space.lasf.sessoes.basicos;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import space.lasf.sessoes.SessoesBackendApplication;
import space.lasf.sessoes.controller.SessaoController;

@SpringBootTest
@ContextConfiguration(classes = SessoesBackendApplication.class)
@ActiveProfiles("test")
public class SmokeTest {
	@Autowired
	private SessaoController pedidoController;
	
	@Test
	void contextLoads() throws Exception {
		assertThat(pedidoController).isNotNull();
	}
}