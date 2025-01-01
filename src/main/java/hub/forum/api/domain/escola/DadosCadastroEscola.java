package hub.forum.api.domain.escola;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroEscola(

        @NotNull(message = "O id do usuário é obrigatório")
        @Pattern(regexp = "^[\\d0-9]")
        Long usuario_id,

        @NotBlank(message = "Informe o nome da escola para cadastro")
        @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ\\-\\s ]+$", message = "O nome deve conter apenas letras e espaços")
        String nomeEscola
) {
}
