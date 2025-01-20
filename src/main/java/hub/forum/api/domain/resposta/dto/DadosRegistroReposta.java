package hub.forum.api.domain.resposta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DadosRegistroReposta(
        @NotBlank(message = "{resposta.obrigatoria}")
        @Pattern(
                regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9_.,:(?=*@$!%&)\\-\\s]+$",
                message = "{resposta.caracteres.invalidos}"
        )
        @Size(min = 2, max = 1000, message = "{resposta.tamanho}")
        String resposta
) {}
