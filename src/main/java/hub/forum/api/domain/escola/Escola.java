package hub.forum.api.domain.escola;

import hub.forum.api.domain.formacao.Formacao;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "escolas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Escola {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String nomeEscola;

    @OneToMany(mappedBy = "escola", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @OrderBy("nome ASC")
    private List<Formacao> formacao;


    public void cadastrarEscola(DadosCadastroEscola dados) {

        this.nomeEscola = dados.nomeEscola();
    }

    public void atualizarInformacoes(DadosAtualizacaoEscola dados) {

        if(dados.nomeEscola() != null){
            this.nomeEscola = dados.nomeEscola();
        }
    }
}
