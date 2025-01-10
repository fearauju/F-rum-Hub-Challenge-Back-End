package hub.forum.api.domain.usuario;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    Usuario findByIdAndAtivoTrue(Long usuarioID);



    @Query("""
            SELECT u
            FROM Usuario u
            LEFT JOIN FETCH u.perfil
            WHERE u.id = :id""")
    Optional<Usuario> findByIdWithPerfil( Long id);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("""
            SELECT u
            FROM Usuario u
            LEFT JOIN FETCH u.perfil p
            WHERE u.login = :login""")
    Optional<Usuario> findByLogin(String login);

    @Query("""
    SELECT u FROM Usuario u
    WHERE u.id = :usuarioInativoID
    AND u.ativo = true
    """)
    Optional<Usuario> buscarUsuarioParaInativar(Long usuarioInativoID);


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
            WHERE u.id = :usuarioID
            AND TYPE(u) = Professor
            """)
    Professor findUsuarioProfessor(Long usuarioID);

    @Query("""
            SELECT u
            FROM Usuario u
            LEFT JOIN u.perfil p
            WHERE TYPE(u) = Suporte
            ORDER BY p.nome
            """)
    Page<Suporte> listaUsuariosSuporte(Pageable paginacao);


    @Query("""
            SELECT u
            FROM Usuario u
            LEFT JOIN u.perfil p
            WHERE TYPE(u) = Professor
            ORDER BY p.nome
            """)
    Page<Professor> listarusuariosProfessores(Pageable paginacao);

    @Query("""
            SELECT u
            FROM Usuario u
            LEFT JOIN u.perfil p
            LEFT JOIN Estudante es
            WHERE TYPE(u) = Estudante
            ORDER BY p.nome
            """)
    Page<Estudante> listarEstudantesMatriculados(Pageable paginacao);

    boolean existsByLoginIgnoreCaseAndIdNot(String login, Long usuarioID);

    @Query("""
            SELECT u
             FROM Usuario u
             LEFT JOIN FETCH u.perfil p
             WHERE TYPE(u) = Professor
             AND p.nome = :nomes
            """
            )
    List<Professor> findByNomeIn(List<String> nomes);

    boolean existsByLogin(String login);


    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE u.login = :login AND u.id != :id")
    boolean existsByLoginAndIdNot(String login, Long id);


}
