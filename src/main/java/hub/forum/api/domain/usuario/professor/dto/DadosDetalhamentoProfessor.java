package hub.forum.api.domain.usuario.professor.dto;

import hub.forum.api.domain.curso.dto.DadosCursoResumido;
import hub.forum.api.domain.usuario.professor.Professor;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;


public record DadosDetalhamentoProfessor(
        Long id,
        String nome,
        String login,
        LocalDate dataAdmissao,
        Set<String> especializacoes,
        String titularidadeAcademica,
        Integer anosExperiencia,
        Set<DadosCursoResumido> cursosLecionados,
        Long totalHorasLecionadas
) {
    public DadosDetalhamentoProfessor(Professor professor) {
        this(
                professor.getId(),
                obterNomeExibicao(professor),
                professor.getUsuario().getLogin(),
                professor.getDataDeAdmissao(),
                professor.getEspecializacoes() != null ?
                        professor.getEspecializacoes() : Set.of(),
                professor.getTitularidadeAcademica(),
                professor.getAnosExperiencia(),
                obterCursosLecionados(professor),
                professor.getTotalHorasLecionadas()
        );
    }

    private static String obterNomeExibicao(Professor professor) {
        var usuario = professor.getUsuario();
        if (usuario.getPerfil() != null && usuario.getPerfil().getNome() != null) {
            return usuario.getPerfil().getNome();
        }
        return "[Perfil não cadastrado] " + usuario.getLogin();
    }

    private static Set<DadosCursoResumido> obterCursosLecionados(Professor professor) {
        if (professor.getCursosLecionados() == null) {
            return Set.of();
        }
        return professor.getCursosLecionados().stream()
                .map(DadosCursoResumido::new)
                .collect(Collectors.toSet());
    }
}