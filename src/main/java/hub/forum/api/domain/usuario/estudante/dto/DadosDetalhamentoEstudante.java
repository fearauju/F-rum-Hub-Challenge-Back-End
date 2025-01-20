package hub.forum.api.domain.usuario.estudante.dto;

import hub.forum.api.domain.matricula.dto.DadosCursoMatriculado;
import hub.forum.api.domain.matricula.dto.DadosDetalhamentoMatricula;
import hub.forum.api.domain.usuario.estudante.Estudante;
import hub.forum.api.domain.usuario.estudante.NivelAcademico;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record DadosDetalhamentoEstudante(
        Long id,
        String nome,
        String login,
        Integer anoIngresso,
        Set<String> interessesAcademicos,
        NivelAcademico nivelAcademico,
        BigDecimal pontuacao,
        Set<DadosCursoMatriculado> cursos,
        Integer cargaHorariaConcluida,
        Integer cursosConcluidos,
        Double mediaAvaliacoes,
        Integer certificadosEmitidos,
        Set<DadosDetalhamentoBadges> badgesConquistadas,
        List<DadosDetalhamentoMatricula> matriculas
) {
    public DadosDetalhamentoEstudante(Estudante estudante) {
        this(
                estudante.getId(),
                obterNomeExibicao(estudante),
                estudante.getUsuario().getLogin(),
                estudante.getAnoIngresso(),
                estudante.getInteressesAcademicos(),
                estudante.getNivelAcademico(),
                estudante.getPontuacao(),
                obterCursosMatriculados(estudante),
                estudante.getCargaHorariaConcluida(),
                estudante.getCursosConcluidos(),
                estudante.getMediaAvaliacoes(),
                estudante.getCertificadosEmitidos(),
                obterBadgesConquistadas(estudante),
                obterMatriculas(estudante)
        );
    }

    private static Set<DadosCursoMatriculado> obterCursosMatriculados(Estudante estudante) {
        return estudante.getMatriculas().stream()
                .flatMap(matricula -> matricula.getMatriculaCurso().stream())
                .map(DadosCursoMatriculado::new)
                .collect(Collectors.toSet());
    }

    private static String obterNomeExibicao(Estudante estudante) {
        var usuario = estudante.getUsuario();
        if (usuario.getPerfil() != null && usuario.getPerfil().getNome() != null) {
            return usuario.getPerfil().getNome();
        }
        return "[Perfil não cadastrado] " + usuario.getLogin();
    }

    private static Set<DadosDetalhamentoBadges> obterBadgesConquistadas(Estudante estudante) {
        if (estudante.getBadgesConquistadas() == null) {
            return Set.of();
        }
        return estudante.getBadgesConquistadas().stream()
                .map(DadosDetalhamentoBadges::new)
                .collect(Collectors.toSet());
    }

    private static List<DadosDetalhamentoMatricula> obterMatriculas(Estudante estudante) {
        return estudante.getMatriculas().stream()
                .map(DadosDetalhamentoMatricula::new)
                .collect(Collectors.toList());
    }
}