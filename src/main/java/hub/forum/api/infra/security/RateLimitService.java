package hub.forum.api.infra.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RateLimitService {


    private Map<String, Bucket> bucketsPorUsuario = new ConcurrentHashMap<>();

    public boolean tryConsume(String login) {
        var bucket = bucketsPorUsuario.computeIfAbsent(login,
                k -> Bucket.builder()
                        .addLimit(Bandwidth.simple(3, Duration.ofMinutes(10)))
                        .build());

        return bucket.tryConsume(1);
    }
}