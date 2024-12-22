package hub.forum.api.domain.curso;

import java.time.Duration;

public record DadosDetalhamentoCurso(

        Long formacao_id,
        String formacao,
        Long id,
        String curso,
        Duration duracao
) {

    public DadosDetalhamentoCurso(Curso curso){
        this(curso.getFormacao().getId(),curso.getFormacao().getFormacao(),
                curso.getId() ,curso.getCurso(), curso.getDuracao());
    }
}
