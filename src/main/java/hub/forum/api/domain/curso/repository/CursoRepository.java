package hub.forum.api.domain.curso.repository;

import hub.forum.api.domain.curso.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface CursoRepository extends JpaRepository<Curso,Long> {

    @Query("""
        SELECT c
        FROM Curso c
        WHERE c.formacao.id = :formacaoID
        """)
    Page<Curso> findByFormacaoId(
            @Param("formacaoId") Long formacaoID,
            Pageable paginacao
    );

        @Query("""
        SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
        FROM Curso c
        JOIN Usuario u
        WHERE c.id = :cursoID
        AND u.id =: professorID
        AND TYPE(u) = Professor
        """)
        boolean existsByIdAndProfessorId(
                 Long cursoID,
                 Long professorID
        );


    boolean existsByCursoIgnoreCase(String curso);

    @Query("""
        SELECT c FROM Curso c
        WHERE c.id = :cursoId AND c.formacao.id = :formacaoId
        """)
    Optional<Curso> findByIdAndFormacaoId(Long cursoId, Long formacaoId);

    boolean existsByIdAndFormacaoId(Long cursoID, Long formacaoID);

    Curso findByCurso(String curso);
}
