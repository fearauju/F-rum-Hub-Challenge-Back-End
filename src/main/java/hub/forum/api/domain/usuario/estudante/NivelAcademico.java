package hub.forum.api.domain.usuario.estudante;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;


@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum NivelAcademico {
    ENSINO_MEDIO("Ensino Médio"),
    GRADUACAO("Graduação"),
    POS_GRADUACAO("Pós-Graduação"),
    MESTRADO("Mestrado"),
    DOUTORADO("Doutorado"),
    NAO_ENCONTRADO("não encontrado");

    private final String descricao;

    NivelAcademico(String descricao) {
        this.descricao = descricao;
    }

    @JsonCreator
    public static NivelAcademico fromString(String text) {
        if (text == null) {
            return null;
        }

        for (NivelAcademico nivelAcademico : NivelAcademico.values()) {
            if (text.equalsIgnoreCase(nivelAcademico.name()) ||
                    text.equalsIgnoreCase(nivelAcademico.getDescricao())) {
                return nivelAcademico;
            }
        }
        throw new IllegalArgumentException("Nível acadêmico inválido: " + text);
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }
}



