package hub.forum.api.domain.usuario;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosExclusaoUsuario(

        @NotNull(message = "O cursoID do usuário é obrigatório")
        @Pattern(regexp = "^[\\d0-9]")
        Long usuario_id,

        @NotNull(message = "Informe se o tipo do usuário cadastrado com todas as letras maiúsculas")
        Long usuarioInativo_id
) {
}
