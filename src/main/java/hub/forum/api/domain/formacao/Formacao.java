package hub.forum.api.domain.formacao;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.curso.DadosCursosFormacao;
import hub.forum.api.domain.escola.Escola;
import hub.forum.api.domain.matricula.Matricula;
import hub.forum.api.domain.usuario.Professor;
import hub.forum.api.domain.usuario.UsuarioRepository;
import jakarta.persistence.*;
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

    @Column(nullable = false, unique = true)
    private String formacao;// Ex: Desenvolvimento de Jogos, Java, Machine Learning

    @Column(length = 1000, nullable = false)
    private String descricao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AreaFormacao areaFormacao;

    @ManyToOne
    @JoinColumn(name = "escola_id")
    private Escola escola;

    @OneToMany(mappedBy = "formacao", cascade = CascadeType.ALL)
    private List<Matricula> matriculas;

    @OneToMany(mappedBy = "formacao", cascade = CascadeType.ALL)
    private List<Curso> cursos = new ArrayList<>();

    public void cadastrarFormacao(DadosCadastroFormacao dados, Escola escola, List<Professor> professores) {

        this.escola = escola;
        this.formacao = dados.formacao().trim();
        this.areaFormacao = dados.areaFormacao();
        this.descricao = dados.descricao().trim();

        this.cursos = criarCursos(dados.cursos(), professores);
    }

    private List<Curso> criarCursos(List<DadosCursosFormacao> cursosDto, List<Professor> professores) {

        return cursosDto.stream()
                .map(cursoDto -> {
                    var curso = new Curso();
                    curso.setCurso(cursoDto.nome().trim());
                    curso.setDuracao(parseDuracao(cursoDto.duracao()));
                    curso.setFormacao(this);
                    curso.setProfessores(professores);
                    return curso;
                })
                .collect(Collectors.toList());
    }

    private Duration parseDuracao(String duracao) {
        return Duration.parse("PT" + duracao.replace(":", "H") + "M");
    }

    public void atualizarInformacoes(DadosAtualizacaoFormacao dados) {

        if(dados.areaFormacao() != null){
            this.areaFormacao = dados.areaFormacao();
        }

        if(dados.formacao() != null){
            this.formacao = dados.formacao();
        }

        if(dados.descricao() != null){
            this.descricao = dados.descricao();
        }
    }
}
