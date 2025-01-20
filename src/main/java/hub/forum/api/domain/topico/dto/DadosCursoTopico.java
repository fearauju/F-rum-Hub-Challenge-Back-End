package hub.forum.api.domain.topico.dto;

import hub.forum.api.domain.curso.Curso;

//criar records auxiliares en vez de tentar desserializar objetos completos
//Evitamos loops de serialização
//Retornamos apenas os dados necessários

public record DadosCursoTopico(
        Long id,
        String nome,
        String formacao,
        String areaFormacao
) {
    public DadosCursoTopico(Curso curso) {
        this(
                curso.getId(),
                curso.getCurso(),
                curso.getFormacao().getFormacao(),
                curso.getFormacao().getEscola().getAreaFormacao().toString()
        );
    }
}
