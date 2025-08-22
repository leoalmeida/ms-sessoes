package space.lasf.sessoes.service;

import java.util.List;
import java.util.Map;

import space.lasf.sessoes.dto.VotoDto;

/**
 * Serviço para gerenciamento de votos.
 */
public interface VotoService {
    
    VotoDto criarVoto(VotoDto voto);
    
    VotoDto buscarVotoPorId(Long id);
    
    List<VotoDto> buscarVotosSessao(Long idSessao);

    Map<Long,VotoDto> buscarVotosAssociado(Long idAssociado);
    
    void removerVoto(Long votoId);
    
}