package hub.forum.api.domain.usuario.estudante.dto;

import hub.forum.api.domain.usuario.estudante.Badge;
import java.time.LocalDateTime;

public record DadosDetalhamentoBadges(
        String nome,
        LocalDateTime dataConquista,
        String descricao
) {

    public DadosDetalhamentoBadges(Badge badge) {
        this(
                badge.getBadge(),
                badge.getDataConquista(),
                badge.getDescricao()
        );
    }
}
