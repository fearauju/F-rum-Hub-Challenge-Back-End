package hub.forum.api.domain.util;

import java.time.Duration;

public class DurationUtils {
    public static Duration parseDuracao(String duracao) {
        return Duration.parse("PT" + duracao.replace(":", "H") + "M");
    }
}