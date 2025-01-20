package hub.forum.api.domain.curso.repository;

import hub.forum.api.domain.curso.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.Set;


public interface CursoRepository extends JpaRepository<Curso,Long> {

    @Query("""
        SELECT c
        FROM Curso c
        WHERE c.formacao.id = :formacaoId
        """)
    Page<Curso> findByFormacaoId(
            Long formacaoId,
            Pageable paginacao
    );


    @Query("""
        SELECT c
        FROM Curso c
        JOIN c.formacao f
        WHERE c.id = :cursoId
        AND f.id = :formacaoId
        """)
    Optional<Curso> findByIdAndFormacaoId(Long cursoId, Long formacaoId);

    @Query("""
    SELECT CASE WHEN COUNT(c) = :totalProfessores THEN true ELSE false END
    FROM Curso c
    JOIN c.professores p
    JOIN p.usuario u
    WHERE c.id = :cursoId
    AND p.id IN :professorIds
    AND u.ativo = true
    """)
    boolean existsByIdAndProfessores(
            Long cursoId,
            Set<Long> professorIds,
            long totalProfessores);


    @Query("""
        SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
        FROM Curso c
        JOIN c.professores p
        WHERE c.id = :cursoId
        AND p.id = :professorId
        """)
    boolean existsByIdAndProfessorId(Long cursoId, Long professorId); // trabalhar com coleção de professores

    @Query("""
    SELECT c FROM Curso c
    JOIN FETCH c.formacao f
    WHERE c.id = :cursoId
""")
    Optional<Curso> findByIdWithFormacao(Long cursoId);
}
