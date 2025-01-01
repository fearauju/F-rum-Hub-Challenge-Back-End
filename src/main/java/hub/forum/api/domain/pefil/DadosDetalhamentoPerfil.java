package hub.forum.api.domain.pefil;

import java.time.LocalDate;


public record DadosDetalhamentoPerfil(
        //nomePerfil
        //formação
        //cursos concluídos
        //certificados

        String nome,
        LocalDate dataNascimento,
        String descricao
) {

    public DadosDetalhamentoPerfil(Perfil perfil){
        this(perfil.getNome(), perfil.getDataNascimento(),perfil.getDescricaoPessoal());
    }
}
