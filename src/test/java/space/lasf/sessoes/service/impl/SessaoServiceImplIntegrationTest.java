package space.lasf.sessoes.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import org.springframework.test.context.ActiveProfiles;

import space.lasf.sessoes.domain.model.SessaoStatus;
import space.lasf.sessoes.domain.model.VotoOpcao;
import space.lasf.sessoes.domain.repository.SessaoRepository;
import space.lasf.sessoes.domain.repository.VotoRepository;
import space.lasf.sessoes.dto.PautaDto;
import space.lasf.sessoes.dto.SessaoDto;
import space.lasf.sessoes.service.SessaoService;

@DataMongoTest
@ActiveProfiles("test")
public class SessaoServiceImplIntegrationTest {
    
    @Autowired
    VotoRepository votoRepository;
    
    @Autowired
    SessaoRepository sessaoRepository; 
    
    @Autowired
    private SessaoService sessaoService;
    
    @TestConfiguration
    static class SessaoServiceImplTestContextConfiguration {
        
        @Autowired
        VotoRepository votoRepository;
        
        @Autowired
        SessaoRepository sessaoRepository; 

        @Autowired
        ModelMapper modelMapper;

        @Bean
        public SessaoService sessaoService() {
            return new SessaoServiceImpl(votoRepository, sessaoRepository);
        }
        
    }

    PautaDto pauta;
    SessaoDto sessao;

    @BeforeEach
    public void setUp() {
        // Cria pauta para testes
        pauta = new PautaDto();
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
    public void coolDown() {
    }

    @Test
    public void testCreateSessao() {
        // Cria pauta para iniciar sessao
        PautaDto novaPauta = new PautaDto();
        novaPauta.setNome("Nome1");
        novaPauta.setDescricao("Descricao1");
        novaPauta.setCategoria("Cat1");
       
        // Executa o método
        SessaoDto createdSessao = sessaoService.criarSessao(novaPauta, Boolean.FALSE);
        
        // Verifica o resultado
        assertNotNull(createdSessao,"Sessao criada não deveria ser nulo");
        assertNotNull(createdSessao.getId(),"Sessao criada deveria ter um ID");
        assertEquals(novaPauta, createdSessao.getPautaDto(),"Sessao deveria ter o associado correto");
        assertEquals(SessaoStatus.CREATED, createdSessao.getStatus(),"Status da sessao deveria estar CREATED");
        
        // Verifica se o sessao foi realmente salva no banco de dados
        SessaoDto foundSessao = sessaoService.buscarSessaoPorId(createdSessao.getId());
        assertNotNull(foundSessao,"Sessao deveria ser encontrada no banco de dados");
        assertEquals(SessaoStatus.CREATED, foundSessao.getStatus(),"Status da sessao deveria estar CREATED");
    }

    @Test
    public void testIniciarSessao() {
        
        // Executa o método
        sessaoService.iniciarSessao(sessao.getId());

        // Verifica se a sessão foi atualizada
        SessaoDto sessaoAtualizada = sessaoService.buscarSessaoPorId(sessao.getId());
        assertNotNull(sessaoAtualizada, "Sessao deveria ser encontrada");
        assertEquals(sessao.getId(), sessaoAtualizada.getId(),"Sessao deveria ter o mesmo ID");
        assertEquals(SessaoStatus.OPEN_TO_VOTE, sessaoAtualizada.getStatus(),"Sessao deveria ter a mesma quantidade de items.");

    }


    @Test
    public void testFindSessaoById() {
        // Executa o método
        SessaoDto foundSessao = sessaoService.buscarSessaoPorId(sessao.getId());

        // Verifica o resultado
        assertNotNull(foundSessao, "Sessao deveria ser encontrada");
        assertEquals(sessao.getId(), foundSessao.getId(),
                "Sessao encontrada deveria ter o ID correto" );
        assertEquals(SessaoStatus.CREATED, foundSessao.getStatus(),
                    "Sessao encontrada deveria ter o número correto");
    }

    @Test
    public void testFindAllSessoes() {
        // Executa o método
        List<SessaoDto> sessoes = sessaoService.buscarTodasSessoes();

        // Verifica o resultado
        assertEquals(1, sessoes.size(),"Deveria encontrar 1 sessao");
        assertEquals(sessao.getId(), sessoes.get(0).getId(),
                    "Sessao encontrada deveria ter o número correto");
    }

   
    @Test
    public void testFinalizarSessaoAberta() {
 
        // Executa o método
        sessaoService.finalizarSessao(sessao.getId());

        // Verifica se o sessao foi finalizado
        SessaoDto finalizedSessao = sessaoService.buscarSessaoPorId(sessao.getId());
        assertNotNull(finalizedSessao,"Sessao deveria ser encontrada");
        assertEquals(SessaoStatus.CLOSED, finalizedSessao.getStatus(), "Status do sessao deveria ser CLOSED");
        assertEquals(VotoOpcao.NAO, finalizedSessao.getResultado(),"Resutado da sessao deveria estar correta");
        assertNotNull(finalizedSessao.getDataFimSessao(), "Data Fim deveria estar preenchida");
        assertNotNull(finalizedSessao.getTotalizadores(), "Totalizadores deveriam estar preenchidos");
        assertEquals(VotoOpcao.values().length,finalizedSessao.getTotalizadores().size(), "Deveriam ter 2 totalizadores preenchidos");

    }

    @Test
    public void testCancelarSessaoAberta() {
        // Executa o método
        sessaoService.cancelarSessao(sessao.getId());

        // Verifica se o sessao foi cancelado
        SessaoDto sessaoCancelada = sessaoService.buscarSessaoPorId(sessao.getId());
        assertNotNull(sessaoCancelada, "Sessao deveria ser encontrada");

        assertEquals(SessaoStatus.CANCELLED, sessaoCancelada.getStatus(), "Status do sessao deveria ser CANCELADO");
        assertNotNull(sessaoCancelada.getDataFimSessao(), "Data FIm deveria estar preenchida");
    }

    @Test
    public void testCreateSessaoWithInvalidAssociadoId() {
        // Executa o método - deve lançar IllegalArgumentException
        
        Throwable  throwable  = 
                assertThrows(IllegalArgumentException.class, () ->{
                    sessaoService.criarSessao(pauta,Boolean.FALSE);
                });
        
        assertEquals(IllegalArgumentException.class, throwable.getClass());
    }

}