package hub.forum.api.domain.escola;


import hub.forum.api.domain.formacao.Formacao;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "Escola")
@Table(name = "escolas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Escola {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nomeEscola;

    @OneToMany(mappedBy = "escola", cascade = CascadeType.ALL)
    private List<Formacao> formacao;

    public Escola( String nomeEscola) {
        this.nomeEscola = nomeEscola;
    }
}
