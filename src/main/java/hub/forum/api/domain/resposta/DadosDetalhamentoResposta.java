package hub.forum.api.domain.resposta;

import java.time.LocalDateTime;

public record DadosDetalhamentoResposta(

        String autor,
        String resposta,
        LocalDateTime dataCriacao
) {

    public DadosDetalhamentoResposta (Resposta resposta){

        this(resposta.getAutor().getPerfil().getNome(), resposta.getResposta(), resposta.getDataCriacao());
    }
}
