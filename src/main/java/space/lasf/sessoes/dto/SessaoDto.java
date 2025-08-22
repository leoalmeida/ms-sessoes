package space.lasf.sessoes.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import space.lasf.sessoes.domain.model.SessaoStatus;
import space.lasf.sessoes.domain.model.TotalizadorOpcao;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessaoDto {

    private Long id;
    private SessaoStatus status;
    private String resultado;
    @Singular(value = "totalizadores")
    List<TotalizadorOpcao> totalizadores;
    private LocalDateTime dataFimSessao;
    private LocalDateTime dataInicioSessao;
    private PautaDto pautaDto;
    private VotoDto votoAssociado;

}
