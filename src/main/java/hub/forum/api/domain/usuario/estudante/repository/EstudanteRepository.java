package hub.forum.api.domain.usuario.estudante.repository;

import hub.forum.api.domain.usuario.estudante.Estudante;
import hub.forum.api.domain.usuario.estudante.dto.DadosDetalhamentoBadges;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.Set;

public interface EstudanteRepository extends JpaRepository<Estudante,Long> {

    @Query("""
            SELECT e
            FROM Estudante e
            JOIN e.usuario u
            LEFT JOIN u.perfil p
            WHERE u.tipoUsuario = ESTUDANTE
            AND u.ativo = true
            ORDER BY p.nome
            """)
    Page<Estudante> listarEstudantesMatriculados(Pageable paginacao);

    @Query("""
            SELECT e
            FROM Estudante e
            JOIN e.usuario u
            WHERE u.id = :usuarioId
            AND u.ativo = true
            """)
    Estudante findByEstudante(Long usuarioId);


    @Query("""
            SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END
            FROM Estudante e
            JOIN e.matriculas m
            JOIN e.usuario u
            WHERE e.id = :estudanteId
            AND u.ativo = true
            """)
    boolean existsByEstudanteMatriculado(Long estudanteId);

    @Query("""
            SELECT badgesConquistadas e
            FROM Estudante e
            WHERE e.id = :estudanteId
            """)
    Set<DadosDetalhamentoBadges> findAllConquistas(Long estudanteId);

    @Query("""
        SELECT e FROM Estudante e
        WHERE e.usuario.id = :usuarioId
    """)
    Optional<Estudante> findByUsuarioId(Long usuarioId);

    @Query("""
        SELECT e FROM Estudante e
        JOIN FETCH e.matriculas m
        JOIN FETCH m.formacao f
        WHERE e.id = :estudanteId
        AND m.ativa = true
    """)
    Optional<Estudante> findByIdWithMatriculaAtiva(Long estudanteId);
}
