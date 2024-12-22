package hub.forum.api.domain.pefil;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record DadosPerfil(

        @NotBlank(message = "O nome é obrigatório")
        @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ ]+$", message = "O nome deve conter apenas letras e espaços")
        String nome,

        @NotNull(message = "A data de nascimento é obrigatória")
        @Past(message = "A data de nascimento deve estar no passado")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento,

        @NotNull(message = "O tipo de perfil é obrigatório e deve ser escrito em letras maiúsculas")
        TipoPerfil tipoPerfil
) {
}
