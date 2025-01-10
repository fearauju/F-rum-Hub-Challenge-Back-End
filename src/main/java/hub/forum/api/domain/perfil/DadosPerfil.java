package hub.forum.api.domain.perfil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosPerfil(

        @NotBlank(message = "Informe o nome do perfil do usuário")
        @Pattern(
                regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ ]+$", message = "O nome deve conter apenas letras e espaços"
        )
        String nomePerfil
) { }
