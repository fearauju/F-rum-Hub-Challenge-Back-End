package hub.forum.api.domain.perfil.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record DadosCadastroPerfil(

        @NotBlank(message = "Informe um nome para seu perfil")
        @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ ]+$", message = "O nome deve conter apenas letras e espaços")
        String nome,

        @Pattern(
                regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9_.,:()\\-\\s]+$", message = "A descrição deve conter apenas letras, numeros, espaços e underline"
        )
        @Size(max = 1000)
        String descricaoPessoal,

        @NotNull(message = "Informe a data de nascimento")
        @Past(message = "A data de nascimento deve estar no passado")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento
) {
}
