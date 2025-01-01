package hub.forum.api.domain.resposta;

import java.time.LocalDateTime;

public record DadosListagemResposta(

        String nomePerfil,
        String resposta,
        LocalDateTime dataCriacao
) {

    public DadosListagemResposta(Resposta resposta){

        this(resposta.getAutor().getPerfil().getNome(),
                resposta.getResposta(), resposta.getDataCriacao());
    }
}
