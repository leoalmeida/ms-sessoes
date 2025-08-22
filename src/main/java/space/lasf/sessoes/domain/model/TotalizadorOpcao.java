package space.lasf.sessoes.domain.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "totalizadores")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalizadorOpcao {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @ManyToOne(optional=false)
    private Sessao sessao;

    @Column(name = "opcao")
    private VotoOpcao opcaoVoto;
    private Long quantidade;
}
