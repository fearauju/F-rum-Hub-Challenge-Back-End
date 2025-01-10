package hub.forum.api.domain.usuario;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosDesbloquearConta(

        @NotNull(message = "Informe o id do usuário com a conta bloqueada")
        @Pattern(regexp = "^\\d+$", message = "O ID deve conter apenas números")
        Long usuarioID
) {
}
