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

import space.lasf.sessoes.basicos.TestFactory;
import space.lasf.sessoes.domain.model.Associado;
import space.lasf.sessoes.domain.model.Sessao;
import space.lasf.sessoes.domain.repository.SessaoRepository;
import space.lasf.sessoes.domain.repository.SessaoRepository;
import space.lasf.sessoes.basicos.TestFactory;


//@ExtendWith(SpringExtension.class)
@DataMongoTest
@ActiveProfiles("test")
public class SessaoRepositoryIntegrationTest extends TestFactory{

    @Autowired
    private SessaoRepository sessaoRepository;

    Sessao sessao1;
    Sessao sessao2;
    Sessao sessao3;
    Pauta pauta1;

    
    @BeforeEach
    public void setUp() {
        
        Associado associado = gerarAssociado(null, null);
        // Criar sessaos
        sessao1 = gerarSessao(associado,gerarPauta());
        sessao2 = gerarSessao(associado,gerarPauta());
        sessao3 = gerarSessao(associado,gerarPauta());

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
        Pauta pauta1 = new Pauta();
        pauta1.setId(Double.valueOf(Math.random()*100000).longValue());
        pauta1.setNome("Produto 1");
        pauta1.setDescricao("Descrição do Produto 1");
        
        Sessao sessao1 = Sessao.builder()
                    .id(Double.valueOf(Math.random()*100000).longValue())
                    .pauta(pauta1)
                    .idAssociado(Double.valueOf(Math.random()*100000).longValue())
                    .build();
        
        // when
        sessaoRepository.save(sessao1);

        // then
        Optional<Sessao> retrievedSessao = sessaoRepository.findById(sessao1.getId());
        assertTrue(retrievedSessao.isPresent());
        assertEquals(sessao1.getId(), retrievedSessao.get().getId());
    }
}
