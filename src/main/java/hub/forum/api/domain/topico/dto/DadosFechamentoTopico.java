package hub.forum.api.domain.topico.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosFechamentoTopico(

        @NotNull(message = "Informe uma nota para o suporte")
        @DecimalMin(value = "0.0", message = "A avaliação deve ser maior ou igual a 0")
        @DecimalMax(value = "5.0", message = "A avaliação deve ser menor ou igual a 5")
        Double avaliacaoSuporte,

        @Pattern(
                regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9.,:()\\-\\s]+$", message = "A mensagem deve conter apenas letras, numeros e espaços"
        )
        String motivoAvaliacao
) {
}
