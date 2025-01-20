package hub.forum.api.domain.matricula.dto;

import java.time.LocalDateTime;

public record DadosProgressoCurso(
        Long cursoId,
        String nomeCurso,
        double progresso,
        LocalDateTime ultimoAcesso
) {}