package hub.forum.api.domain.usuario.estudante.dto;

import java.time.LocalDateTime;

public record DadosProgressoEstudante(
        Integer cargaHorariaConcluida,
        Integer cursosConcluidos,
        Double mediaAvaliacoes,
        Integer totalBadges,
        LocalDateTime ultimaAtividade
) {}