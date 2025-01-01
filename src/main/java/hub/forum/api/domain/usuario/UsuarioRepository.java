package hub.forum.api.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    @Query("SELECT u FROM Usuario u WHERE u.login = :login")
    Optional<Usuario> findByLogin(String login);

    @Query("SELECT u FROM Usuario u WHERE u.cursoID = :cursoID AND TYPE(u) IN (Professor, Suporte)")
    Optional<Usuario> findInativableUserById(@Param("id") Long id);

    @Modifying
    @Query("""
        UPDATE Usuario u
        SET u.ativo = :ativo
        WHERE u.cursoID = :cursoID
        AND TYPE(u) IN (Professor, Suporte)
        """)
    void updateStatusAtivo(@Param("id") Long id, @Param("ativo") boolean ativo);

    @Modifying
    @Query("""
        UPDATE Usuario u
        SET u.tentativasLogin = :tentativas,
            u.contaBloqueada = :tentativas >= 6
        WHERE u.login = :login
        """)
    void atualizarTentativasLogin(@Param("login") String login,
                                  @Param("tentativas") Integer tentativas);

    @Modifying
    @Query("""
        UPDATE Usuario u
        SET u.tentativasLogin = :tentativas,
            u.contaBloqueada = false,
            u.ultimoLogin = :ultimoLogin
        WHERE u.login = :login
        """)
    void atualizarLoginSucesso(@Param("login") String login,
                               @Param("tentativas") Integer tentativas,
                               @Param("ultimoLogin") LocalDateTime ultimoLogin);
}
