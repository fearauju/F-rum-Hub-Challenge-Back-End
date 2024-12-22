package hub.forum.api.domain.escola;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record DadosCadastroEscola(

        @NotBlank
        @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ\\-\\s ]+$", message = "O nome deve conter apenas letras e espaços")
        String nomeEscola
) {
}
