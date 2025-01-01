package hub.forum.api.domain.curso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadoscadastroCurso(

        @NotNull(message = "O cursoID da formação é obrigatório")
        @Pattern(regexp = "^[\\d0-9]")
        Long formacaoID,

        @NotBlank(message = "O nome do curso é obrigatório")
        @Pattern(
                regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9_.,:()\\-\\s]+$", message = "O nome deve conter apenas letras, numeros, espaços e underline"
        )
        String curso,

        @NotBlank(message = "A duração do curso é obrigatória")
        @Pattern(
                regexp = "^\\d{2}:\\d{2}$",
                message = "A duração deve estar no formato HH:mm, como '08:00'"
        )
        String duracao
) {
}
