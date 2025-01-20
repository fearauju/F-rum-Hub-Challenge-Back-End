package hub.forum.api.domain.usuario.estudante;

import hub.forum.api.domain.matricula.Matricula;
import hub.forum.api.domain.usuario.TipoUsuario;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.estudante.dto.DadosAtualizacaoEstudante;
import hub.forum.api.domain.usuario.estudante.dto.DadosCadastroEstudante;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "estudantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("ESTUDANTE")
@Slf4j
@EqualsAndHashCode
public class Estudante {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;

    @Column(name = "ano_ingresso")
    private Integer anoIngresso;

    @ElementCollection
    @CollectionTable(
            name = "estudante_interesses_academicos",
            joinColumns = @JoinColumn(name = "estudante_id")
    )
    @Column(name = "interesse")
    private Set<String> interessesAcademicos = new HashSet<>();

    @Column(name = "nivel_academico")
    @Enumerated(EnumType.STRING)
    private NivelAcademico nivelAcademico;

    @Column(precision = 10, scale = 2)
    private BigDecimal pontuacao;

    @Column(name = "carga_horaria_concluida")
    private Integer cargaHorariaConcluida;

    @Column(name = "cursos_concluidos")
    private Integer cursosConcluidos;

    @Column(name = "media_avaliacoes")
    private Double mediaAvaliacoes;

    @Column(name = "ultima_atividade")
    private LocalDateTime ultimaAtividade;

    @Column(name = "certificados_emitidos")
    private Integer certificadosEmitidos;

    @OneToMany(mappedBy = "estudante", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Matricula> matriculas = new HashSet<>();


    @ElementCollection
    @CollectionTable(
            name = "estudante_badges",
            joinColumns = @JoinColumn(name = "estudante_id")
    )
    @Column(name = "badge")
    private Set<Badge> badgesConquistadas = new HashSet<>();

    @Transient
    public TipoUsuario obterTipoUsuario() {
        return TipoUsuario.ESTUDANTE;
    }

    public void cadastrarEstudante(DadosCadastroEstudante dados) {

        this.anoIngresso = LocalDate.now().getYear();
        this.interessesAcademicos = new HashSet<>(dados.interessesAcademicos());
        this.nivelAcademico = dados.nivelAcademico();
        this.pontuacao = BigDecimal.ZERO;
        this.cargaHorariaConcluida = 0;
        this.cursosConcluidos = 0;
        this.mediaAvaliacoes = 0.0;
        this.certificadosEmitidos = 0;
        this.matriculas = new HashSet<>();
        this.badgesConquistadas = new HashSet<>();
    }

    public void atualizarEstudante(DadosAtualizacaoEstudante dados) {

        if (dados.interessesAcademicos() != null && !dados.interessesAcademicos().isEmpty()) {
            this.interessesAcademicos.addAll(dados.interessesAcademicos());
        }
        if (dados.nivelAcademico() != null) {
            System.out.println("Nível acadêmico anterior: " + this.nivelAcademico);
            System.out.println("Novo nível acadêmico: " + dados.nivelAcademico());
            this.nivelAcademico = dados.nivelAcademico();
        }

        this.ultimaAtividade = LocalDateTime.now();
    }

    public void atualizarProgressoCurso(Integer cargaHoraria) {
        this.cargaHorariaConcluida += cargaHoraria;
        this.ultimaAtividade = LocalDateTime.now();
    }

    public void registrarCursoConcluido() {
        this.cursosConcluidos++;
        this.certificadosEmitidos++;
        verificarBadgesPorConclusao();
    }

    private void verificarBadgesPorConclusao() {
        if (this.cursosConcluidos == 1) {
            adicionarBadge("Primeiro Curso", "Concluiu seu primeiro curso");
        } else if (this.cursosConcluidos == 5) {
            adicionarBadge("Dedicado", "Concluiu 5 cursos");
        } else if (this.cursosConcluidos == 10) {
            adicionarBadge("Expert", "Concluiu 10 cursos");
        }
    }

    public void adicionarBadge(String nome, String descricao) {
        var badge = new Badge(nome, LocalDateTime.now(), descricao);
        this.badgesConquistadas.add(badge);
    }

    public void atualizarMediaAvaliacoes(Double novaAvaliacao) {
        if (this.mediaAvaliacoes == 0.0) {
            this.mediaAvaliacoes = novaAvaliacao;
        } else {
            this.mediaAvaliacoes = (this.mediaAvaliacoes + novaAvaliacao) / 2;
        }
    }
}
