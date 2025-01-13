package hub.forum.api.infra.batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;

@Configuration
public class RetryConfig {

    @Bean
    public RetryPolicy retryPolicy() {
        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        policy.setMaxAttempts(3);
        return policy;
    }

    @Bean
    public BackOffPolicy backOffPolicy() {
        ExponentialBackOffPolicy policy = new ExponentialBackOffPolicy();
        policy.setInitialInterval(1000);
        policy.setMultiplier(2);
        policy.setMaxInterval(5000);
        return policy;
    }
}