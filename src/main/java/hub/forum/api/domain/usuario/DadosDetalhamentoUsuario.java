package hub.forum.api.domain.usuario;

public record DadosDetalhamentoUsuario(

        Long id,
        String nome,
        String email
) {

    public DadosDetalhamentoUsuario(Usuario usuario){
        this(usuario.getId(), usuario.getPerfil().getNome(), usuario.getEmail());
    }

}
