package hub.forum.api.domain.usuario.estudante;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
public class Badge {


    @Column(name = "badge")
    private String badge;

    @Column(name = "data_conquista")
    private LocalDateTime dataConquista;


    @Column(name = "descricao")
    private String descricao;

    public Badge(String nome, LocalDateTime dataConquista, String descricao) {
        this.badge = nome;
        this.dataConquista = dataConquista;
        this.descricao = descricao;
    }
}
