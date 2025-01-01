package hub.forum.api.domain.curso;

import java.time.Duration;

public record DadosListagemCurso(
        Long id,
        String curso,
        Duration duracao,
        String formacao,
        Integer totalDeAlunos,
        Double avaliacao
) {
    public DadosListagemCurso(Curso dados) {
        this(dados.getId(),
                dados.getCurso(),
                dados.getDuracao(),
                dados.getFormacao().getFormacao(),
                dados.getTotalDeAlunos(),
                dados.getAvaliacao());
    }
}