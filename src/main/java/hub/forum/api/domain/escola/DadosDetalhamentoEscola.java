package hub.forum.api.domain.escola;

public record DadosDetalhamentoEscola(
        Long id,
        String nomeEscola
) {

    public DadosDetalhamentoEscola(Escola escola){
        this(escola.getId(), escola.getNomeEscola());
    }
}