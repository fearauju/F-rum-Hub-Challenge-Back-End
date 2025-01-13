package hub.forum.api.domain.formacao;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.curso.dto.DadosCursosFormacao;
import hub.forum.api.domain.escola.Escola;
import hub.forum.api.domain.formacao.dto.DadosAtualizacaoFormacao;
import hub.forum.api.domain.formacao.dto.DadosCadastroFormacao;
import hub.forum.api.domain.matricula.Matricula;
import hub.forum.api.domain.usuario.professor.Professor;
import hub.forum.api.domain.util.DurationUtils;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@Entity
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

    @OneToMany(mappedBy = "formacao", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Matricula> matriculas;

    @OneToMany(mappedBy = "formacao", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Curso> cursos = new ArrayList<>();

    public void cadastrarFormacao(DadosCadastroFormacao dados, Escola escola, List<Professor> professores) {
        this.escola = escola;
        this.formacao = dados.formacao().trim();
        this.areaFormacao = dados.areaFormacao();
        this.descricao = dados.descricao().trim();

        this.cursos = criarCursos(dados.cursos(), professores);
    }

    private List<Curso> criarCursos(List<DadosCursosFormacao> cursosDto, List<Professor> professores) {
        Map<Long, Professor> professoresPorId = professores.stream()
                .collect(Collectors.toMap(Professor::getId, p -> p));

        return cursosDto.stream()
                .map(cursoDto -> {
                    var curso = new Curso();
                    curso.setCurso(cursoDto.nome().trim());
                    curso.setDuracao(DurationUtils.parseDuracao(cursoDto.duracao()));
                    curso.setFormacao(this);

                    // Usar apenas IDs dos professores inicialmente
                    Set<Long> professorIds = professores.stream()
                            .filter(p -> cursoDto.nome_professor().contains(p.getPerfil().getNome()))
                            .map(Professor::getId)
                            .collect(Collectors.toSet());

                    // Criar nova lista de professores
                    curso.setProfessores(new ArrayList<>());

                    // Adicionar referências por ID
                    professorIds.forEach(id -> {
                        Professor professor = professoresPorId.get(id);
                        if (professor != null) {
                            curso.getProfessores().add(professor);
                        }
                    });

                    return curso;
                })
                .collect(Collectors.toList());
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
