package hub.forum.api.domain.topico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DadosAtualizacaoTopico(

        @NotNull(message = "O id do usuário é obriatório")
        Long usuarioID,

        @NotBlank(message = "O título é obrigatório")
        @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9_.,:()\\-\\s]+$", message = "O nome deve conter apenas letras, numeros, espaços e underline")
        @Size(max = 100)
        String titulo,

        @NotBlank(message = "O conteúdo é obrigatório, conte-nos qual a sua dúvida")
        @Pattern( regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9_.,:(?=*@$!%&)\\-\\s]+$", message = "A resposta deve conter apenas letras, numeros, espaços, caracteres especiais e underline")
        @Size(max = 1000)
        String mensagem
) {
}
