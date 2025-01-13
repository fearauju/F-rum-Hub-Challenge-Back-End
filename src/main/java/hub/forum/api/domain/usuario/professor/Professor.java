package hub.forum.api.domain.usuario.professor;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.usuario.TipoUsuario;
import hub.forum.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "professores")
@DiscriminatorValue("PROFESSOR")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Professor extends Usuario {

    @ElementCollection
    @CollectionTable(
            name = "professor_especializacoes",
            joinColumns = @JoinColumn(name = "professor_id")
    )
    @Column(name = "especializacao")
    private Set<String> especializacoes = new HashSet<>();

    @Column(name = "titularidade_academica")
    private String titularidadeAcademica;

    @Column(name = "anos_experiencia")
    private Integer anosExperiencia;

    @Column(name = "data_de_admissao")
    private LocalDateTime dataDeAdmissao;

    @ElementCollection
    @CollectionTable(
            name = "professor_cursos_lecionados",
            joinColumns = @JoinColumn(name = "professor_id")
    )
    @Column(name = "curso")
    private Set<String> cursosLecionados = new HashSet<>();


    @Column(name = "total_horas_lecionadas")
    private Long totalHorasLecionadas = 0L; // Alterado para Long para evitar problemas de conversão

    @ManyToMany
    @JoinTable(
            name = "curso_professor",
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    private List<Curso> cursos = new ArrayList<>();

    @Override
    public TipoUsuario obterTipoUsuario() {
        return TipoUsuario.PROFESSOR;
    }

    @PostLoad //tentar inicializar as coleções adequadamente
    private void inicializarColecoes() {
        if (especializacoes == null) {
            especializacoes = new HashSet<>();
        }
        if (cursos == null) {
            cursos = new ArrayList<>();
        }
        if (totalHorasLecionadas == null) {
            totalHorasLecionadas = 0L; // Garantir que nunca seja null
        }
    }

    public void cadastrarProfessor(DadosCadastroProfessor dados) {
        // Converter List para Set
        this.especializacoes = new HashSet<>(dados.especializacoes());
        this.titularidadeAcademica = dados.titularidade_academica();
        this.anosExperiencia = dados.anos_experiencia();
        this.dataDeAdmissao = dados.data_admissao().atStartOfDay(); // converte LocalDate para LocalDateTime
    }

    public void adicionarCurso(String nomeCurso, Duration duracao) {
        this.cursosLecionados.add(nomeCurso);
        this.totalHorasLecionadas += duracao.toHours();
    }


    @Override
    public String toString() {
        return "Professor{" +
                "id= " + getId() +
                "login= " + getLogin() +
                "username= " + getUsername() +
                "nome do perfil= " + getPerfil().getNome() +
                "especializacoes=" + especializacoes +
                ", titularidadeAcademica='" + titularidadeAcademica + '\'' +
                ", cursosLecionados=" + cursosLecionados +
                ", totalHorasLecionadas=" + totalHorasLecionadas +
                ", anosExperiencia=" + anosExperiencia +
                ", dataDeAdmissao=" + dataDeAdmissao +
                '}';
    }
}
