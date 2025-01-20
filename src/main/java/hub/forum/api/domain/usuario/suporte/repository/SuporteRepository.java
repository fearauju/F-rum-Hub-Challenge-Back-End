package hub.forum.api.domain.usuario.suporte.repository;

import hub.forum.api.domain.usuario.suporte.Suporte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SuporteRepository extends JpaRepository<Suporte, Long> {

    @Query("""
            SELECT s
            FROM Suporte s
            JOIN s.usuario u
            WHERE u.id = :usuarioId
            AND u.tipoUsuario = SUPORTE
            AND u.ativo = true
            """)
    Suporte findUsuarioSuporte(Long usuarioId);

    @Query("""
            SELECT s
            FROM Suporte s
            JOIN s.usuario u
            LEFT JOIN u.perfil p
            WHERE u.ativo = true
            ORDER BY p.nome NULLS LAST
            """)
    Page<Suporte> listaUsuariosSuporte(Pageable paginacao);


}

