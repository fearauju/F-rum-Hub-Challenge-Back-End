package hub.forum.api.domain.topico;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.resposta.Resposta;
import hub.forum.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Topico")
@Table(name = "topicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String titulo;

    @Column(unique = true)
    private String mensagem;

    private LocalDateTime dataCriacao;

    private boolean resolvido;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @ManyToOne(optional = false) // Cada tópico tem um único autor
    @JoinColumn(name = "autor_id") // O autor está relacionado à tabela "usuarios"
    private Usuario autor;

    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL)
    private List<Resposta> resposta = new ArrayList<>();

    public void cadastrarTopico(@Valid DadosCadastroTopico dados) {

        this.titulo = dados.titulo();
        this.mensagem = dados.mensagem();
        this.dataCriacao = LocalDateTime.now();
        this.curso.setId(dados.cursoID());
        this.autor.setId(dados.usuarioID());
        this.resolvido = false; //fechar tópico --> usuário suporte
    }
    public void atualizarTopico(DadosAtualizacaoTopico dados) {

        if(dados.titulo() != null){
            this.titulo = dados.titulo();
        }

        if(dados.mensagem() != null){
            this.mensagem = dados.mensagem();
        }
    }
}
