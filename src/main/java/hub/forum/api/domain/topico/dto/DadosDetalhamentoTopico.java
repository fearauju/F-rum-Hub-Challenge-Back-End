package hub.forum.api.domain.topico.dto;

import hub.forum.api.domain.topico.StatusTopico;
import hub.forum.api.domain.topico.Topico;
import java.time.LocalDateTime;

//criar records auxiliares en vez de tentar desserializar objetos completos
//Evitamos ‘loops’ de serialização
//Retornamos apenas os dados necessários
//Dados adicionais podem ser incluídos nos dtos auxiliares
public record DadosDetalhamentoTopico(
        Long id,
        String titulo,
        String mensagem,
        LocalDateTime dataCriacao,
        DadosAutor autor,
        DadosCursoTopico curso,
        String status
) {
    public DadosDetalhamentoTopico(Topico topico) {
        this(
                topico.getId(),
                obterTitulo(topico),
                topico.getMensagem(),
                topico.getDataCriacao(),
                obterDadosAutor(topico), //record auxiliar
                obterDadosCurso(topico), //record auxiliar
                obterStatus(topico)
        );
    }

    private static String obterTitulo(Topico topico) {
        return topico.getTitulo() != null ? topico.getTitulo().trim() : "";
    }

    private static DadosAutor obterDadosAutor(Topico topico) {
        return new DadosAutor(topico.getAutor());
    }

    private static DadosCursoTopico obterDadosCurso(Topico topico) {
        return new DadosCursoTopico(topico.getCurso());
    }

    private static String obterStatus(Topico topico) {
        return topico.isResolvido() ?
                StatusTopico.RESOLVIDO.getDescricao() :
                StatusTopico.NAO_RESOLVIDO.getDescricao();
    }
}