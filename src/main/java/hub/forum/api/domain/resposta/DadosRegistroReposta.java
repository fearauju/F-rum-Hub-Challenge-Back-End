package hub.forum.api.domain.resposta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DadosRegistroReposta(

        @NotNull(message = "Informe o id do autor")
        @Pattern(regexp = "^\\d+$", message = "O ID deve conter apenas números")
        Long autorID,

        @NotBlank(message = "Informe a sua solução para a dúvida deste tópico")
        @Pattern( regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9_.,:(?=*@$!%&)\\-\\s]+$", message = "A resposta deve conter apenas letras, numeros, espaços, caracteres especiais e underline")
        @Size(min = 2,max = 1000)
        String resposta

) {
}
