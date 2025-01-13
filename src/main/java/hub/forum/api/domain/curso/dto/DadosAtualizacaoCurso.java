package hub.forum.api.domain.curso.dto;

import jakarta.validation.constraints.Pattern;

public record DadosAtualizacaoCurso(

        @Pattern(
                regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9_.,:()\\-\\s]+$", message = "O nome deve conter apenas letras, numeros, espaços e underline"
        )
        String nomeCurso,


        @Pattern(
                regexp = "^\\d{2}:\\d{2}$",
                message = "A duração deve estar no formato HH:mm, como '08:00'"
        )
        String duracao

) {
}
