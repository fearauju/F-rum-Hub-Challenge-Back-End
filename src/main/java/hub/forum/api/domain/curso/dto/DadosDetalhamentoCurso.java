package hub.forum.api.domain.curso.dto;

import hub.forum.api.domain.curso.Curso;

import java.time.Duration;
import java.util.List;

public record DadosDetalhamentoCurso(
        Long formacaoId,
        String formacao,
        Long cursoId,
        String curso,
        Duration duracao,
        Double avaliacao,
        Integer totalDeAlunos,
        List<String> professores
) {
    public DadosDetalhamentoCurso(Curso curso) {
        this(
                curso.getFormacao().getId(),
                curso.getFormacao().getFormacao(),
                curso.getId(),
                curso.getCurso(),
                curso.getDuracao(),
                curso.getAvaliacao(),
                curso.getTotalDeAlunos(),
                curso.getProfessores().stream()
                        .map(prof -> prof.getUsuario().getPerfil().getNome())
                        .toList()
        );
    }
}