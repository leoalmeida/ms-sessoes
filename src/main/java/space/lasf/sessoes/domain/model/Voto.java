package space.lasf.sessoes.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Entidade que representa um pedido.
 */
@Entity
@Table(name = "votos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Voto {

    @Serial
    private static final long serialVersionUID = 1L;
 
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name = "associado_id")
    @NotNull
    private Long idAssociado;

    @Column(name = "sessao_id")
    @NotNull
    private Long idSessao;

    @Column(name = "opcao")
    @NotNull 
    @Enumerated(EnumType.STRING)
    private VotoOpcao opcao;

    @Column(name = "data_voto")
    @NotNull
    private LocalDateTime dataVoto;

}