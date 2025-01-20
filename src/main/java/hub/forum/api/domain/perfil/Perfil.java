package hub.forum.api.domain.perfil;

import hub.forum.api.domain.perfil.dto.DadosAtualizacaoPerfil;
import hub.forum.api.domain.perfil.dto.DadosCadastroPerfil;
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
public class Perfil {

    @Id
    private Long id; // Mesmo ID do usuário

    @OneToOne
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 1000)
    private String descricaoPessoal;

    @Column(nullable = false)
    private LocalDate dataNascimento;


    public void criarPerfil(DadosCadastroPerfil dados, Usuario usuario) {
        this.nome = dados.nome().trim();
        this.descricaoPessoal = dados.descricaoPessoal().trim();
        this.dataNascimento = dados.dataNascimento();
        this.usuario = usuario;
    }

    public void atualizarPerfil(@Valid DadosAtualizacaoPerfil dados) {
        if(dados.nome() != null){
            this.nome = dados.nome().trim();
        }

        if(dados.descricaoPessoal() != null){
            this.descricaoPessoal = dados.descricaoPessoal().trim();
        }
    }
}