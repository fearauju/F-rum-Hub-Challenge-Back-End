package hub.forum.api.domain.resposta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface RespostaRepository extends JpaRepository<Resposta,Long> {


    Optional<Resposta> findByIdAndTopicoId(Long respostaID, Long topicoID);

    @Query("""
            SELECT r
            FROM Resposta r
            WHERE r.topico.id = :topicoID
            """)
    Page<Resposta> findRespostasTopicoID(Long topicoID, Pageable paginacao);

}
