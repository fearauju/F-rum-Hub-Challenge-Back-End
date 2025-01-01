package hub.forum.api.domain.topico;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.formacao.AreaFormacao;

import java.time.LocalDateTime;

public record DadosDetalhamentoTopico(

        Long usuarioID,
        Long topicoID,
        AreaFormacao areaFormacao,
        Curso curso,
        String titulo,
        String mensagem,
        LocalDateTime dataCriacao
) {
    public DadosDetalhamentoTopico(Topico topico) {
        this(topico.getAutor().getId(), topico.getId(), topico.getCurso().getFormacao().getAreaFormacao(),
                topico.getCurso(), topico.getTitulo(), topico.getMensagem(), topico.getDataCriacao());
    }
}
