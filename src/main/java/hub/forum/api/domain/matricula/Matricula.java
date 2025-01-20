package hub.forum.api.domain.matricula;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.formacao.Formacao;
import hub.forum.api.domain.usuario.estudante.Estudante;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "matriculas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_matricula",unique = true)
    private Long numeroDaMatricula; //GERADO AUTOMATICAMENTE

    @Column(nullable = false)
    @PastOrPresent(message = "A data de assinatura não pode estar no futuro")
    private LocalDateTime dataAssinatura;

    @Column(nullable = false)
    @Future(message = "A data de expiração deve estar no futuro")
    private LocalDateTime dataExpiracaoAssinatura;

    @Column(name = "ativo", columnDefinition = "BOOLEAN")
    private boolean ativa; //status matricula

    @ManyToOne(optional = false)
    @JoinColumn(name = "estudante_id")
    private Estudante estudante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "formacao_id")
    private Formacao formacao;

    @OneToMany(mappedBy = "matricula", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatriculaCurso> matriculaCurso = new ArrayList<>();


    @PrePersist
    public void prePersist() {
        if (this.dataAssinatura == null) {
            this.dataAssinatura = LocalDateTime.now();
        }
        if (this.dataExpiracaoAssinatura == null) {
            this.dataExpiracaoAssinatura = this.dataAssinatura.plusYears(1);
        }
        gerarNumeroMatricula();
    }

    private void gerarNumeroMatricula() {
        if (this.numeroDaMatricula == null) {
            LocalDateTime agora = LocalDateTime.now();
            String ano = String.valueOf(agora.getYear());
            String sequencial = String.format("%05d",
                    ThreadLocalRandom.current().nextInt(1, 99999));
            this.numeroDaMatricula = Long.parseLong(ano + sequencial);
        }
    }

    public void validarMatriculaAtiva() {
        if (!this.isAtiva()) {
            throw new ValidacaoException("Matrícula inativa. Necessário renovar");
        }
    }


    public void cadastrarMatriculaEstudante(Formacao formacao, Estudante estudante){

        this.numeroDaMatricula = getNumeroDaMatricula();
        this.dataAssinatura = LocalDateTime.now();
        this.dataExpiracaoAssinatura = dataAssinatura.plusYears(1);
        this.ativa = true;
        this.formacao = formacao;
        this.estudante = estudante;
    }

    public boolean isAtiva() {

        // Verifica e atualiza o status se necessário
        if (ativa && LocalDateTime.now().isAfter(dataExpiracaoAssinatura)) {
            this.ativa = false;
        }
        return this.ativa;
    }

    public boolean podeSerRenovada() {
        return !isAtiva() && LocalDateTime.now().isAfter(dataExpiracaoAssinatura);
    }

    public void renovarMatricula() {

        if (!podeSerRenovada()) {
            throw new IllegalStateException("Matrícula não pode ser renovada: " +
                    (isAtiva() ? "ainda está ativa" : "período de renovação não iniciado"));
        }

        this.dataAssinatura = LocalDateTime.now();
        this.dataExpiracaoAssinatura = dataAssinatura.plusYears(1);
        this.ativa = true;
    }

    public void adicionarCurso(Curso curso) {

        if (!possuiCurso(curso)) {
            var matriculaCurso = new MatriculaCurso(this, curso);
            this.matriculaCurso.add(matriculaCurso);
            matriculaCurso.setMatricula(this);
        }
    }

    public boolean possuiCurso(Curso curso) {

        return matriculaCurso.stream()
                .anyMatch(mc -> mc.getCurso().equals(curso));
    }
}