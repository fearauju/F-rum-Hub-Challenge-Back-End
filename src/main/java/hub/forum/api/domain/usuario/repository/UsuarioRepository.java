package hub.forum.api.domain.usuario.repository;

import hub.forum.api.domain.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    Usuario findByIdAndAtivoTrue(Long usuarioID);

    Usuario findByLoginAndAtivoTrue(String login);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.perfil WHERE u.id = :id")
    Optional<Usuario> findByIdWithPerfil(Long id);

    Usuario findByIdAndAtivoFalse(Long usuarioInativoId);

    Usuario findByLogin(String login);

    boolean existsByLogin(String login);

    @Query("""
    SELECT u FROM Usuario u
    LEFT JOIN u.perfil p
    WHERE u.ativo = false
    ORDER BY p.nome NULLS LAST
    """)
    Page<Usuario> findAllByAtivoFalse(Pageable paginacao);
}
