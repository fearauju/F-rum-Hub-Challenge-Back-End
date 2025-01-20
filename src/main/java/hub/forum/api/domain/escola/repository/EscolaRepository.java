package hub.forum.api.domain.escola.repository;


import hub.forum.api.domain.escola.Escola;
import hub.forum.api.domain.formacao.Formacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EscolaRepository extends JpaRepository<Escola,Long> {

    boolean existsByNomeEscolaIgnoreCase(String nomeEscola);

    @Query("""
            SELECT f
            FROM Formacao f
            WHERE f.escola.nomeEscola = :nomeEscola
            """)
    List<Formacao> findByFormacaoEscola(String nomeEscola);
}
