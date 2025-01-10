package hub.forum.api.domain.perfil.validacao;

import java.time.LocalDate;

public record DadosValidacaoPerfil(
        String nome,
        LocalDate dataNascimento,
        String descricaoPessoal,
        Long usuarioID
) {}