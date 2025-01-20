package hub.forum.api.domain.usuario.suporte.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DadosAvaliacaoSuporte(
        @NotNull
        Long respostaId,

        @NotNull
        @Min(1) @Max(5)
        Integer nota,

        String motivoAvaliacao
) {}