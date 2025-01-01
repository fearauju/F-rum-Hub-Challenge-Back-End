package hub.forum.api.domain.pefil;

import hub.forum.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "Perfil")
@Table(name = "perfis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Perfil {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // validações para cadastro interno
    //fazer pelo mysql todas as regex e validações, se possível.

    private String nome;
    private String descricaoPessoal;
    private LocalDate dataNascimento;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario  usuario;

    public void atualizarPefil(@Valid DadosAtualizacaoPerfil dados) {

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
