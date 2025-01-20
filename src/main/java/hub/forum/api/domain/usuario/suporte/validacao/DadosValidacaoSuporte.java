package hub.forum.api.domain.usuario.suporte.validacao;

import hub.forum.api.domain.usuario.suporte.TurnoTrabalho;

import java.time.LocalDate;
import java.util.Set;

public record DadosValidacaoSuporte(
        Long usuarioId,
        Set<String> especializacoes,
        TurnoTrabalho turnoTrabalho,
        LocalDate dataAdmissao
) {}