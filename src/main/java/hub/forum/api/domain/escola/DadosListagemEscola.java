package hub.forum.api.domain.escola;

public record DadosListagemEscola(

        Long id,
        String nomeEscola
) {

    public DadosListagemEscola(Escola escola){
        this(escola.getId(), escola.getNomeEscola());
    }
}
