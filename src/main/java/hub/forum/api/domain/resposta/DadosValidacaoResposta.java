package hub.forum.api.domain.resposta;

public record DadosValidacaoResposta(
        Long topicoId,
        Long respostaId,
        Long autorId
) {}
