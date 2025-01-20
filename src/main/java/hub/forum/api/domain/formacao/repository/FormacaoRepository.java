package hub.forum.api.domain.formacao.repository;

import hub.forum.api.domain.escola.AreaFormacao;
import hub.forum.api.domain.formacao.Formacao;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FormacaoRepository extends JpaRepository<Formacao,Long> {

    boolean existsByFormacaoIgnoreCase(String formacao);

    Optional<Formacao> findByFormacaoAndEscolaId(String formacao, Long escolaId);

    @Query("""
        SELECT DISTINCT f FROM Formacao f
        LEFT JOIN FETCH f.escola e
        ORDER BY e.areaFormacao, f.formacao
    """)
    List<Formacao> findAllFormacoesWithEscolas();

    Page<Formacao> findByEscolaId(Long escolaId, Pageable paginacao);

    @Query("SELECT f FROM Formacao f JOIN FETCH f.escola e WHERE e.areaFormacao = :area")
    List<Formacao> findByEscolaAreaFormacao(AreaFormacao area);
}

