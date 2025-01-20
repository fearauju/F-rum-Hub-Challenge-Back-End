package hub.forum.api.domain.topico;

import lombok.Getter;

@Getter
public enum StatusTopico {

    RESOLVIDO("Resolvido"),
    NAO_RESOLVIDO("Não resolvido");

    private String descricao;

    StatusTopico(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}