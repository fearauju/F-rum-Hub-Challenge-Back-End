package hub.forum.api.domain.formacao;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.curso.dto.DadosCursosFormacao;
import hub.forum.api.domain.escola.Escola;
import hub.forum.api.domain.formacao.dto.DadosAtualizacaoFormacao;
import hub.forum.api.domain.formacao.dto.DadosCadastroFormacao;
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
    private Long id;

    @Column(nullable = false)
    private String formacao;

    @Column(nullable = false, length = 1000)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;

    @OneToMany(mappedBy = "formacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Curso> cursos = new ArrayList<>();


    public void cadastrarFormacao(DadosCadastroFormacao dados, Escola escola, List<Professor> professores) {

        escola.validarFormacao(dados);

        this.escola = escola;
        this.formacao = dados.formacao().trim();
        this.descricao = dados.descricao().trim();

        // Remove duplicatas nos cursos
        this.cursos = criarCursos(
                dados.cursos().stream().distinct().collect(Collectors.toList()),
                professores
        );
    }

    private List<Curso> criarCursos(List<DadosCursosFormacao> cursosDto, List<Professor> professores) {
        return cursosDto.stream()
                .map(cursoDto -> {
                    var curso = new Curso();
                    curso.setCurso(cursoDto.nome().trim());
                    curso.setDuracao(DurationUtils.parseDuracao(cursoDto.duracao()));
                    curso.setFormacao(this);

                    // Associar professores ao curso
                    List<Professor> professoresDoCurso = professores.stream()
                            .filter(p -> cursoDto.nomeProfessor().contains(p.getUsuario().getPerfil().getNome()))
                            .toList();

                    professoresDoCurso.forEach(curso::adicionarProfessor);

                    return curso;
                })
                .collect(Collectors.toList());
    }

    public void atualizarInformacoes(DadosAtualizacaoFormacao dados) {

        if(dados.formacao() != null){
            this.formacao = dados.formacao().trim();
        }

        if(dados.descricao() != null){
            this.descricao = dados.descricao().trim();
        }
    }
}
