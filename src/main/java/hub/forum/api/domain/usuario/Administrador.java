package hub.forum.api.domain.usuario;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "administradores")
@Getter
@Setter
@DiscriminatorValue("ADMINISTRADOR")
public class Administrador extends Usuario {

    private List<String> acoesExecutadas;
    private LocalDateTime DataAcaoExecutada;


    @Override
    public TipoUsuario obterTipoUsuario() {
        return TipoUsuario.ADMINISTRADOR;
    }
    // realiza as alterações internas no sistema
}
