package hub.forum.api.domain.usuario;

import hub.forum.api.domain.usuario.converterStrings.ConverterListaDeString;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "administradores")
@Getter
@Setter
@DiscriminatorValue("ADMINISTRADOR")
@Slf4j
public class Administrador extends Usuario {

    @Column(name = "acoes_executadas")
    @Convert(converter = ConverterListaDeString.class)
    private List<String> acoesExecutadas;

    @Column(name = "data_acao_executada")
    private LocalDateTime DataAcaoExecutada;

    @Override
    public TipoUsuario obterTipoUsuario() {
        log.debug("Obtendo tipo de usuário para Administrador: {}", TipoUsuario.ADMINISTRADOR);
        return TipoUsuario.ADMINISTRADOR;
    }
    // realiza as alterações internas no sistema
}
