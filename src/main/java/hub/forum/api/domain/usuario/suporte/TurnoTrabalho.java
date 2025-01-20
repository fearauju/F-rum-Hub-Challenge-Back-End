package hub.forum.api.domain.usuario.suporte;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum TurnoTrabalho {
    MANHA("Manhã"),
    TARDE("Tarde"),
    NOITE("Noite");

    private final String descricao;

    TurnoTrabalho(String descricao) {
        this.descricao = descricao;
    }

    @JsonCreator
    public static TurnoTrabalho fromDescricao(String descricao) {
        for (TurnoTrabalho turno : TurnoTrabalho.values()) {
            if (turno.getDescricao().equalsIgnoreCase(descricao)) {
                return turno;
            }
        }
        throw new IllegalArgumentException("Turno inválido: " + descricao);
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }
}

