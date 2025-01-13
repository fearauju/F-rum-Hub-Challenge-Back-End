package hub.forum.api.infra.batch.monitoramento;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobCompletionNotificationListener implements JobExecutionListener {

    private final MeterRegistry meterRegistry;

    public JobCompletionNotificationListener(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB COMPLETED! Verificando resultados");
            meterRegistry.counter("batch.formacao.completed").increment();
        } else if(jobExecution.getStatus() == BatchStatus.FAILED) {
            log.error("!!! JOB FAILED! Verificando erros");
            meterRegistry.counter("batch.formacao.failed").increment();
        }
    }
}