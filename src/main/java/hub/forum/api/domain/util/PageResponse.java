package hub.forum.api.domain.util;

import org.springframework.data.domain.Page;
import java.util.ArrayList;
import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last
) {
    // Construtor que recebe os campos individualmente
    public PageResponse {
        content = content != null ? content : new ArrayList<>();
    }

    // Construtor que recebe um objeto Page do Spring
    public PageResponse(Page<T> page) {
        this(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
