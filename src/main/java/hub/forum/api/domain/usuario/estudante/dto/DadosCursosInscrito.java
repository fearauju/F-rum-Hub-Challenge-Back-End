package hub.forum.api.domain.usuario.estudante.dto;

import hub.forum.api.domain.curso.Curso;

import java.time.LocalDateTime;

public record DadosCursosInscrito(
        Curso curso,
        LocalDateTime dataInscricao,
        LocalDateTime dataConclusao,
        boolean concluido
) {
}
