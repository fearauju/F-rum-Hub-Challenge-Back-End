package hub.forum.api.domain.formacao;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.escola.Escola;
import hub.forum.api.domain.usuario.Professor;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private String formacao;// Ex: Desenvolvimento de Jogos, Java, Machine Learning
    @Column(length = 1000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    private AreaFormacao areaFormacao;

    @ManyToOne
    @JoinColumn(name = "escola_id")
    private Escola escola;

    @OneToMany(mappedBy = "formacao", cascade = CascadeType.ALL)
    private List<Curso> cursos = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "professor_id")
    private Professor professor;

    public Formacao(@Valid DadosCadastroFormacao dados, Escola escola) {

        this.escola = escola;
        this.formacao = dados.formacao();
        this.areaFormacao = AreaFormacao.valueOf(dados.areaFormacao().name());
        this.descricao = dados.descricao();
        this.cursos = dados.cursos().stream()
                .map(cursoDto -> {
                    Curso curso = new Curso();
                    curso.setCurso(cursoDto.nome());
                    curso.setDuracao(Duration.parse("PT" + cursoDto.duracao().replace(":","H") + "M")); // Converte HH:mm para Duration
                    curso.setFormacao(this); // Associa o curso à formação
                    return curso;
                })
                .collect(Collectors.toList());
    }
}
