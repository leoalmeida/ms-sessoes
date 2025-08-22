package space.lasf.sessoes.controller;


import java.net.URI;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import space.lasf.sessoes.dto.VotoDto;
import space.lasf.sessoes.service.VotoService;



/**
 * Controller para gerenciamento de votos.
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/v1/votos")
public class VotosController {

    @Autowired
    private VotoService votoService;

    @GetMapping("/sessao/{idSessao}")
    public ResponseEntity<List<VotoDto>> pesquisarVotosSessao(@PathVariable Long idSessao) {
        List<VotoDto> result = votoService.buscarVotosSessao(idSessao);
        
        return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(result);
    }

    @GetMapping("/associado/{idAssociado}")
    public ResponseEntity<Map<Long,VotoDto>> pesquisarVotosAssociado(@PathVariable Long idAssociado) {
        Map<Long,VotoDto> result = votoService.buscarVotosAssociado(idAssociado);
        
        return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VotoDto> buscarVotoById(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
            .body(votoService.buscarVotoPorId(id));
    }

    @PostMapping
    public ResponseEntity<VotoDto> criarVoto(@RequestBody VotoDto votoDto, UriComponentsBuilder uriBuilder) {
        VotoDto novoVoto = votoService.criarVoto(votoDto);
        URI endereco = uriBuilder.path("/votos/{id}").buildAndExpand(novoVoto.getId()).toUri();
        return ResponseEntity.created(endereco)
                .contentType(MediaType.APPLICATION_JSON)
                .body(novoVoto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerVoto(@PathVariable Long id) {
        votoService.removerVoto(id);
        return ResponseEntity.noContent().build();
    }

}
