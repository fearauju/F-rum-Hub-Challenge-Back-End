package hub.forum.api.domain.usuario;

import hub.forum.api.domain.pefil.DadosPerfil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record DadosCadastroUsuario(

        @NotBlank
        @Size(max = 10, message = "O login deve ter entre 5 e 20 caracteres")
        @Pattern(
                regexp = "^[a-zA-Z0-9_]+$",
                message = "O login deve conter apenas letras, números e underscores (_)"
        )
        String login,


        @NotBlank
        @Email(message = "O email deve ser válido")
        String email,

        @NotBlank
        @Size(min = 8, max = 32, message = "A senha deve conter entre 8 e 32 caracteres")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,32}$",
                message = "A senha deve conter pelo menos uma letra maiúscula," +
                          " uma letra minúscula, um número e um caractere especial"
        )
        String senha,

        @NotNull
        @Valid
        DadosPerfil perfil
) {
}
