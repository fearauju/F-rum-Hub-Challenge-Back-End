package hub.forum.api.domain.usuario.suporte.dto;

import hub.forum.api.domain.usuario.suporte.TurnoTrabalho;

import java.util.Set;

public record DadosAtualizacaoSuporte(

        Set<String> especializacoes,

        TurnoTrabalho turnoTrabalho
) {
}
