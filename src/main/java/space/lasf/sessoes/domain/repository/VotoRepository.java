package space.lasf.sessoes.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import space.lasf.sessoes.domain.model.Voto;

/**
 * Repositório para a entidade Voto.
 */
@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

    @Query("select v from Voto v where v.idSessao = ?1")
    List<Voto> findVotosByIdSessao(Long idSessao);

    @Query("select v from Voto v where v.idAssociado = ?1")
    Stream<Voto> findVotosByIdAssociado(Long idAssociado);

    @Query("select v from Voto v where v.idSessao = ?1 AND v.idAssociado = ?2")
    Optional<Voto> findVotosByIdSessaoAndIdAssociado(Long idSessao, Long idAssociado);
}
