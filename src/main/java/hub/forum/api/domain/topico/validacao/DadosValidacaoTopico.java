package hub.forum.api.domain.topico.validacao;

public record DadosValidacaoTopico(
        Long topicoID,
        Long autorID,
        Long cursoID,
        String formacao,
        String titulo,
        String mensagem,
        boolean resolvido
) {}