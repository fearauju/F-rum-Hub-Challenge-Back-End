package hub.forum.api.domain.usuario.estudante;

import java.math.BigDecimal;
import java.util.List;

public record DadosDetalhamentoEstudante(
        String nome,
        List<String> interessesAcademicos,
        List<String> cursosInscrito,
        Integer cargaHorariaConcluida,
        BigDecimal pontuacao

) {

    public DadosDetalhamentoEstudante(Estudante estudante){

        this(estudante.getPerfil().getNome(), estudante.getInteressesAcademicos(),
                estudante.getCursosInscrito(), estudante.getCargaHorariaConcluida(),
                estudante.getPontuacao());
    }
}
