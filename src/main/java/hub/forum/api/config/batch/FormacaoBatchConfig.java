package hub.forum.api.config.batch;

import hub.forum.api.domain.formacao.batch.context.FormacaoJobContext;
import hub.forum.api.domain.formacao.batch.processors.FormacaoItemProcessor;
import hub.forum.api.domain.formacao.batch.readers.FormacaoItemReader;
import hub.forum.api.domain.formacao.batch.writers.FormacaoItemWriter;
import hub.forum.api.infra.exceptions.BatchProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@RequiredArgsConstructor
public class FormacaoBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final RetryPolicy retryPolicy;
    private final BackOffPolicy backOffPolicy;
    private final FormacaoItemReader formacaoReader;
    private final FormacaoItemProcessor formacaoProcessor;
    private final FormacaoItemWriter formacaoWriter;

    @Bean
    public Step salvarFormacaoStep() {
        return new StepBuilder("salvarFormacaoStep", jobRepository)
                .<FormacaoJobContext, FormacaoJobContext>chunk(1, transactionManager)
                .reader(formacaoReader)
                .processor(formacaoProcessor)
                .writer(formacaoWriter)
                .faultTolerant()
                .retry(OptimisticLockingFailureException.class)
                .retryPolicy(retryPolicy)
                .backOffPolicy(backOffPolicy)
                .skip(BatchProcessingException.class)
                .skipLimit(3)
                .build();
    }
}