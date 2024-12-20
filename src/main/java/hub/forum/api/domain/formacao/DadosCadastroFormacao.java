package hub.forum.api.domain.formacao;

import hub.forum.api.domain.curso.DadosCursosFormacao;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record DadosCadastroFormacao(

        @NotNull(message = "O id da escola é obrigatório")
        Long escola_id,

        @NotBlank(message = "O nome da formação é obrigatório")
        @Pattern(
                 regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ]+$", message = "O nome deve conter apenas letras e espaços"
                )
        String formacao,

        @NotNull(message = "A área de formação é obrigatória")
        AreaFormacao areaFormacao,

        @NotBlank(message = "A descrição da formação é obrigatória")
        @Pattern(
                regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9_ ]+$", message = "O nome deve conter apenas letras, numeros, espaços e underline"
        )
        String descricao,

        @NotNull(message = "Pelo menos um curso deve ser adicionado")
        @Valid
        List<DadosCursosFormacao> curso
) {
}
