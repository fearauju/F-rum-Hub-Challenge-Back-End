package hub.forum.api.domain.resposta.dto;

public record DadosValidacaoResposta(
        Long topicoId,
        Long respostaId,
        Long autorId
) {}
