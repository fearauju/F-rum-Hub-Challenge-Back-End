package hub.forum.api.domain.escola.dto;

import hub.forum.api.domain.escola.AreaFormacao;
import hub.forum.api.domain.escola.Escola;

public record DadosListagemEscola(

        Long id,
        String nomeEscola,
        AreaFormacao areaFormacao
) {

    public DadosListagemEscola(Escola escola){
        this(escola.getId(), escola.getNomeEscola(), escola.getAreaFormacao());
    }
}
