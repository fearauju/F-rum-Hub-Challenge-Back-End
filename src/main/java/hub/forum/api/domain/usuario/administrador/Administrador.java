package hub.forum.api.domain.usuario.administrador;

import hub.forum.api.domain.usuario.TipoUsuario;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.administrador.dto.DadosRegistroAcao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "administradores")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Administrador extends Usuario {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;

    @OneToMany(mappedBy = "administrador", cascade = CascadeType.ALL)
    private List<RegistroAcaoAdmin> acoesExecutadas = new ArrayList<>();

    @Transient
    public TipoUsuario obterTipoUsuario() {
        return TipoUsuario.ADMINISTRADOR;
    }

    public void registrarAcao(DadosRegistroAcao dados) {
        var registro = new RegistroAcaoAdmin(this, dados.acoesExecutadas(), dados.detalhes().trim());
        this.acoesExecutadas.add(registro);
    }
}