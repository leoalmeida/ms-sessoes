package space.lasf.sessoes.controller;


import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import space.lasf.sessoes.domain.model.SessaoStatus;
import space.lasf.sessoes.dto.PautaDto;
import space.lasf.sessoes.dto.SessaoDto;
import space.lasf.sessoes.service.SessaoService;

/**
 * Controller para gerenciamento de sessoes.
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/v1/sessoes")
public class SessaoController {

    @Autowired
    private SessaoService sessaoService;


    @GetMapping
    public ResponseEntity<List<SessaoDto>> buscarTodasSessoes() {
        List<SessaoDto> sessoes = sessaoService.buscarTodasSessoes();
        return ResponseEntity.ok(sessoes);
    }

    @GetMapping("/{idSessao}")
    public ResponseEntity<SessaoDto> buscarSessaoPorId(
                                            @PathVariable Long idSessao) {
        return ResponseEntity.ok(sessaoService.buscarSessaoPorId(idSessao));
    }

    @GetMapping("/{idSessao}/associado/{idAssociado}")
    public ResponseEntity<SessaoDto> buscarSessaoPorId(
                                            @PathVariable Long idSessao,
                                            @PathVariable Long idAssociado) {
        return ResponseEntity.ok(sessaoService.buscarSessaoPorId(idSessao,idAssociado));
    }

    @PostMapping
    public ResponseEntity<SessaoDto> criarSessao(@RequestBody PautaDto pautaDto, 
                                                @RequestParam boolean iniciarSessao) {
        

        SessaoDto sessaoCriada = sessaoService.criarSessao(pautaDto, iniciarSessao);

        return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(sessaoCriada);
    }

    @PostMapping("/{idSessao}/iniciar")
    public ResponseEntity<SessaoDto> iniciarSessao(@PathVariable Long idSessao) {
        return ResponseEntity.ok(sessaoService.iniciarSessao(idSessao));
    }

    @PostMapping("/{idSessao}/finalizar")
    public ResponseEntity<SessaoDto> finalizarSessao(@PathVariable Long idSessao) {
        return ResponseEntity.ok(sessaoService.finalizarSessao(idSessao));
    }

    @PostMapping("/{idSessao}/cancelar")
    public ResponseEntity<Void> cancelarSessao(@PathVariable Long idSessao) {
        sessaoService.cancelarSessao(idSessao);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{idSessao}/validar")
    public ResponseEntity<Map<Long, Boolean>> validarSessao(@PathVariable Long idSessao) {
        SessaoDto dto = sessaoService.buscarSessaoPorId(idSessao);
        
        boolean isValid = SessaoStatus.OPEN_TO_VOTE.equals(dto.getStatus());

        return ResponseEntity.ok(Map.of(idSessao, isValid));
    }

}