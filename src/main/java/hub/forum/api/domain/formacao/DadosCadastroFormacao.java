package hub.forum.api.domain.formacao;

import hub.forum.api.domain.curso.DadosCursosFormacao;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record DadosCadastroFormacao(

        @NotNull(message = "O id da escola é obrigatório")
        Long escolaID,

        @NotBlank(message = "O nome da formação é obrigatório")
        @Pattern(
                 regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ ]+$", message = "O nome deve conter apenas letras e espaços"
                )
        String formacao,

        @NotNull(message = "A área de formação é obrigatória e deve ser escrita com todas as letras maiúsculas")
        AreaFormacao areaFormacao,

        @NotBlank(message = "A descrição da formação é obrigatória")
        @Pattern(
                regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9.,:()\\-\\s]+$", message = "A mensagem deve conter apenas letras, numeros e espaços"
        )
        String descricao,

        @NotEmpty(message = "Pelo menos um curso deve ser adicionado")
        @Valid
        List<DadosCursosFormacao> cursos
) {
}
