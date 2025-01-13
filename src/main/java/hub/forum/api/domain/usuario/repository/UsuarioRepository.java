package hub.forum.api.domain.usuario.repository;

import hub.forum.api.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    Usuario findByIdAndAtivoTrue(Long usuarioID);

    Usuario findByLoginAndAtivoTrue(String login);
}
