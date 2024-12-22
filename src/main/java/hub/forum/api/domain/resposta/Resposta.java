package hub.forum.api.domain.resposta;

import hub.forum.api.domain.pefil.Perfil;
import hub.forum.api.domain.topico.Topico;
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
    @JoinColumn(name = "perfil_id")
    private Perfil perfil;

    @ManyToOne
    @JoinColumn(name = "topico_id")
    private Topico topico;
}
