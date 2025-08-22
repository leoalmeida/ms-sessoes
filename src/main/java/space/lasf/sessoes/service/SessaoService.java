package space.lasf.sessoes.service;


import java.util.List;

import space.lasf.sessoes.dto.PautaDto;
import space.lasf.sessoes.dto.SessaoDto;

/**
 * Serviço para gerenciamento de sessoes.
 */
public interface SessaoService {
    
    SessaoDto criarSessao(PautaDto pautaDto, boolean iniciarSessao);

    boolean isSessaoAbertaParaVotacao(Long id);
    
    SessaoDto buscarSessaoPorId(Long id, Long idAssociado);
    
    SessaoDto buscarSessaoPorId(Long id);
    
    List<SessaoDto> buscarTodasSessoes();
    
    SessaoDto buscarResultadoDeVotacao(Long idSessao);
    
    SessaoDto iniciarSessao(Long idSessao);
    
    SessaoDto finalizarSessao(Long idSessao);
    
    void cancelarSessao(Long idSessao);

}