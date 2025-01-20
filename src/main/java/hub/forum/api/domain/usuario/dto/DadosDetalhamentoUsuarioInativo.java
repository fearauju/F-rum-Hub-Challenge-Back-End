package hub.forum.api.domain.usuario.dto;

import hub.forum.api.domain.usuario.Usuario;

public record DadosDetalhamentoUsuarioInativo(
        Long id,
        String login,
        String nome
) {
    public DadosDetalhamentoUsuarioInativo(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getLogin(),
                obterNomeExibicao(usuario)
        );
    }

    private static String obterNomeExibicao(Usuario usuario) {
        if (usuario.getPerfil() != null && usuario.getPerfil().getNome() != null) {
            return usuario.getPerfil().getNome();
        }
        return "[Perfil não cadastrado] " + usuario.getLogin();
    }
}