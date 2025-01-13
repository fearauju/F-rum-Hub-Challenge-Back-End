package hub.forum.api.domain.usuario.suporte;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SuporteRepository extends JpaRepository<Suporte, Long> {

    @Query("""
            SELECT u
            FROM Usuario u
            WHERE u.id = :usuarioID
            AND TYPE(u) = Suporte
            """)
    Suporte findUsuarioSuporte(Long usuarioID);

    @Query("""
            SELECT u
            FROM Usuario u
            LEFT JOIN u.perfil p
            WHERE TYPE(u) = Suporte
            ORDER BY p.nome
            """)
    Page<Suporte> listaUsuariosSuporte(Pageable paginacao);


}

