package hub.forum.api.domain.escola;

import hub.forum.api.domain.formacao.dto.DadosFormacaoResumido;

import java.util.List;

public record DadosDetalhamentoEscola(
        Long escolaId,
        String nomeEscola,
        List<DadosFormacaoResumido> formacoes,
        int totalFormacoes,
        int totalCursos
) {
    public DadosDetalhamentoEscola(Escola escola) {
        this(
                escola.getId(),
                escola.getNomeEscola(),
                escola.getFormacao().stream()
                        .map(DadosFormacaoResumido::new)
                        .toList(),
                escola.getFormacao().size(),
                escola.getFormacao().stream()
                        .mapToInt(f -> f.getCursos().size())
                        .sum()
        );
    }
}