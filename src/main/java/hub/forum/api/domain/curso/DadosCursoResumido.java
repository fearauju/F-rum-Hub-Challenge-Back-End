package hub.forum.api.domain.curso;

import java.time.Duration;

public record DadosCursoResumido(
        Long id,
        String nome,
        Duration duracao
) {
    public DadosCursoResumido(Curso curso) {
        this(curso.getId(), curso.getCurso(), curso.getDuracao());
    }
}