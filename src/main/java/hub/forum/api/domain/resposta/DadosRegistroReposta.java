package hub.forum.api.domain.resposta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DadosRegistroReposta(

        @NotNull(message = "Informe o id do autor")
        Long autorID,

        @NotBlank(message = "O conteúdo é obrigatório, conte-nos qual a sua dúvida")
        @Pattern( regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9_.,:(?=*@$!%&)\\-\\s]+$", message = "A respostamensagem deve conter apenas letras, numeros, espaços, caracteres especiais e underline")
        @Size(min = 2,max = 1000)
        String resposta

) {
}
