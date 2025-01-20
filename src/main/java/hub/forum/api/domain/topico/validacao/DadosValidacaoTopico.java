package hub.forum.api.domain.topico.validacao;

public record DadosValidacaoTopico(
        Long topicoId,
        Long autorId,
        Long cursoId,
        String formacao,
        String titulo,
        String mensagem,
        boolean resolvido,
        boolean isEstudante // novo campo
) {}