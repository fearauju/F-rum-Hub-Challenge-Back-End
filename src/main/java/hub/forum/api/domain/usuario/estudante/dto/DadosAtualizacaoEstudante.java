package hub.forum.api.domain.usuario.estudante.dto;

import hub.forum.api.domain.usuario.estudante.NivelAcademico;


import java.util.Set;

public record DadosAtualizacaoEstudante(

        Set<String> interessesAcademicos,

        NivelAcademico nivelAcademico
) {}