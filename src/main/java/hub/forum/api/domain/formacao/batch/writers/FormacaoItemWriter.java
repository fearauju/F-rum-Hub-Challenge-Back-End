package hub.forum.api.domain.formacao.batch.writers;

import hub.forum.api.domain.formacao.repository.FormacaoRepository;
import hub.forum.api.domain.formacao.batch.context.FormacaoJobContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class FormacaoItemWriter implements ItemWriter<FormacaoJobContext> {

    private final FormacaoRepository formacaoRepository;
    private final StepExecution stepExecution;

    @Override
    public void write(Chunk<? extends FormacaoJobContext> chunk) throws Exception {
        var context = chunk.getItems().get(0);
        var formacao = formacaoRepository.save(context.getFormacao());

        context.setFormacao(formacao);
        stepExecution.getJobExecution()
                .getExecutionContext()
                .put("jobContext", context);

        log.info("Formação salva com ID: {}", formacao.getId());
    }
}