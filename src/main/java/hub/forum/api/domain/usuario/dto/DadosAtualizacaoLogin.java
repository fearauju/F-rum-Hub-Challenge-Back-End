package hub.forum.api.domain.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DadosAtualizacaoLogin(

        @Email(message = "Formato de email inválido")
        @NotBlank(message = "Email não pode estar em branco")
        @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
        String login,

        @NotBlank(message = "Senha não pode estar em branco")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
                message = "A senha deve conter pelo menos 8 caracteres, incluindo letra maiúscula, " +
                        "minúscula, número e caractere especial"
        )
        @Size(min = 8, max = 32, message = "A senha deve ter entre 8 e 32 caracteres")
        String senha
) {
}
