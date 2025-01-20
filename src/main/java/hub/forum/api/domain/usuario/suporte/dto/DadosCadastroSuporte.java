package hub.forum.api.domain.usuario.suporte.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hub.forum.api.domain.usuario.suporte.TurnoTrabalho;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

public record DadosCadastroSuporte(

        @NotEmpty(message = "Pelo menos uma especialização deve ser adicionada")
        Set<String> especializacoes,

        @NotNull(message = "O turno de trabalho é obrigatório")
        TurnoTrabalho turnoTrabalho, // usa o enum diretamente

        @NotNull(message = "Informe a data de admissão do funcionário")
        @PastOrPresent(message = "A data de admissão não pode ser futura")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataAdmissao
) {}
