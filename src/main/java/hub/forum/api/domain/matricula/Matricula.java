package hub.forum.api.domain.matricula;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.usuario.Estudante;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(unique = true)
    private Long numeroMatricula; //GERADO AUTOMATICAMENTE

    @ManyToOne(optional = false)
    @JoinColumn(name = "estudante_id") // Chave estrangeira para Estudante
    private Estudante estudante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cursoID") // Chave estrangeira para Curso
    private Curso curso;

    @Column(nullable = false)
    @PastOrPresent(message = "A data de assinatura não pode estar no futuro")
    private LocalDateTime dataAssinatura; //MOMENTO DE SALVAMENTO -LOCALDATE.NOW();

    @Column(nullable = false)
    @Future(message = "A data de expiração deve estar no futuro")
    private LocalDateTime expiracaoAssinatura; //MOMENTO DE SALVAMENTO - ADICIONA 1 ANO A PARTIR DA DATA DE ASSINATURA;

    public boolean isAssinaturaValida() {
        return expiracaoAssinatura.isAfter(LocalDateTime.now());
    }
}