package hub.forum.api.domain.formacao.dto;

import hub.forum.api.domain.escola.AreaFormacao;
import hub.forum.api.domain.formacao.Formacao;

public record DadosListagemFormacao(

        String nomeEscola,
        Long formacaoID,
        AreaFormacao areaFormacao,
        String formacao,
        String descricao

) {

    public DadosListagemFormacao(Formacao formacao){
        this(formacao.getEscola().getNomeEscola(),
                formacao.getId(), formacao.getEscola().getAreaFormacao(),
                formacao.getFormacao(), formacao.getDescricao());
    }
}
