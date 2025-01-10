package hub.forum.api.domain.perfil;

import java.time.LocalDate;


public record DadosDetalhamentoPerfil(
        //nomePerfil
        //formação
        //cursos concluídos
        //certificados

        Long id,
        String nome,
        LocalDate dataNascimento,
        String descricao
) {

    public DadosDetalhamentoPerfil(Perfil perfil){
        this(perfil.getId(), perfil.getNome(), perfil.getDataNascimento(),perfil.getDescricaoPessoal());
    }
}
