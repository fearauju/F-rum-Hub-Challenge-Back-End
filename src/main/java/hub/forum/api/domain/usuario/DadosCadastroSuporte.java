package hub.forum.api.domain.usuario;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.List;

public record DadosCadastroSuporte(

        @NotEmpty(message = "Pelo menos um curso deve ser adicionado")
        List<String> especializacoes,
        @Pattern(
                regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ]+$", message = "O nome deve conter apenas letras"
        )
        String turno_de_trabalho,

        @NotNull(message = "Informe a data de admissão do funcionário")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data_admissao
) {
}
