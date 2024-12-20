package hub.forum.api.domain.escola;

import hub.forum.api.domain.formacao.DadosCadastroFormacao;
import hub.forum.api.domain.formacao.Formacao;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

    private String nomeEscola; // Ex: Alura

    @OneToMany(mappedBy = "escola", cascade = CascadeType.ALL)
    private List<Formacao> formacao;

    public Escola(@Valid DadosCadastroEscola dados) {
        this.nomeEscola = dados.nomeEscola();
    }

    public Escola(DadosCadastroFormacao dados) {
        this.formacao.forEach(f -> setId(dados.escola_id()));
    }
}
