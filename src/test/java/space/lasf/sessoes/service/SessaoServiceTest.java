package space.lasf.sessoes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import space.lasf.sessoes.basicos.TestFactory;
import space.lasf.sessoes.domain.model.Associado;
import space.lasf.sessoes.domain.model.Pauta;
import space.lasf.sessoes.domain.model.Sessao;
import space.lasf.sessoes.domain.model.SessaoStatus;
import space.lasf.sessoes.domain.model.VotoOpcao;
import space.lasf.sessoes.domain.repository.AssociadoRepository;
import space.lasf.sessoes.domain.repository.PautaRepository;
import space.lasf.sessoes.domain.repository.SessaoRepository;
import space.lasf.sessoes.service.impl.SessaoServiceImpl;
import space.lasf.sessoes.basicos.TestFactory;

@ExtendWith(SpringExtension.class)
public class SessaoServiceTest  extends TestFactory{

    @Mock
    private SessaoRepository sessoesRepository;

    @Mock
    private AssociadoRepository associadosRepository;

    @Mock
    private SessaoRepository itemSessaoRepository;

    @Mock
    private PautaRepository pautaRepository;
    
    @Mock
    private ObjectsValidator<Sessao> validadorDeSessao;

    @InjectMocks
    private SessaoServiceImpl service;

    Pauta pautaEntity;

    @BeforeEach
    public void setUp() {
        pautaEntity = gerarPauta();
    }


    @Test
    public void testeCriacaoDeSessaoParaAssociadoValido() {
        // Cria um mock de Order para poder mockar os métodos de calculo 
        // Configura os mocks
        Sessao mockSessao = mock(Sessao.class);
        Associado mockAssociado = mock(Associado.class);

        doReturn(Optional.of(mockAssociado)).when(associadosRepository)
            .findById(eq(1L));
        doReturn(mockSessao).when(sessoesRepository).save(any(Sessao.class));
        doReturn(mockAssociado).when(associadosRepository).save(any(Associado.class));

        // Executa o método
        Sessao createdSessao = service.criarSessao(1L, pautaEntity);

        // Verifica o resultado
        assertNotNull(createdSessao, "Sessao criado não deveria ser nulo");
        assertEquals(mockAssociado.getId(), createdSessao.getIdAssociado(), "Sessao deveria ter o associado correto");

        // Verifica se os métodos foram chamados
        verify(associadosRepository, times(1))
            .findById(eq(1L));
        // Verifica que persist foi chamado pelo menos uma vez para Sessao e uma vez para ItemSessao
        // Não verificamos o número exato de chamadas porque isso pode variar dependendo da implementação
        verify(sessoesRepository, atLeastOnce()).save(any(Sessao.class));
    }

    @Test
    public void testeBuscarSessaoPorIdSessao() {
        // Configura os mocks
        Sessao mockSessao = mock(Sessao.class);

        doReturn(Optional.of(mockSessao)).when(sessoesRepository).findById(eq(1L));

        // Executa o método
        Optional<Sessao> foundSessao = service.buscarSessaoPorId(1L);

        // Verifica o resultado
        assertTrue(foundSessao.isPresent(),"Sessao deveria ser encontrada");
        assertEquals(mockSessao, foundSessao.get(),
                "Sessao encontrada deveria ser a sessão definida");

        // Verifica se o método foi chamado
        verify(sessoesRepository, times(1)).findById(1L);
    }

    @Test
    public void testeBucarTodasSessoes() {
        // Configura os mocks
        Sessao mockSessao = mock(Sessao.class);
        Sessao mockSessao2 = mock(Sessao.class);
        Sessao mockSessao3 = mock(Sessao.class);

        doReturn(Arrays.asList(mockSessao,mockSessao2,mockSessao3))
                .when(sessoesRepository).findAll();

        // Executa o método
        List<Sessao> sessoes = service.buscarTodasSessoes();

        // Verifica o resultado
        assertEquals( 3, sessoes.size(), "Deveria encontrar 3 sessoes");

        // Verifica se os métodos foram chamados
        verify(sessoesRepository, times(1)).findAll();
    }

    
    @Test
    public void testeFinalizacaoDeUmaSessao() {
        // Cria um mock de Sessao para poder mockar o método calcularTotalSessao
        Sessao mockSessao = 
            mock(Sessao.class)
                .iniciarSessao()
                .contabilizarVoto(VotoOpcao.NAO, 1L)
                .contabilizarVoto(VotoOpcao.NAO, 2L)
                .contabilizarVoto(VotoOpcao.SIM, 3L);
        
        // Configura o Repository para retornar o mock de Sessao
        doReturn(Optional.of(mockSessao)).when(sessoesRepository).findById(eq(1L));
        doReturn(mockSessao).when(sessoesRepository).save(any(Sessao.class));
        
        // Executa o método
        service.finalizarSessao(1L);

        // Verifica o resultado
        assertEquals(VotoOpcao.NAO, mockSessao.getResultado(), "O NÃO deveria ser o resultado final");
        assertEquals(VotoOpcao.values().length, 
                            mockSessao.getTotalizadores().size(), 
                            "Deveria existir um totalizador para cada tipo de resposta");
        assertNotNull(mockSessao.getDataFimSessao(), 
                            "A Data final deveria estar preenchida");
        assertEquals(SessaoStatus.CLOSED, 
                            mockSessao.getStatus(), 
                            "Sessão deveria ser encerrado no final");

        // Verifica se os métodos foram chamados
        verify(sessoesRepository, times(1)).findById(eq(1L));
        verify(sessoesRepository, times(1)).save(any(Sessao.class));
        verify(mockSessao, times(1)).finalizarSessao();        
    }

    @Test
    public void testeCancelamentoDeUmaSessao() {
        // Cria um mock de Sessao para poder mockar o método cancelarSessao
        Sessao mockSessao = mock(Sessao.class)
                                .iniciarSessao();
        // Configura o Repository para retornar o mock de Sessao
        doReturn(Optional.of(mockSessao)).when(sessoesRepository).findById(eq(1L));
        doReturn(mockSessao).when(sessoesRepository).save(any(Sessao.class));

        // Executa o método
        service.cancelarSessao(1L);
        
        // Verifica o resultado
        assertNotNull(mockSessao.getDataFimSessao(), 
                            "A Data final deveria estar preenchida");
        assertEquals(SessaoStatus.CANCELLED, 
                            mockSessao.getStatus(), 
                            "Sessão deveria ser encerrado no final");

        // Verifica se os métodos foram chamados
        verify(sessoesRepository, times(1)).findById(eq(1L));
        verify(mockSessao, times(1)).cancelarSessao();
        verify(sessoesRepository, times(1)).save(any(Sessao.class));
       
    }
}