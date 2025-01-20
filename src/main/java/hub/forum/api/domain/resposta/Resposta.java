package hub.forum.api.domain.resposta;

import hub.forum.api.domain.resposta.dto.DadosRegistroReposta;
import hub.forum.api.domain.topico.Topico;
import hub.forum.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "respostas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Resposta {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "mensagem", nullable = false, length = 1000)
    private String resposta;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "melhor_resposta", nullable = false, columnDefinition = "TINYINT")
    private boolean melhorResposta; // se tiver melhor resposta tópico é fechado

    @ManyToOne
    @JoinColumn(name = "topico_id")
    private Topico topico;

    @ManyToOne(optional = false) // Cada resposta tem um único autor
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    public void salvarResposta(DadosRegistroReposta dados, Topico topico, Usuario autor) {
        this.resposta = dados.resposta().trim();
        this.dataCriacao = LocalDateTime.now();
        this.melhorResposta = false;
        this.topico = topico;
        this.autor = autor;
    }

    public void atualizarResposta(DadosRegistroReposta dados) {
        if(dados.resposta() != null){
            this.resposta = dados.resposta().trim();
        }
    }

    public void escolherMelhorResposta(Topico topico) {
        this.melhorResposta = true;
        topico.marcarComoResolvido();
    }
}
