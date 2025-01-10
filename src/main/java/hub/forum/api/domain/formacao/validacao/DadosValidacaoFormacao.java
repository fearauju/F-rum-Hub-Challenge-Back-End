package hub.forum.api.domain.formacao.validacao;

import hub.forum.api.domain.curso.DadosCursosFormacao;

import java.util.List;

public record DadosValidacaoFormacao(
        Long escolaId,
        String formacao,
        List<DadosCursosFormacao> cursos
) {}