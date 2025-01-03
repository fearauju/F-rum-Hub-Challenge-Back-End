package hub.forum.api.domain.formacao;

import hub.forum.api.domain.curso.DadosCursosFormacao;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record DadosCadastroFormacao(

        @NotNull(message = "O cursoID da escola é obrigatório")
        @Pattern(regexp = "^[\\d0-9]")
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
                regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9_.,:()\\-\\s]+$", message = "A descrição deve conter apenas letras, numeros, espaços e underline"
        )
        String descricao,

        @NotEmpty(message = "Pelo menos um curso deve ser adicionado")
        @Valid
        List<DadosCursosFormacao> cursos
) {
}
