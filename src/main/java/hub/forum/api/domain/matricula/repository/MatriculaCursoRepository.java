package hub.forum.api.domain.matricula.repository;

import hub.forum.api.domain.matricula.MatriculaCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MatriculaCursoRepository extends JpaRepository<MatriculaCurso, Long> {

    @Query("""
        SELECT mc FROM MatriculaCurso mc
        JOIN FETCH mc.matricula m
        JOIN FETCH mc.curso c
        WHERE m.estudante.id = :estudanteId
    """)
    List<MatriculaCurso> findByEstudanteId(Long estudanteId);

    // Buscar cursos concluídos
    List<MatriculaCurso> findByConcluido(boolean concluido);

    // Verificar inscrição existente
    boolean existsByMatriculaIdAndCursoId(Long matriculaId, Long cursoId);

    Optional<MatriculaCurso> findByMatriculaIdAndCursoId(Long matriculaId, Long cursoId);

    Collection<MatriculaCurso> findByMatriculaId(Long matriculaId);

    Optional<MatriculaCurso> findByMatriculaEstudanteIdAndCursoId(Long estudanteId, Long cursoId);
}
