package hub.forum.api.domain.formacao.batch.processors;

import hub.forum.api.domain.formacao.batch.context.FormacaoJobContext;
import hub.forum.api.domain.usuario.professor.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfessorItemProcessor implements ItemProcessor<FormacaoJobContext, FormacaoJobContext> {

    private final ProfessorRepository professorRepository;

    @Override
    public FormacaoJobContext process(FormacaoJobContext context) {
        List<String> nomesProfessores = context.getDadosCadastro().cursos().stream()
                .flatMap(curso -> curso.nome_professor().stream())
                .distinct()
                .collect(Collectors.toList());

        var professores = professorRepository.findByNomesOptimized(nomesProfessores);
        log.info("Encontrados {} professores", professores.size());

        context.setProfessores(professores);
        return context;
    }
}
