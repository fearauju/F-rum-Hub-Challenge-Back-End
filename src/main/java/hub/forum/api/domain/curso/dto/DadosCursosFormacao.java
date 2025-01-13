package hub.forum.api.domain.curso.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record DadosCursosFormacao(

        @NotBlank(message = "O nome do curso é obrigatório")
        String nome,

        @NotBlank(message = "A duração do curso é obrigatória")
        @Pattern(
                regexp = "^\\d{2}:\\d{2}$",
                message = "A duração deve estar no formato HH:mm, como '08:00'"
        )
        String duracao,

        List<String> nome_professor
) {
}
