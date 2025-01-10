package hub.forum.api.domain.matricula.validacao;

import java.time.LocalDateTime;

public record DadosValidacaoMatricula(
        Long estudanteId,
        Long cursoId,
        LocalDateTime dataAssinatura,
        LocalDateTime dataExpiracao
) {}
