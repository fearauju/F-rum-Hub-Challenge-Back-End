package hub.forum.api.domain.formacao.batch.listeners.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FormacaoJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Iniciando job de cadastro de formação");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Job de cadastro de formação finalizado com sucesso");
        } else {
            log.error("Job de cadastro de formação finalizado com erro");
        }
    }
}