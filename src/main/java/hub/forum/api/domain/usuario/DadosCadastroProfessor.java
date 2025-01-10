package hub.forum.api.domain.usuario;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

public record DadosCadastroProfessor(

        @NotEmpty(message = "Informe pelo menos uma especialização")
        List<String> especializacoes,

        @NotBlank(message = "Informe a titularidade academica do professor")
        @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ ]+$", message = "O nome deve conter apenas letras e espaços")
        String titularidade_academica,

        List<String> cursos_lecionados,
        Integer anos_experiencia,

        @NotNull(message = "Informe a data de admissão do professor")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data_admissao
) {
}
