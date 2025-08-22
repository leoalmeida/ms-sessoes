package space.lasf.sessoes.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import space.lasf.sessoes.domain.model.Sessao;
import space.lasf.sessoes.domain.model.SessaoStatus;
import space.lasf.sessoes.domain.repository.SessaoRepository;
import space.lasf.sessoes.domain.repository.VotoRepository;
import space.lasf.sessoes.dto.PautaDto;
import space.lasf.sessoes.dto.SessaoDto;
import space.lasf.sessoes.dto.VotoDto;
import space.lasf.sessoes.service.SessaoService;

/**
 * Implementação do serviço para gerenciamento de sessões.
 */
@Service
@RequiredArgsConstructor
public class SessaoServiceImpl implements SessaoService {

    @Autowired
    private final VotoRepository votacaoRepository;

    @Autowired
    private final SessaoRepository sessaoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public SessaoDto criarSessao(PautaDto pautaDto, boolean iniciarSessao) {
        
        if (pautaDto == null || pautaDto.getId() == null ) {
            throw new IllegalArgumentException("ID da pauta não pode ser nula.");
        }
        
        Sessao entityIn = Sessao.builder()
                .idPauta(pautaDto.getId())
                .build();

        if (iniciarSessao) {
            entityIn.iniciarSessao();
        } else {
            entityIn.setStatus(SessaoStatus.CREATED);
        }
        
        SessaoDto dtoOut = modelMapper.map(sessaoRepository.save(entityIn), SessaoDto.class);
        dtoOut.setPautaDto(pautaDto);
        return dtoOut;
    }

    @Override
    public boolean isSessaoAbertaParaVotacao(Long id) {
        Sessao sessao = sessaoRepository.existSessaoOnStatus(SessaoStatus.OPEN_TO_VOTE, id);
        return (null != sessao && sessao.getId().compareTo(id)==0);
    }

    @Override
    public SessaoDto buscarSessaoPorId(Long idSessao, Long idAssociado){
        SessaoDto result = sessaoRepository.findById(idSessao)
                    .map(entitySession -> modelMapper.map(entitySession, SessaoDto.class))
                    .orElseThrow(() -> new EntityNotFoundException());
        VotoDto votoAssociado = votacaoRepository
                .findVotosByIdSessaoAndIdAssociado(idSessao, idAssociado)
                .map(entityVoto -> modelMapper.map(entityVoto,VotoDto.class))
                .orElse(null);
        result.setVotoAssociado(votoAssociado);
        return result;
    }

    @Override
    public SessaoDto buscarSessaoPorId(Long id) {
        Sessao result = sessaoRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException());
        return modelMapper.map(result, SessaoDto.class);
    }

    @Override
    public List<SessaoDto> buscarTodasSessoes() {
        return sessaoRepository.findAll().stream()
                .map(p -> modelMapper.map(p, SessaoDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public SessaoDto buscarResultadoDeVotacao(Long idSessao){
        if (idSessao == null) {
            throw new IllegalArgumentException("ID da sessao inválido: " + idSessao);
        }
        Sessao sessao = sessaoRepository.findById(idSessao)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada com o ID: " + idSessao));

        if (sessao.getStatus().equals(SessaoStatus.CLOSED)){
            return modelMapper.map(sessao, SessaoDto.class);
        } else {
            throw new IllegalStateException("Sessão aind não finalizada. Status atual: " + sessao.getStatus());
        }
    }

    @Override
    @Transactional
    public SessaoDto iniciarSessao(Long idSessao) {
        // Verifica se sessão existe
        Sessao sessaoAlterada = sessaoRepository.findById(idSessao)
                    .orElseThrow(() -> new EntityNotFoundException())
                    .iniciarSessao();
      
        // Caso sessão seja encontrada, 
        //      inicializar a sessão e atualizar registro 
        Sessao sessaoIniciada = sessaoRepository.save(sessaoAlterada);
        return modelMapper.map(sessaoIniciada, SessaoDto.class);
    }

    @Override
    @Transactional
    public SessaoDto finalizarSessao(Long idSessao) {
        // Verifica se sessão existe
        Sessao sessaoAlterada = sessaoRepository.findById(idSessao)
                    .orElseThrow(() -> new EntityNotFoundException("Sessao não encontrado com o id: " + idSessao))
                    .finalizarSessao()
                    .totalizarVotos(votacaoRepository.findVotosByIdSessao(idSessao));

        // Caso sessão seja encontrada, 
        //      finalize a sessão e atualiza registro 
        Sessao sessaoIniciada = sessaoRepository.save(sessaoAlterada);
        return modelMapper.map(sessaoIniciada, SessaoDto.class);
    }

    @Override
    @Transactional
    public void cancelarSessao(Long idSessao) {
        // Verifica se sessão existe
        Sessao sessaoCancelada = sessaoRepository.findById(idSessao)
            .orElseThrow(() -> new IllegalArgumentException("Sessao não encontrado com o id: " + idSessao))
            .cancelarSessao();
            // Caso sessão seja encontrada, 
            //      cancele a sessão e atualiza registro 
        sessaoRepository.save(sessaoCancelada);
    }


}
