package hub.forum.api.domain.usuario.professor;

import java.util.Set;


public record DadosDetalhamentoProfessor(

        Long id,
        String nome,
        Set<String> especializacoes,
        Set<String> cursosLecionados,
        Long totalHorasLecionadas,
        String titularidadeAcademica
) {
    public DadosDetalhamentoProfessor(Professor professor) {
        this(
                professor.getId(),
                professor.getPerfil().getNome(),
                professor.getEspecializacoes(),
                professor.getCursosLecionados(),
                professor.getTotalHorasLecionadas(),
                professor.getTitularidadeAcademica()
        );
    }
}
