package hub.forum.api.domain.matricula;

import java.time.LocalDateTime;
import java.util.List;

public record DadosDetalhamentoMatricula(

        Long estudanteID,
        String nomeEstudante,
        Long numeroMatricula,
        LocalDateTime dataAssinaturaMatricula,
        LocalDateTime dataExpiracaoMatricula,
        List<DadosCursoMatriculado> cursos

) {
    public DadosDetalhamentoMatricula(Matricula matricula) {
        this(matricula.getEstudante().getId(), matricula.getEstudante().getPerfil().getNome(),
                matricula.getNumeroMatricula(), matricula.getDataAssinatura(),
                matricula.getDataExpiracaoAssinatura(), matricula.getMatriculaCurso().stream()
                        .map(DadosCursoMatriculado::new)
                        .toList());
    }
}
