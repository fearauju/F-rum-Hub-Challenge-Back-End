package hub.forum.api.domain.usuario.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosExclusaoUsuario(

        @NotNull(message = "Informe o id do usuário que ficará inativo")
        @Pattern(regexp = "^\\d+$", message = "O ID deve conter apenas números")
        Long usuarioInativoID
) {
}
