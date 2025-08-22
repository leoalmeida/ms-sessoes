package space.lasf.sessoes.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import space.lasf.sessoes.dto.PautaDto;


@FeignClient("ms-pautas")
public interface PautaClient {

    @GetMapping("/v1/pautas/{id}")
    PautaDto consultarPauta(@PathVariable Long id);

    @PostMapping("/v1/pautas")
    PautaDto criarPauta(@RequestBody PautaDto dto);

}
