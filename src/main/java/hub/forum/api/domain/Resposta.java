package hub.forum.api.domain;

import hub.forum.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Resposta")
@Table(name = "respostas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Resposta {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String mensagem;
    private String dataCriacao;
    private boolean solucao; // Toda a resposta tem solução setada falso por padrão

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "topico_id")
    private Topico topico;
}
