package hub.forum.api.domain.usuario.dto;

import jakarta.validation.constraints.*;

public record DadosAtualizacaoLogin(

        @Email(message = "Formato de email inválido")
        @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
        String login,

        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
                message = "A senha deve conter pelo menos 8 caracteres, incluindo letra maiúscula, " +
                        "minúscula, número e caractere especial"
        )
        @Size(min = 8, max = 32, message = "A senha deve ter entre 8 e 32 caracteres")
        String senha
) {
}
