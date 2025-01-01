package hub.forum.api.domain.topico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico,Long> {


    Optional<Topico> findByIdAndAutorId(Long topicoId, Long autorId);


    @Query("""
            SELECT t.titulo, t.mensagem
            FROM Topico t
            WHERE t.titulo = :titulo
            OR t.mensagem = :mensagem
            """)

    boolean existsByTituloOrMensagem(String titulo, String mensagem);


    @Query("""
        SELECT t.resolvido
        FROM Topico t
        WHERE t.id = :topicoId
        """)
    boolean isTopicoResolvido(Long topicoId);
}
