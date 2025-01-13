package hub.forum.api.infra.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BatchCleanupService {

    private final JobOperator jobOperator;
    private final JobExplorer jobExplorer;

    public BatchCleanupService(JobOperator jobOperator, JobExplorer jobExplorer) {
        this.jobOperator = jobOperator;
        this.jobExplorer = jobExplorer;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupOldJobs() {
        try {
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

            Set<Long> oldJobExecutions = jobExplorer.findRunningJobExecutions("cadastrarFormacaoJob")
                    .stream()
                    .filter(execution -> {
                        LocalDateTime endTime = execution.getEndTime();
                        return endTime != null && endTime.isBefore(thirtyDaysAgo);
                    })
                    .map(JobExecution::getId)
                    .collect(Collectors.toSet());

            for (Long executionId : oldJobExecutions) {
                try {
                    jobOperator.abandon(executionId);
                    log.info("Job execution abandonada com sucesso: {}", executionId);
                } catch (Exception e) {
                    log.error("Erro ao abandonar job execution: {}", executionId, e);
                }
            }
        } catch (Exception e) {
            log.error("Erro durante a limpeza de jobs antigos", e);
        }
    }
}