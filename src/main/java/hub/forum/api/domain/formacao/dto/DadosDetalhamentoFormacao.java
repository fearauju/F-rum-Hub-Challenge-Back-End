package hub.forum.api.domain.formacao.dto;

import hub.forum.api.domain.curso.dto.DadosCursoResumido;
import hub.forum.api.domain.escola.AreaFormacao;
import hub.forum.api.domain.formacao.Formacao;
import java.util.List;

public record DadosDetalhamentoFormacao(
        Long id,
        String nomeEscola,
        String formacao,
        String descricao,
        AreaFormacao areaFormacao,
        List<DadosCursoResumido> cursos
) {
    public DadosDetalhamentoFormacao(Formacao formacao) {
        this(
                formacao.getId(),
                obterNomeEscola(formacao),
                formacao.getFormacao(),
                formacao.getDescricao(),
                formacao.getEscola().getAreaFormacao(),
                obterCursos(formacao)
        );
    }

    private static String obterNomeEscola(Formacao formacao) {
        return formacao.getEscola() != null ?
                formacao.getEscola().getNomeEscola() :
                "[Escola não encontrada]";
    }

    private static List<DadosCursoResumido> obterCursos(Formacao formacao) {
        if (formacao.getCursos() == null) {
            return List.of();
        }
        return formacao.getCursos().stream()
                .map(DadosCursoResumido::new)
                .toList();
    }
}