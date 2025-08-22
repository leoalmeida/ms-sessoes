package space.lasf.sessoes.domain.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import space.lasf.sessoes.domain.model.Sessao;
import space.lasf.sessoes.domain.model.SessaoStatus;

/**
 * Repositório para a entidade Sessao.
 */
@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Long> {
    @Query("select s from Sessao s where s.status = ?1 and s.id = ?2")
    Sessao existSessaoOnStatus(SessaoStatus status, Long idSessao);
}
