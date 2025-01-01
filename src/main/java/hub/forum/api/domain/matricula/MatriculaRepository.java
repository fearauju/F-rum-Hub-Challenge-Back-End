package hub.forum.api.domain.matricula;


import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface MatriculaRepository extends JpaRepository<Matricula,Long> {

    Matricula findMatriculaByEstudanteId(Long estudanteId);

    DadosDetalhamentoMatricula UpdateDataAssinaturaById(LocalDateTime hoje, Long estudanteId);
}
