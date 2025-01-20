package hub.forum.api.domain.usuario.suporte.dto;

import hub.forum.api.domain.usuario.suporte.Suporte;
import hub.forum.api.domain.usuario.suporte.TurnoTrabalho;
import java.time.LocalDate;
import java.util.Set;

public record DadosDetalhamentoSuporte(
        Long id,
        String nome,
        String login,
        Set<String> especializacoes,
        Integer casosResolvidos,
        Double avaliacaoSuporte,
        TurnoTrabalho turnoTrabalho,
        LocalDate dataAdmissao
) {
    public DadosDetalhamentoSuporte(Suporte suporte) {
        this(
                suporte.getId(),
                obterNomeExibicao(suporte),
                suporte.getUsuario().getLogin(),
                suporte.getEspecializacoes() != null ?
                        suporte.getEspecializacoes() : Set.of(),
                suporte.getCasosResolvidos(),
                suporte.getAvaliacaoSuporte(),
                suporte.getTurnoTrabalho(),
                suporte.getDataAdmissao()
        );
    }

    private static String obterNomeExibicao(Suporte suporte) {
        var usuario = suporte.getUsuario();
        if (usuario.getPerfil() != null && usuario.getPerfil().getNome() != null) {
            return usuario.getPerfil().getNome();
        }
        return "[Perfil não cadastrado] " + usuario.getLogin();
    }
}