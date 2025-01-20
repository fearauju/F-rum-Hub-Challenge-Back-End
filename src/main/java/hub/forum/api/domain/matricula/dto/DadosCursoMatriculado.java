package hub.forum.api.domain.matricula.dto;

import hub.forum.api.domain.matricula.MatriculaCurso;
import java.time.LocalDateTime;

public record DadosCursoMatriculado(
        Long cursoId,
        String nomeCurso,
        LocalDateTime dataInscricao,
        LocalDateTime dataConclusao,
        boolean concluido
) {

    public DadosCursoMatriculado(MatriculaCurso matriculaCurso) {
        this(
                matriculaCurso.getCurso().getId(),
                matriculaCurso.getCurso().getCurso(),
                matriculaCurso.getDataInscricao(),
                matriculaCurso.getDataConclusao(),
                matriculaCurso.isConcluido()
        );
    }
}
