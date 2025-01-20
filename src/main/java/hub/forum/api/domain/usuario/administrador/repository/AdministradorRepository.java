package hub.forum.api.domain.usuario.administrador.repository;

import hub.forum.api.domain.usuario.administrador.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

    @Query("""
            SELECT a
            FROM Administrador a
            JOIN a.usuario u
            WHERE u.id = :usuarioId
            """)
    Administrador findByUsuarioAdministrador(Long usuarioId);
}
