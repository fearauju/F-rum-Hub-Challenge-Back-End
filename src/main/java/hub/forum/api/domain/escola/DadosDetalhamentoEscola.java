package hub.forum.api.domain.escola;

public record DadosDetalhamentoEscola(

        Long id_usuario,
        Long id_escola,
        String nomeEscola
) {

    public DadosDetalhamentoEscola(DadosDetalhamentoEscola detalhamentoEscola){
        this(detalhamentoEscola.id_usuario(), detalhamentoEscola.id_escola(), detalhamentoEscola.nomeEscola());
    }
}
