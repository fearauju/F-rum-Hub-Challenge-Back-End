package hub.forum.api.domain.resposta;

import hub.forum.api.domain.pefil.Perfil;
import hub.forum.api.domain.topico.Topico;
import hub.forum.api.domain.usuario.Suporte;
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
    private boolean melhor_resposta; // se tiver melhor resposta tópico é fechado

    @ManyToOne
    @JoinColumn(name = "topico_id")
    private Topico topico;

    @ManyToOne(optional = false) // Cada resposta tem um único autor
    @JoinColumn(name = "autor_id") // O autor está relacionado à tabela "usuarios"
    private Usuario autor;
}
