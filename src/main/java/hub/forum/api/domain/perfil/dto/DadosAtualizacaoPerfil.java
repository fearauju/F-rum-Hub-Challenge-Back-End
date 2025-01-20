package hub.forum.api.domain.perfil.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record DadosAtualizacaoPerfil(

        @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ ]+$", message = "O nome deve conter apenas letras e espaços")
        String nome,

        @Pattern(
                regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9_.,:()\\-\\s]+$", message = "A descrição deve conter apenas letras, numeros, espaços e underline"
        )
        @Size(max = 1000)
        String descricaoPessoal
) {
}
