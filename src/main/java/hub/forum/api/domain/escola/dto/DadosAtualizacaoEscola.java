package hub.forum.api.domain.escola.dto;

import hub.forum.api.domain.escola.AreaFormacao;
import jakarta.validation.constraints.Pattern;

public record DadosAtualizacaoEscola(

        @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ\\-\\s ]+$", message = "O nome deve conter apenas letras e espaços")
        String nomeEscola,

        AreaFormacao areaFormacao
) {
}
