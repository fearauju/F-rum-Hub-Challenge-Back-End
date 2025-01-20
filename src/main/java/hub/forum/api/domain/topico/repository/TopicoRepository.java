package hub.forum.api.domain.topico.repository;

import hub.forum.api.domain.topico.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico,Long> {


    Optional<Topico> findByIdAndAutorId(Long topicoId, Long autorId);

    @Query("""
    SELECT CASE 
        WHEN COUNT(t) > 0 THEN true 
        ELSE false 
    END 
    FROM Topico t 
    WHERE t.id = :topicoId 
    AND t.resolvido = true
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
    SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
    FROM Topico t
    WHERE LOWER(TRIM(t.titulo)) = :titulo
    AND LOWER(TRIM(t.mensagem)) = :mensagem
""")
    boolean existsByTituloEMensagem(String titulo, String mensagem);

    @Query("""
    SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
    FROM Topico t
    WHERE LOWER(TRIM(t.titulo)) = :titulo
    AND LOWER(TRIM(t.mensagem)) = :mensagem
    AND t.curso.formacao.id = :formacaoId
""")
    boolean existsByTituloEMensagemNaFormacao(
            String titulo,
            String mensagem,
            Long formacaoId
    );


    @Query("""
                SELECT t FROM Topico t
                LEFT JOIN FETCH t.autor a
                LEFT JOIN FETCH t.curso c
                WHERE SIZE(t.respostas) = 0
                AND t.resolvido = false
                ORDER BY t.dataCriacao DESC
            """)
    Page<Topico> listarTopicosSemRespostas(Pageable paginacao);

    @Query("""
                SELECT t FROM Topico t
                LEFT JOIN FETCH t.autor a
                LEFT JOIN FETCH t.curso c
                WHERE t.resolvido = false
                AND SIZE(t.respostas) = 0
                AND DATEDIFF(CURRENT_TIMESTAMP, t.dataCriacao) >= 1
                ORDER BY t.dataCriacao DESC
            """)
    Page<Topico> findTopicosRecentes(Pageable pageable);


    @Query("""
        SELECT COUNT(t) FROM Topico t
        WHERE SIZE(t.respostas) = 0
    """)
    Integer countTopicosSemResposta();

    @Query("""
        SELECT COUNT(t)
        FROM Topico t
        WHERE t.resolvido = false
        AND SIZE(t.respostas) = 0
        AND DATEDIFF(CURRENT_TIMESTAMP, t.dataCriacao) >= 1
    """)
    Integer countTopicosRecentes();

    @Query("""
        SELECT AVG(TIMESTAMPDIFF(HOUR, t.dataCriacao, 
            (SELECT MIN(r.dataCriacao) FROM Resposta r WHERE r.topico = t)))
        FROM Topico t 
        WHERE SIZE(t.respostas) > 0
    """)
    Double calcularTempoMedioResposta();

    @Query("""
            SELECT COUNT(t.respostas)
            FROM Topico t
            """)
    Integer countTotalRespostas();


}
