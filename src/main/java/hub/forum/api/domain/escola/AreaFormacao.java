package hub.forum.api.domain.escola;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
public enum AreaFormacao {

    CRIACAO_EDICAO_VIDEOS("Criação de vídeos"),
    DATA_SCIENCE("Data science"),
    DEV_OPS("Dev ops"),
    FRONT_END("Front-end"),
    IA("Inteligência artificial"),
    INOVACAO_E_GESTAO("Inovação e gestão"),
    MOBILE("Mobile"),
    BACK_END("Back-end"),
    UX_DESIGN("Ux-Design");

    private final String descricao;

    AreaFormacao(String descricao) {
        this.descricao = descricao;
    }

    @JsonCreator
    public static AreaFormacao fromDescricao(String area) {
        if (area == null || area.trim().isEmpty()) {
            throw new IllegalArgumentException("A área de formação não pode ser vazia");
        }

        return Arrays.stream(AreaFormacao.values())
                .filter(a -> a.getDescricao().equalsIgnoreCase(area.trim()) ||
                        a.name().equalsIgnoreCase(area.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Formação indisponível '%s'. Valores permitidos: %s",
                                area,
                                Arrays.stream(AreaFormacao.values())
                                        .map(af -> String.format("%s (%s)", af.getDescricao(), af.name()))
                                        .collect(Collectors.joining(", "))
                        )
                ));
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }
}
