package hub.forum.api.domain.formacao;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.escola.Escola;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Formacao")
@Table(name = "formacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Formacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String formacao;// Ex: Desenvolvimento de Jogos, Java, SQL Banco de Dados
    @Column(length = 1000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    private AreaFormacao areaFormacao;

    @ManyToOne
    @JoinColumn(name = "escola_id")
    private Escola escola;

    @OneToMany(mappedBy = "formacao", cascade = CascadeType.ALL)
    private List<Curso> cursos = new ArrayList<>();

    public Formacao(@Valid DadosCadastroFormacao dados) {
        this.escola = new Escola(dados);
        this.formacao = dados.formacao();
        this.areaFormacao = AreaFormacao.valueOf(dados.areaFormacao().name());
        this.descricao = dados.descricao();
    }
}
