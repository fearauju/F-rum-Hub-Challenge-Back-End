package hub.forum.api.domain.usuario.professor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor,Long> {

    @Query("SELECT p FROM Professor p LEFT JOIN FETCH p.perfil WHERE p.id = :id")
    Optional<Professor> findByIdWithPerfil(Long id);

    @Query("SELECT p FROM Professor p " +
            "LEFT JOIN FETCH p.perfil " +
            "LEFT JOIN FETCH p.cursos " +
            "LEFT JOIN FETCH p.especializacoes " +
            "WHERE p.id = :id")
    Optional<Professor> findByIdWithAllAssociations(Long id);

    @Query("""
            SELECT u
            FROM Usuario u
            WHERE u.id = :usuarioID
            AND TYPE(u) = Professor
            """)
    Professor findUsuarioProfessor(Long usuarioID);

    @Query("""
            SELECT u
            FROM Usuario u
            LEFT JOIN u.perfil p
            WHERE TYPE(u) = Professor
            ORDER BY p.nome
            """)
    Page<Professor> listarusuariosProfessores(Pageable paginacao);

    @Query("""
    SELECT DISTINCT u
    FROM Usuario u
    LEFT JOIN FETCH u.perfil p
    WHERE TYPE(u) = Professor
    AND p.nome IN :nomes
    AND u.ativo = true
    """)
    List<Professor> findByNomesIn(Collection<String> nomes);

    // Método adicional para debug
    @Query("SELECT p.nome FROM Usuario u JOIN u.perfil p WHERE TYPE(u) = Professor")
    List<String> findAllProfessorNames();

    @Query("""
        SELECT DISTINCT p FROM Professor p
        JOIN FETCH p.perfil perf
        WHERE p.ativo = true
        AND perf.nome IN :nomes
    """)
    List<Professor> findByNomesOptimized(List<String> nomes);
}
