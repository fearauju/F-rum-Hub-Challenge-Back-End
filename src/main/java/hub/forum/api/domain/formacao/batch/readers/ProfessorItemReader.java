package hub.forum.api.domain.formacao.batch.readers;

import hub.forum.api.domain.formacao.batch.context.FormacaoJobContext;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfessorItemReader implements ItemReader<FormacaoJobContext> {

    private boolean processed = false;
    private final StepExecution stepExecution;

    @Override
    public FormacaoJobContext read() {
        if (processed) {
            return null;
        }

        processed = true;
        return (FormacaoJobContext) stepExecution
                .getJobExecution()
                .getExecutionContext()
                .get("jobContext");
    }
}
