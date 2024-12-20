package hub.forum.api.domain.usuario;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.pefil.Perfil;
import hub.forum.api.domain.Resposta;
import hub.forum.api.domain.Topico;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Usuario")
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;

    @Column(unique = true)
    private String email;
    private String senha;

    @OneToOne(mappedBy = "usuario", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    private Perfil perfil;

    @ManyToMany(mappedBy = "usuario")
    private List<Curso> curso = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Topico> topico = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Resposta> resposta = new ArrayList<>();



    public Usuario(@Valid DadosCadastroUsuario dados) {

        this.login = dados.login();
        this.email = dados.email();
        this.senha = dados.senha();
        this.perfil = new Perfil(dados.perfil());
        this.perfil.setUsuario(this); //estabelece o relacionamento bidirecional
    }
}
