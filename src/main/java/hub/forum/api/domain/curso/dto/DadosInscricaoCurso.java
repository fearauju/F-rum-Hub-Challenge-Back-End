package hub.forum.api.domain.curso.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosInscricaoCurso(

        @NotNull(message = "O id do estudante é obrigatório")
        @Pattern(regexp = "^[\\d0-9]")
        Long estudanteID,

        @NotBlank(message = "O id do curso é obrigatório")
        @Pattern(
                regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9.,:()\\-\\s]+$", message = "O nome deve conter apenas letras, numeros espaços"
        )
        Long cursoID
) {
}
