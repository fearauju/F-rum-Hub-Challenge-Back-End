package hub.forum.api.domain.formacao;

public record DadosListagemFormacao(

        String nomeEscola,
        Long formacaoID,
        AreaFormacao areaFormacao,
        String formacao,
        String descricao

) {

    public DadosListagemFormacao(Formacao formacao){
        this(formacao.getEscola().getNomeEscola(),
                formacao.getId(), formacao.getAreaFormacao(),
                formacao.getFormacao(), formacao.getDescricao());
    }
}
