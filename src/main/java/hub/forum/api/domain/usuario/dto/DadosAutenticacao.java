package hub.forum.api.domain.usuario.dto;

import jakarta.validation.constraints.*;

public record DadosAutenticacao(

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$",
                message = "Formato de email inválido")
        @Size(min = 3, max = 20,
                message = "Email deve ter entre 5 e 100 caracteres")
        String login,

        @NotBlank(message = "A senha é obrigatória")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
                message = "A senha deve conter pelo menos 8 caracteres, incluindo letra maiúscula, " +
                        "minúscula, número e caractere especial"
        )
        @Size(min = 8,max = 32, message = "A senha deve conter no mínimo 8 caracteres e máximo 32 caracteres")
        String senha
) {}
