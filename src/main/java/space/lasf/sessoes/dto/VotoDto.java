package space.lasf.sessoes.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import space.lasf.sessoes.domain.model.VotoOpcao;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VotoDto {

    private Long id;
    private Long idAssociado;
    private Long idSessao;
    private VotoOpcao opcao;
    private LocalDateTime dataVoto;

}
