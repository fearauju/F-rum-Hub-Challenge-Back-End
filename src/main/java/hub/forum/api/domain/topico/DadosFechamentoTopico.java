package hub.forum.api.domain.topico;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosFechamentoTopico(

        @NotNull(message = "O id do usuário é obrigatório")
        @Pattern(regexp = "^\\d+$", message = "O ID deve conter apenas números")
        Long usuarioID,

        @NotNull(message = "Informe uma nota para o suporte")
        @DecimalMin(value = "0.0", message = "A avaliação deve ser maior ou igual a 0")
        @DecimalMax(value = "10.0", message = "A avaliação deve ser menor ou igual a 10")
        Double avaliacaoSuporte,

        @Pattern(
                regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9.,:()\\-\\s]+$", message = "A mensagem deve conter apenas letras, numeros e espaços"
        )
        String motivoAvaliacao
) {
}
