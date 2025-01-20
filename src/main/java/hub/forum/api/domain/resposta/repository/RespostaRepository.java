package hub.forum.api.domain.resposta.repository;

import hub.forum.api.domain.resposta.Resposta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface RespostaRepository extends JpaRepository<Resposta,Long> {


    Optional<Resposta> findByIdAndTopicoId(Long respostaid, Long topicoid);

    @Query("""
    SELECT r FROM Resposta r
    LEFT JOIN FETCH r.autor a
    LEFT JOIN FETCH a.perfil p
    WHERE r.topico.id = :topicoid
    """)
    Page<Resposta> findRespostasTopicoId(Long topicoid, Pageable paginacao);

}
