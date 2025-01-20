package hub.forum.api.domain.perfil.dto;

import hub.forum.api.domain.perfil.Perfil;

import java.time.LocalDate;


public record DadosDetalhamentoPerfil(

        Long id,
        String nome,
        LocalDate dataNascimento,
        String descricao
) {

    public DadosDetalhamentoPerfil(Perfil perfil){
        this(perfil.getUsuario().getId(), perfil.getNome(), perfil.getDataNascimento(),perfil.getDescricaoPessoal());
    }
}
