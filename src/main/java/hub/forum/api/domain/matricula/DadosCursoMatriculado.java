package hub.forum.api.domain.matricula;

import java.time.LocalDateTime;

public record DadosCursoMatriculado(
        Long cursoId,
        String nomeCurso,
        LocalDateTime dataInscricao,
        LocalDateTime dataConclusao,
        boolean concluido
) {
    // Construtor que recebe MatriculaCurso
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
