package space.lasf.sessoes.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;


/**
 * Entidade que representa um item de pedido.
 */
@Entity
@Table(name = "sessoes")
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Sessao {
    
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name = "pauta_id")
    private Long idPauta;

    @Column(name = "status")
    @NotNull @Enumerated(EnumType.STRING)
    private SessaoStatus status;

    @Column(name = "resultado")
    @Enumerated(EnumType.STRING)
    private VotoOpcao resultado;

    @Column(name = "data_fim")
    private LocalDateTime dataFimSessao;

    @Column(name = "data_inicio")
    private LocalDateTime dataInicioSessao;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "sessao")
    private List<TotalizadorOpcao> totalizadores;

    @Builder
    public Sessao(Long id, Long idPauta) {
        this.id = id;
        this.idPauta = idPauta;
        this.totalizadores = null;
        this.resultado = null;
        this.dataInicioSessao = null;
        this.dataFimSessao = null;
        this.status = SessaoStatus.CREATED;
    }

    public Sessao iniciarSessao(){
        if (!SessaoStatus.CREATED.equals(this.getStatus())){
            throw new IllegalArgumentException("Sessão já foi iniciada!");
        }
        this.totalizadores = new ArrayList<>();
        this.dataInicioSessao = LocalDateTime.now();
        this.status = SessaoStatus.OPEN_TO_VOTE;
        return this;
    }   

    public Sessao finalizarSessao(){
        if (!SessaoStatus.OPEN_TO_VOTE.equals(this.getStatus())){
            throw new IllegalArgumentException("Sessão não está aberta!");
        }
        this.dataFimSessao = LocalDateTime.now(); 
        this.status = SessaoStatus.CLOSED;
        return this;
    }
    
    public Sessao cancelarSessao(){
        if (!SessaoStatus.OPEN_TO_VOTE.equals(this.getStatus())){
            throw new IllegalArgumentException("Sessão não está aberta!");
        }
        this.dataFimSessao = LocalDateTime.now(); 
        this.status = SessaoStatus.CANCELLED;
        return this;
    }

    public Sessao totalizarVotos(List<Voto> votos) {
        if (null==votos){
            throw new IllegalArgumentException("Sessão não foi iniciada corretamente!");
        }
        if (!SessaoStatus.OPEN_TO_VOTE.equals(this.getStatus())){
            throw new IllegalArgumentException("Sessão não está aberta para votação!");
        }
        this.totalizadores = new ArrayList<>();
        for (VotoOpcao opcao : VotoOpcao.values()) {
            Long contagem = votos.stream()
                .filter(v -> v.getOpcao().equals(opcao))
                .count();
            this.totalizadores.add(TotalizadorOpcao.builder()
                                                .opcaoVoto(opcao)
                                                .quantidade(contagem)
                                                .build());
        }

        this.resultado = this.totalizadores.stream()
                .max((o1, o2) -> o1.getQuantidade().compareTo(o2.getQuantidade()))
                .get().getOpcaoVoto();

        return this;
    }

}
