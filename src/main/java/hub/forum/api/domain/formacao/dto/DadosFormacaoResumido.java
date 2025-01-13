package hub.forum.api.domain.formacao.dto;

import hub.forum.api.domain.formacao.Formacao;

public record DadosFormacaoResumido(
        Long id,
        String nome
) {
    public DadosFormacaoResumido(Formacao formacao) {
        this(formacao.getId(), formacao.getFormacao());
    }
}