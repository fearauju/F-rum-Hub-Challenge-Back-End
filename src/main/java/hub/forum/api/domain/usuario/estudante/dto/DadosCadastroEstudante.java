package hub.forum.api.domain.usuario.estudante.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import hub.forum.api.domain.escola.AreaFormacao;
import hub.forum.api.domain.usuario.estudante.NivelAcademico;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Set;

public record DadosCadastroEstudante(


        @NotBlank(message = "Informe um nome para seu perfil")
        @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ ]+$", message = "O nome deve conter apenas letras e espaços")
        String nome,

        @Pattern(
                regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9_.,:()\\-\\s]+$", message = "A descrição deve conter apenas letras, números, espaços e underline"
        )
        @Size(max = 1000, message = "A descrição não pode exceder 1000 caracteres")
        String descricaoPessoal,

        @NotNull(message = "Informe a data de nascimento")
        @Past(message = "A data de nascimento deve estar no passado")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento,

        @NotNull(message = "A área de formação é obrigatória e deve ser escrita com todas as letras maiúsculas")
        AreaFormacao areaFormacao,

        @NotEmpty(message = "Informe pelo menos um interesse acadêmico")
        Set<@NotBlank(message = "O interesse acadêmico não pode ser vazio") String> interessesAcademicos,

        @NotNull(message = "O nível acadêmico é obrigatório")
        @JsonProperty("nivel_academico")
        NivelAcademico nivelAcademico
) {
}
