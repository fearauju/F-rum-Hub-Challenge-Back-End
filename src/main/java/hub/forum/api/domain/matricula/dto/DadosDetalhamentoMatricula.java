package hub.forum.api.domain.matricula.dto;

import hub.forum.api.domain.escola.AreaFormacao;
import hub.forum.api.domain.matricula.Matricula;

import java.time.LocalDateTime;

public record DadosDetalhamentoMatricula(
        Long id,
        Long numeroMatricula,
        String formacao,
        AreaFormacao areaFormacao,
        String escolaNome,
        LocalDateTime dataAssinatura,
        LocalDateTime dataExpiracao,
        boolean ativa
) {
    public DadosDetalhamentoMatricula(Matricula matricula) {
        this(
                matricula.getId(),
                matricula.getNumeroDaMatricula(),
                matricula.getFormacao().getFormacao(),
                matricula.getFormacao().getEscola().getAreaFormacao(),
                matricula.getFormacao().getEscola().getNomeEscola(),
                matricula.getDataAssinatura(),
                matricula.getDataExpiracaoAssinatura(),
                matricula.isAtiva()
        );
    }
}
