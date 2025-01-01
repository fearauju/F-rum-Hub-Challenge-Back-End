package hub.forum.api.domain.resposta;

import hub.forum.api.domain.topico.Topico;
import hub.forum.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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

    private String resposta;
    private LocalDateTime dataCriacao;
    private boolean melhor_resposta; // se tiver melhor resposta tópico é fechado

    @ManyToOne
    @JoinColumn(name = "topico_id")
    private Topico topico;

    @ManyToOne(optional = false) // Cada resposta tem um único autor
    @JoinColumn(name = "autor_id") // O autor está relacionado à tabela "usuarios"
    private Usuario autor;

    public void salvarResposta(DadosRegistroReposta dados) {

        if(dados.resposta() != null){
            this.resposta = dados.resposta();
        }

        this.dataCriacao = LocalDateTime.now();
        this.melhor_resposta = false;
    }

    public void atualizarResposta(DadosRegistroReposta dados) {
        if(dados.resposta() != null){
            this.resposta = dados.resposta();
        }
    }

    public void escolherMelhorResposta() {
        this.melhor_resposta = true;
    }


}
