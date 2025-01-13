package hub.forum.api.domain.formacao.repository;

import hub.forum.api.domain.formacao.Formacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormacaoRepository extends JpaRepository<Formacao,Long> {

    boolean existsByFormacaoIgnoreCase(String formacao);

}
