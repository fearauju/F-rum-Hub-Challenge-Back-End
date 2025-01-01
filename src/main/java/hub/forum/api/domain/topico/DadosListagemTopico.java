package hub.forum.api.domain.topico;

import java.time.LocalDateTime;

public record DadosListagemTopico(

        String titulo,
        String mensagem,
        LocalDateTime dataCriacao
) {

    public DadosListagemTopico(Topico topico){
        this(topico.getTitulo(), topico.getMensagem(), topico.getDataCriacao());
    }
}
