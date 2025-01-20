package hub.forum.api.domain.resposta.dto;

import hub.forum.api.domain.resposta.Resposta;
import hub.forum.api.domain.usuario.Usuario;

import java.time.LocalDateTime;

public record DadosDetalhamentoResposta(
        Long id,
        String autor,
        String login,
        String resposta,
        LocalDateTime dataCriacao,
        boolean solucao
) {
    public DadosDetalhamentoResposta(Resposta resposta) {
        this(
                resposta.getId(),
                obterNomeExibicao(resposta.getAutor()),
                resposta.getAutor().getLogin(),
                resposta.getResposta(),
                resposta.getDataCriacao(),
                resposta.isMelhorResposta()
        );
    }

    private static String obterNomeExibicao(Usuario autor) {
        if (autor.getPerfil() != null && autor.getPerfil().getNome() != null) {
            return autor.getPerfil().getNome();
        }
        return "[Perfil não cadastrado] " + autor.getLogin();
    }
}