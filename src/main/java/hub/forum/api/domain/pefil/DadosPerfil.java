package hub.forum.api.domain.pefil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosPerfil(

        @NotBlank
        @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ ]+$", message = "O nome deve conter apenas letras e espaços")
        String nome
) {
}