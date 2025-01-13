package hub.forum.api.infra.batch.monitoramento;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@Slf4j
public class BatchMonitoringService {

    private final MeterRegistry registry;

    public BatchMonitoringService(MeterRegistry registry) {
        this.registry = registry;
    }

    public void recordJobExecution(JobExecution jobExecution) {
        Timer.builder("batch.job.duration")
                .tag("job", jobExecution.getJobInstance().getJobName())
                .tag("status", jobExecution.getStatus().name())
                .register(registry)
                .record(Duration.between(
                        jobExecution.getStartTime(),
                        jobExecution.getEndTime() != null ? jobExecution.getEndTime() : Instant.now()
                ));
    }
}
