package hub.forum.api.domain.matricula;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosEstudanteMatriculado(

        @NotNull(message = "Informe cursoID do usuário que fará a atualização da matricula")
        @Pattern(regexp = "^[\\d0-9]")
        Long usuario_Id,

        @NotNull(message = "O cursoID do estudante é obrigatório")
        @Pattern(regexp = "^[\\d0-9]")
        Long estudante_Id
) {
}
