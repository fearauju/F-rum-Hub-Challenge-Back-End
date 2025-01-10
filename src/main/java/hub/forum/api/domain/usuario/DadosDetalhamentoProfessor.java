package hub.forum.api.domain.usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record DadosDetalhamentoProfessor(

        String nome,
        List<String> especializacoes,
        String titularidadeAcademnica,
        List<String> cursosLecionados,
        Integer totalHorasLecionadas,
        Integer anosExperiencia,
        LocalDate dataAdimissao
) {

    public DadosDetalhamentoProfessor(Professor professor){

        this(professor.getPerfil().getNome(),
                new ArrayList<>(professor.getEspecializacoes()),
                professor.getTitularidadeAcademica(),
                new ArrayList<>(professor.getCursosLecionados()),
                professor.getTotalHorasLecionadas(),
                professor.getAnosExperiencia(),
                professor.getDataDeAdmissao());
    }
}
