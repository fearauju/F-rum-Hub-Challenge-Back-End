package hub.forum.api.domain.escola.dto;

import hub.forum.api.domain.escola.AreaFormacao;
import hub.forum.api.domain.escola.Escola;
import hub.forum.api.domain.formacao.Formacao;
import hub.forum.api.domain.formacao.dto.DadosFormacaoResumido;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record DadosDetalhamentoEscola(
        Long escolaId,
        String nomeEscola,
        AreaFormacao areaFormacao,
        Set<DadosFormacaoResumido> formacoes,
        int totalFormacoes,
        int totalCursos
) {
    public DadosDetalhamentoEscola(Escola escola, List<Formacao> formacao) {
        this(
                escola.getId(),
                escola.getNomeEscola(),
                escola.getAreaFormacao(),
                formacao == null ? Set.of() :
                        formacao.stream()
                        .map(DadosFormacaoResumido::new)
                        .collect(Collectors.toSet()),
                Objects.requireNonNull(formacao).size(),
                formacao.stream()
                        .mapToInt(f -> f.getCursos().size())
                        .sum()
        );
    }
}