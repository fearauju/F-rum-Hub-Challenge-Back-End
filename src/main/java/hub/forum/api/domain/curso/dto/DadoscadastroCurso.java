package hub.forum.api.domain.curso.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record DadoscadastroCurso(

        @NotBlank(message = "O nome do curso é obrigatório")
        @Pattern(
                regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9.,:()\\-\\s]+$", message = "O nome deve conter apenas letras, numeros e espaços"
        )
        String curso,

        @NotBlank(message = "A duração do curso é obrigatória")
        @Pattern(
                regexp = "^\\d{2}:\\d{2}$",
                message = "A duração deve estar no formato HH:mm, como '08:00'"
        )
        String duracao,
        @NotEmpty(message = "É necessário informar o nome do professor do curso")
        List<String> professoresNomes
) {
}
