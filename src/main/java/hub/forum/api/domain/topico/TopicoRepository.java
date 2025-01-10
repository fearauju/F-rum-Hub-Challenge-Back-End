package hub.forum.api.domain.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        AND t.resolvido = false
        """)
    boolean topicoResolvido(Long topicoId);

    @Query("""
            SELECT t
            FROM Topico t
            WHERE t.resolvido = false
            """)
    Page<Topico> topicosNaoResolvidos(Pageable paginacao);

    @Query("""
            SELECT t
            FROM Topico t
            WHERE t.resolvido = true
            """)
    Page<Topico> topicosResolvidos(Pageable paginacao);


    @Query("""
            SELECT COUNT(t.id)
            FROM Topico t
            WHERE t.resolvido = false
            """)
    Integer totalTopicosNaoResolvidos();


    @Query("""
            SELECT COUNT(t.id)
            FROM Topico t
            WHERE t.resolvido = true
            """)
    Integer totalTopicosResolvidos();


    @Query("""
            SELECT t
            FROM Topico t
            WHERE COUNT(t.resposta) = 0
            AND t.resolvido = false
            ORDER BY t.dataCriacao desc
            """)
    Page<Topico> listarTopicosSemRespostas(Pageable paginacao);
}
