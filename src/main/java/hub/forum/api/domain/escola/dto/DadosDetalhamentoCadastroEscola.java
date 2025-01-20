package hub.forum.api.domain.escola.dto;

import hub.forum.api.domain.escola.AreaFormacao;
import hub.forum.api.domain.escola.Escola;

public record DadosDetalhamentoCadastroEscola(
        Long escolaId,
        String nomeEscola,
        AreaFormacao areaFormacao
) {

    public DadosDetalhamentoCadastroEscola(Escola escola){
        this(escola.getId(), escola.getNomeEscola(), escola.getAreaFormacao());
    }
}
