package hub.forum.api.domain.topico.dto;

public record DadosEstatisticasTopico(
        Integer topicosResolvidos,
        Integer topicosNaoResolvidos,
        Integer topicosSemResposta,
        Integer topicosUrgentes,
        Double percentualResolucao,
        Double tempoMedioResposta
) {
    public DadosEstatisticasTopico {
        // Calcula o percentual de resolução se houver tópicos
        if (topicosResolvidos != null && topicosNaoResolvidos != null) {
            int total = topicosResolvidos + topicosNaoResolvidos;
            percentualResolucao = total > 0 ?
                    (double) topicosResolvidos * 100 / total : 0.0;
        }
    }
}