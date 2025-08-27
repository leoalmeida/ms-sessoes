package space.lasf.sessoes.domain.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Optional;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import org.springframework.test.context.ActiveProfiles;

import space.lasf.sessoes.domain.model.Sessao;
import space.lasf.sessoes.domain.model.SessaoStatus;
import space.lasf.sessoes.domain.repository.SessaoRepository;
import space.lasf.sessoes.dto.PautaDto;
import space.lasf.sessoes.dto.SessaoDto;

//@ExtendWith(SpringExtension.class)
@DataMongoTest
@ActiveProfiles("test")
public class SessaoRepositoryIntegrationTest {

    @Autowired
    private SessaoRepository sessaoRepository;

    Sessao sessao1;
    Sessao sessao2;
    Sessao sessao3;
    PautaDto pauta1;

    
    @BeforeEach
    public void setUp() {
        // Criar sessoes
        
        sessao1 = Sessao.builder()
                          .id(Double.valueOf(Math.random()*100000).longValue())
                          .idPauta(Double.valueOf(Math.random()*100000).longValue())
                          .build();
        sessao2 = Sessao.builder()
                          .id(Double.valueOf(Math.random()*100000).longValue())
                          .idPauta(Double.valueOf(Math.random()*100000).longValue())
                          .build();
        sessao3 = Sessao.builder()
                          .id(Double.valueOf(Math.random()*100000).longValue())
                          .idPauta(Double.valueOf(Math.random()*100000).longValue())
                          .build();
        sessaoRepository.saveAll(Arrays.asList(sessao1,sessao2,sessao3));
    }

    @AfterEach
    void clean() {
        sessaoRepository.delete(sessao1);
        sessaoRepository.delete(sessao2);
        sessaoRepository.delete(sessao3);
    }

    @Test
    public void shouldBeNotEmpty() {
        assertTrue(sessaoRepository.findAll().size()>0);
    }

    @Test
    void dadoSessao_quandoCriarSessao_entaoSessaoPersistido() {
        // given
        Sessao sessao1 = Sessao.builder()
                    .id(Double.valueOf(Math.random()*100000).longValue())
                    .idPauta(Double.valueOf(Math.random()*100000).longValue())
                    .build();
        
        // when
        sessaoRepository.save(sessao1);

        // then
        Optional<Sessao> retrievedSessao = sessaoRepository.findById(sessao1.getId());
        assertTrue(retrievedSessao.isPresent());
        assertEquals(sessao1.getId(), retrievedSessao.get().getId());
    }
}
