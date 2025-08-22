package space.lasf.sessoes.service.impl;



import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import space.lasf.sessoes.core.exception.BusinessException;
import space.lasf.sessoes.core.util.ObjectsValidator;
import space.lasf.sessoes.domain.model.Voto;
import space.lasf.sessoes.dto.VotoDto;
import space.lasf.sessoes.domain.repository.VotoRepository;
import space.lasf.sessoes.service.SessaoService;
import space.lasf.sessoes.service.VotoService;

/**
 * Implementação do serviço para gerenciamento de votos.
 */
@Service
@RequiredArgsConstructor
public class VotoServiceImpl implements VotoService {

    @Autowired
    private ObjectsValidator<Voto> validadorDeVoto;

    @Autowired
    private VotoRepository repository;

    @Autowired
    private SessaoService sessaoService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public VotoDto criarVoto(VotoDto dto) {
        Voto entity = modelMapper.map(dto,Voto.class);
        System.out.println("Voto: " + entity.toString());
        entity.setDataVoto(LocalDateTime.now());
        validadorDeVoto.validate(entity);// Valida a voto antes de salvar
        System.out.println("Verifica Sessao: " + entity.getIdSessao());
        if (sessaoService.isSessaoAbertaParaVotacao(entity.getIdSessao())){
            Voto result = repository.save(entity);
            return modelMapper.map(result, VotoDto.class);
        } else {
            throw new BusinessException("Sessão encerrada para votação.");
        }

    }

    @Override
    public VotoDto buscarVotoPorId(Long id) {
        Voto result = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException());
        return modelMapper.map(result, VotoDto.class);
    }

    @Override
    public List<VotoDto> buscarVotosSessao(Long idSessao) {
        return repository.findVotosByIdSessao(idSessao).stream()
                .map(p -> modelMapper.map(p, VotoDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Map<Long,VotoDto> buscarVotosAssociado(Long idAssociado) {
        return repository.findVotosByIdAssociado(idAssociado)
                    .map(p -> modelMapper.map(p, VotoDto.class))
                    .collect(Collectors.toMap(VotoDto::getIdSessao, Function.identity()));
    }

    @Override
    @Transactional
    public void removerVoto(Long votoId) {
        if (repository.findById(votoId).isPresent()) {
            repository.deleteById(votoId);
        }else {
            throw new EntityNotFoundException("Voto não encontrado com ID: " + votoId);
        }
    }
    
}