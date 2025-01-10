package hub.forum.api.domain.escola;

public record DadosDetalhamentoCadastroEscola(
        Long escolaID,
        String nomeEscola
) {

    public DadosDetalhamentoCadastroEscola(Escola escola){
        this(escola.getId(), escola.getNomeEscola());
    }
}
