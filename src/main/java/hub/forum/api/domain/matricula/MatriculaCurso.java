package hub.forum.api.domain.matricula;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matricula_cursos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class MatriculaCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "matricula_id")
    private Matricula matricula;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    private LocalDateTime dataInscricao;
    private LocalDateTime dataConclusao;
    private boolean concluido;

    public MatriculaCurso(Matricula matricula, Curso curso) {
        this.matricula = matricula;
        this.curso = curso;
        this.dataInscricao = LocalDateTime.now();
        this.concluido = false;
    }



    public void concluir() {
        if (this.concluido) {
            throw new ValidacaoException("Curso já concluído");
        }
        this.concluido = true;
        this.dataConclusao = LocalDateTime.now();
    }

    public void validarNaoConcluido() {
        if (this.concluido) {
            throw new ValidacaoException(
                    "Curso já concluído em " + this.dataConclusao
            );
        }
    }
}
