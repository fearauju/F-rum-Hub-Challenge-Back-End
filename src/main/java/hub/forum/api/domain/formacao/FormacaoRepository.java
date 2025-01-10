package hub.forum.api.domain.formacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FormacaoRepository extends JpaRepository<Formacao,Long> {

    boolean existsByFormacaoIgnoreCase(String formacao);

    @Query("""
        SELECT f FROM Formacao f
        LEFT JOIN FETCH f.cursos
        WHERE f.id = :id
        """)
    Optional<Formacao> findByIdWithCursos(Long id);

    Formacao findByFormacao(String formacao);
}
