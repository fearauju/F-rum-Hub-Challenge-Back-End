package hub.forum.api.domain.topico;

import jakarta.validation.constraints.*;

public record DadosCadastroTopico(

        @NotNull(message = "O cursoID do usuário é obrigatório")
        @Pattern(regexp = "^[\\d0-9]")
        Long usuarioID,

        @NotBlank(message = "O nome da formação é obrigatório")
        @Pattern(
                regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ ]+$", message = "O nome deve conter apenas letras e espaços"
        )
        String formacao,

        @NotNull(message = "O cursoID do usuário é obrigatório")
        @Pattern(regexp = "^[\\d0-9]")
        Long cursoID,

        @NotBlank(message = "O título é obrigatório")
        @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9_.,:()\\-\\s]+$", message = "O nome deve conter apenas letras, numeros, espaços e underline")
        @Size(min = 20, max = 100)
        String titulo,

        @NotBlank(message = "O conteúdo é obrigatório, conte-nos qual a sua dúvida")
        @Pattern( regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9_.,:(?=*@$!%&)\\-\\s]+$", message = "A resposta deve conter apenas letras, numeros, espaços, caracteres especiais e underline")
        @Size(min = 100, max = 1000)
        String mensagem
) {
}
