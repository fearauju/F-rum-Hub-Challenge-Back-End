package hub.forum.api.domain.usuario.professor.dto;

import jakarta.validation.constraints.Pattern;

import java.util.Set;

public record DadosAtualizacaoProfessor(

        Set<String> especializacoes,

        @Pattern(regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ ]+$", message = "O nome deve conter apenas letras e espaços")
        String titularidadeAcademica
) {
}
