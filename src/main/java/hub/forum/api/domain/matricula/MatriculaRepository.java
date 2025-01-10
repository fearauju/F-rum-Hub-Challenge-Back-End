package hub.forum.api.domain.matricula;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<Matricula,Long> {

    Matricula findMatriculaByEstudanteId(Long estudanteID);

    @Query("""
        SELECT m FROM Matricula m
        LEFT JOIN FETCH m.matriculaCurso mc
        LEFT JOIN FETCH mc.curso
        WHERE m.estudante.id = :estudanteId
    """)
    Optional<Matricula> findMatriculaAtivaByEstudanteId(Long estudanteID);
}
