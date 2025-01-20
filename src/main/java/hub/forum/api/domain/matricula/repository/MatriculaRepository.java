package hub.forum.api.domain.matricula.repository;

import hub.forum.api.domain.matricula.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<Matricula,Long> {

    @Query("""
        SELECT m
        FROM Matricula m
        JOIN m.estudante e
        LEFT JOIN FETCH m.matriculaCurso mc
        LEFT JOIN FETCH mc.curso
        WHERE e.id = :estudanteId
    """)
    Optional<Matricula> findByEstudanteIdWithCursos(Long estudanteId);

    @Modifying
    @Query(value = "UPDATE matriculas SET ativo = false WHERE id = :id", nativeQuery = true)
    void alterarStatusMatricula(Long id);
}
