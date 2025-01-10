package hub.forum.api.domain.formacao;

public record DadosFormacaoResumido(
        Long id,
        String nome
) {
    public DadosFormacaoResumido(Formacao formacao) {
        this(formacao.getId(), formacao.getFormacao());
    }
}