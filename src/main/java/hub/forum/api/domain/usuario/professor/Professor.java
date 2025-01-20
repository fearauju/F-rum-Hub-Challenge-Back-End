package hub.forum.api.domain.usuario.professor;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.usuario.TipoUsuario;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.professor.dto.DadosAtualizacaoProfessor;
import hub.forum.api.domain.usuario.professor.dto.DadosCadastroProfessor;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "professores")
@DiscriminatorValue("PROFESSOR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Slf4j
public class Professor {

    @Id
    private Long id; // Mesmo ID do usuário

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;

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
    private LocalDate dataDeAdmissao;

    @ManyToMany
    @JoinTable(
            name = "cursos_professores",
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    private Set<Curso> cursosLecionados = new HashSet<>();

    @Column(name = "total_horas_lecionadas")
    private Long totalHorasLecionadas = 0L;

    @PostLoad //tentar inicializar as coleções adequadamente
    private void inicializarColecoes() {
        if (especializacoes == null) {
            especializacoes = new HashSet<>();
        }

        if (cursosLecionados == null) {
            cursosLecionados = new HashSet<>();
        }
    }

    public void cadastrarProfessor(DadosCadastroProfessor dados) {
        // Converter List para Set
        this.especializacoes = new HashSet<>(dados.especializacoes());
        this.titularidadeAcademica = dados.titularidadeAcademica().trim();
        this.anosExperiencia = dados.anosExperiencia();
        this.dataDeAdmissao = dados.dataAdmissao();
    }

    public void adicionarCurso(Curso curso, Duration duracao) {
        if (curso == null) return;

        if (this.cursosLecionados.add(curso)) { // `add` retorna false se já existe
            this.totalHorasLecionadas += duracao.toHours();
            curso.adicionarProfessor(this); // Sincroniza do outro lado
        }
    }

    public void atualizar(DadosAtualizacaoProfessor dados) {

        if (dados.especializacoes() != null) {
            this.especializacoes.addAll(dados.especializacoes());
        }

        if (dados.titularidadeAcademica() != null) {
            this.titularidadeAcademica = dados.titularidadeAcademica().trim();
        }
    }


    @Transient
    public TipoUsuario obterTipoUsuario() {
        return TipoUsuario.PROFESSOR;
    }


    @Override
    public String toString() {
        String nomePerfil = "Não possui perfil cadastrado";
        if (usuario != null && usuario.getPerfil() != null && usuario.getPerfil().getNome() != null) {
            nomePerfil = usuario.getPerfil().getNome();
        }

        return "Professor{" +
                "id= " + getId() +
                ", login= " + (usuario != null ? usuario.getLogin() : "N/A") +
                ", username= " + (usuario != null ? usuario.getUsername() : "N/A") +
                ", nome do perfil= " + nomePerfil +
                ", especializacoes=" + especializacoes +
                ", titularidadeAcademica='" + titularidadeAcademica + '\'' +
                ", cursosLecionados=" + cursosLecionados +
                ", totalHorasLecionadas=" + totalHorasLecionadas +
                ", anosExperiencia=" + anosExperiencia +
                ", dataDeAdmissao=" + dataDeAdmissao +
                '}';
    }
}


