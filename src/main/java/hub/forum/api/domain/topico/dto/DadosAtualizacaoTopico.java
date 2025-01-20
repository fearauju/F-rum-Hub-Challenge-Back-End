package hub.forum.api.domain.topico.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DadosAtualizacaoTopico(


        @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9_.,:()\\-\\s]+$", message = "O título deve conter apenas letras, numeros, espaços e underline")
        @Size(max = 100)
        String titulo,


        @Pattern( regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9_.,:(?=*@$!%&)\\-\\s]+$", message = "A mensagem deve conter apenas letras, numeros, espaços, caracteres especiais e underline")
        @Size(max = 255)
        String mensagem
) {
}
