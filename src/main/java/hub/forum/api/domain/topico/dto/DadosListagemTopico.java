package hub.forum.api.domain.topico.dto;

import hub.forum.api.domain.topico.Topico;
import java.time.LocalDateTime;

public record DadosListagemTopico(

        Long topicoId,
        String titulo,
        String mensagem,
        LocalDateTime dataCriacao,
        DadosAutor autor
) {

    public DadosListagemTopico(Topico topico){
        this(topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao(),
                obterDadosAutor(topico));
    }

    private static DadosAutor obterDadosAutor(Topico topico) {
        return new DadosAutor(topico.getAutor());
    }
}
