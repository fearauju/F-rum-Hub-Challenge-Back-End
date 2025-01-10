package hub.forum.api.domain.formacao;

import hub.forum.api.domain.curso.DadosCursoResumido;

import java.util.List;

public record DadosDetalhamentoFormacao(
        Long id,
        Long escolaId,
        String formacao,
        String descricao,
        AreaFormacao areaFormacao,
        List<DadosCursoResumido> cursos
) {
    public DadosDetalhamentoFormacao(Formacao formacao) {
        this(
                formacao.getId(),
                formacao.getEscola().getId(),
                formacao.getFormacao(),
                formacao.getDescricao(),
                formacao.getAreaFormacao(),
                formacao.getCursos().stream()
                        .map(DadosCursoResumido::new)
                        .toList()
        );
    }
}
