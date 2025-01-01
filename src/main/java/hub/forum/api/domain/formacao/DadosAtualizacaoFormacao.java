package hub.forum.api.domain.formacao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosAtualizacaoFormacao(

        @NotNull(message = "O id do usuário que faz a requisição é obrigatório")
        Long usuario_id,

        @NotNull(message = "A área de formação é obrigatória e deve ser escrita com todas as letras maiúsculas")
        AreaFormacao areaFormacao,

        @Pattern(
                regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ ]+$", message = "O nome deve conter apenas letras e espaços"
        )
        String formacao,

        @Pattern(
                regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9_.,:()\\-\\s]+$", message = "A descrição deve conter apenas letras, numeros, espaços e underline"
        )
        String descricao
) {
}
