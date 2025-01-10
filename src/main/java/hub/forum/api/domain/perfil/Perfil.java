package hub.forum.api.domain.perfil;

import hub.forum.api.domain.perfil.validacao.DadosValidacaoPerfil;
import hub.forum.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "perfis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Perfil {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 1000)
    private String descricaoPessoal;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario  usuario;

    public void criarPerfil(DadosValidacaoPerfil validacaoDados, Usuario usuario) {

        this.nome = validacaoDados.nome();
        this.descricaoPessoal = validacaoDados.descricaoPessoal();
        this.dataNascimento = validacaoDados.dataNascimento();
        this.usuario = usuario;
    }

    public void atualizarPerfil(@Valid DadosAtualizacaoPerfil dados) {

        if(dados.nome() != null){
            this.nome = dados.nome();
        }

        if(dados.dataNascimento() != null){
            this.dataNascimento = dados.dataNascimento();
        }

        if(dados.descricaoPessoal() != null){
            this.descricaoPessoal = dados.descricaoPessoal();
        }
    }
}
