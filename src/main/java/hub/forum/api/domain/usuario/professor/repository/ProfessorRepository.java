package hub.forum.api.domain.usuario.professor.repository;

import hub.forum.api.domain.usuario.professor.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;


public interface ProfessorRepository extends JpaRepository<Professor,Long> {


    @Query("""
            SELECT u
            FROM Usuario u
            WHERE u.id = :usuarioId
            AND u.tipoUsuario = PROFESSOR
            AND u.ativo = true
            """)
    Professor findUsuarioProfessorPorId(Long usuarioId);

    List<Professor> findByUsuarioPerfilNomeIn(List<String> nomes);

    @Query("""
       SELECT p
       FROM Professor p
       JOIN p.usuario u
       LEFT JOIN u.perfil pf
       WHERE u.ativo = true
       ORDER BY COALESCE(pf.nome, u.login)
       """)
    Page<Professor> listarusuariosProfessores(Pageable paginacao);

    @Query("""    
    SELECT DISTINCT p
    FROM Professor p
    JOIN p.usuario u
    JOIN u.perfil perfil
    LEFT JOIN FETCH p.cursosLecionados
    WHERE perfil.nome IN :nomes
    AND u.ativo = true
    """)
    List<Professor> findByNomesIn(Collection<String> nomes);

}
