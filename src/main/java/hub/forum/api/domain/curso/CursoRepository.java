package hub.forum.api.domain.curso;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CursoRepository extends JpaRepository<Curso,Long> {

    @Query("""
        SELECT c FROM Curso c
        WHERE c.formacao.id = :formacaoId
        """)
    Page<Curso> findByFormacaoId(
            @Param("formacaoId") Long formacaoId,
            Pageable paginacao
    );

    @Query("""
            SELECT u Curso u
            WHERE U.formacao.id = :formacao_id
            AND u.curso.id = :curso_id
            """)
    boolean existsByIdINFormacao(Long formacao_id, Long curso_id);

        @Query("""
        SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
        FROM Curso c
        WHERE c.id = :cursoId
        AND c.professor.id = :professorId
        """)
        boolean existsByIdAndProfessorId(
                @Param("cursoId") Long cursoId,
                @Param("professorId") Long professorId
        );
}
