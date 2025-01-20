package hub.forum.api.domain.usuario.professor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record DadosCadastroProfessor(

        @NotEmpty(message = "Informe pelo menos uma especialização")
        List<String> especializacoes,

        @NotBlank(message = "Informe a titularidade academica do professor")
        @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ ]+$", message = "O nome deve conter apenas letras e espaços")
        String titularidadeAcademica,

        @NotNull(message = "Os anos de experiência são obrigatórios")
        @Min(value = 0, message = "Anos de experiência não pode ser negativo")
        Integer anosExperiencia,

        @NotNull(message = "Informe a data de admissão do professor")
        @PastOrPresent(message = "A data de admissão não pode ser futura")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataAdmissao
) {
}
