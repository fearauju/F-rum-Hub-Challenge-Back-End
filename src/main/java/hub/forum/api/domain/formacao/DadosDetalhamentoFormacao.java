package hub.forum.api.domain.formacao;

public record DadosDetalhamentoFormacao(
        Long escola_id,
        String formacao,
        AreaFormacao areaFormacao
) {

    public DadosDetalhamentoFormacao(Formacao formacao){
        this(formacao.getEscola().getId(), formacao.getFormacao(), formacao.getAreaFormacao());
    }
}
