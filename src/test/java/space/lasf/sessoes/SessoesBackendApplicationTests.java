package space.lasf.sessoes;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import space.lasf.sessoes.SessoesBackendApplication;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SessoesBackendApplication.class)
@ActiveProfiles("test")
@TestPropertySource(properties = { 
	"logging.level.org.springframework=INFO",
    "logging.level.org.hibernate=WARN" })
class SessoesBackendApplicationTests {

	@Test
	void contextLoads() {
		assertTrue(true);
	}

}
