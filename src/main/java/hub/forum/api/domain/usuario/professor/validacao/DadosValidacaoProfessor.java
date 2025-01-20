package hub.forum.api.domain.usuario.professor.validacao;

import java.time.LocalDate;
import java.util.Set;

public record DadosValidacaoProfessor(
        Long usuarioId,
        Set<String> especializacoes,
        String titularidadeAcademica,
        Integer anosExperiencia,
        LocalDate dataAdmissao
) {}
