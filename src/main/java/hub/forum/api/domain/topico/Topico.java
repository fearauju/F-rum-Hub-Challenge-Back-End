package hub.forum.api.domain.topico;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.pefil.Perfil;
import hub.forum.api.domain.resposta.Resposta;
import hub.forum.api.domain.usuario.Suporte;
import hub.forum.api.domain.usuario.Usuario;
import jakarta.persistence.*;
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
}
