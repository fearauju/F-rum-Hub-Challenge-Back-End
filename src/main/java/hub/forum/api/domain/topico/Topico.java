package hub.forum.api.domain.topico;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.resposta.Resposta;
import hub.forum.api.domain.topico.dto.DadosAtualizacaoTopico;
import hub.forum.api.domain.topico.dto.DadosCadastroTopico;
import hub.forum.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Topico")
@Table(name = "topicos", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"titulo", "mensagem"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, length = 255)
    private String mensagem;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Column(columnDefinition = "TINYINT")
    private boolean resolvido;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario autor;

    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resposta> respostas = new ArrayList<>();

    public void cadastrarTopico(DadosCadastroTopico dados, Curso curso, Usuario autor) {

        this.titulo = dados.titulo().trim();
        this.mensagem = dados.mensagem().trim();
        this.dataCriacao = LocalDateTime.now();
        this.curso = curso;
        this.autor = autor;
        this.resolvido = false; // Fechar tópico --> usuário suporte ou administrador
    }

    public void atualizarTopico(DadosAtualizacaoTopico dados) {

        if(dados.titulo() != null){
            this.titulo = dados.titulo().trim();
        }

        if(dados.mensagem() != null){
            this.mensagem = dados.mensagem().trim();
        }
    }

    public void marcarComoResolvido() {
        this.resolvido = true;
    }
}