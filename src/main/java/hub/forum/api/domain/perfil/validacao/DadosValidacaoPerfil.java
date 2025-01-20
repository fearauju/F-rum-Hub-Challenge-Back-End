package hub.forum.api.domain.perfil.validacao;

import hub.forum.api.domain.usuario.Usuario;

public record DadosValidacaoPerfil(
        String nome,
        String descricaoPessoal,
        Long usuarioId,
        Usuario usuarioLogado
) {}