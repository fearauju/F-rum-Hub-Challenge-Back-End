package hub.forum.api.domain.matricula;

import java.time.LocalDateTime;

public record DadosDetalhamentoMatricula(

        Long estudante_id,
        String nomeEstudante,
        Integer numeroMatricula,
        LocalDateTime dataAssinaturaMatricula,
        LocalDateTime dataExpiracaoMatricula
) {
    public DadosDetalhamentoMatricula(DadosDetalhamentoMatricula dadosMatricula) {
        this(dadosMatricula.estudante_id(), dadosMatricula.nomeEstudante(), dadosMatricula.numeroMatricula(),
                dadosMatricula.dataAssinaturaMatricula(),dadosMatricula.dataExpiracaoMatricula());
    }
}
