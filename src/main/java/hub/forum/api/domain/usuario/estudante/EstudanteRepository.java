package hub.forum.api.domain.usuario.estudante;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EstudanteRepository extends JpaRepository<Estudante,Long> {

    @Query("""
            SELECT u
            FROM Usuario u
            LEFT JOIN u.perfil p
            LEFT JOIN Estudante es
            WHERE TYPE(u) = Estudante
            ORDER BY p.nome
            """)
    Page<Estudante> listarEstudantesMatriculados(Pageable paginacao);
}
