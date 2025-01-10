package hub.forum.api.domain.usuario;

import java.time.LocalDate;
import java.util.List;

public record DadosDetalhamentoSuporte(

        String nome,
        List<String> especializacoes,
        Integer casosResolvidos,
        Double avaliacaoAtendimento,
        LocalDate dataAdmissao,
        String turnoDeTrabalho
) {

    public DadosDetalhamentoSuporte(Suporte suporte){

        this(suporte.getPerfil().getNome(), suporte.getEspecializacoes(),
                suporte.getCasosResolvidos(),suporte.getAvaliacaoSuporte(),
                suporte.getDataAdmissao(), suporte.getTurnoDeTrabalho());
    }
}
