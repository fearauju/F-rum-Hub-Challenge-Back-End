package hub.forum.api.domain.usuario;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "suportes")
@Getter
@Setter
@DiscriminatorValue("SUPORTE")
public class Suporte extends Usuario{

    private List<String>especializacoes = new ArrayList<>();
    private String turnoDeTrabalho;
    private Integer casosResolvidos; // associa-se com quando o tópico é finalizado pelo próprio suporte ou ao usuário escolher a melhor resposta.
    private Double avaliacaoAtendimento;
    private Date dataAdmissao;
    private Boolean ativo;


    @Override
    public TipoUsuario obterTipoUsuario() {
        return TipoUsuario.SUPORTE;
    }
    //responsável por gerenciar fórum, no momento

}
