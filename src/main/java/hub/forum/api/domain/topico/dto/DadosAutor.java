package hub.forum.api.domain.topico.dto;

import hub.forum.api.domain.usuario.Usuario;

//criar records auxiliares en vez de tentar desserializar objetos completos
public record DadosAutor(
        Long id,
        String nome
) {
    public DadosAutor(Usuario autor) {
        this(
                autor.getId(),
                autor.getPerfil().getNome()
        );
    }
}
