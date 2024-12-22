package hub.forum.api.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record DadosAutenticacao(

        @NotNull(message = "O e-mail é obrigatório")
        @Email(message = "Informe um email válido")
        String login,

        @NotNull(message = "A senha é obrigatória")
        String senha
) {
}
