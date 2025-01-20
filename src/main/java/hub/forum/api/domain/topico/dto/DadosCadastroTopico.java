package hub.forum.api.domain.topico.dto;

import jakarta.validation.constraints.*;

public record DadosCadastroTopico(

        @NotNull(message = "O id do curso é obrigatório")
        Long cursoId,

        @NotBlank(message = "O título é obrigatório")
        @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9_.,:()\\-\\s]+$", message = "O nome deve conter apenas letras, numeros, espaços e underline")
        @Size(max = 100)
        String titulo,

        @NotBlank(message = "O conteúdo é obrigatório, conte-nos qual a sua dúvida")
        @Pattern( regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9_.,:(?=*@$!%&)\\-\\s]+$", message = "A resposta deve conter apenas letras, numeros, espaços, caracteres especiais e underline")
        @Size(max = 255)
        String mensagem
) {
}
