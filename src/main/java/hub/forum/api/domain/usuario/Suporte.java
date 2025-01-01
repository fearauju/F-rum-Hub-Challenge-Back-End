package hub.forum.api.domain.usuario;

import hub.forum.api.domain.usuario.converterStrings.ConverterListaDeString;
import jakarta.persistence.*;
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
public class Suporte extends Usuario implements InativacaoUsuario{


    @Column(name = "especializacoes")
    @Convert(converter = ConverterListaDeString.class)
    private List<String>especializacoes = new ArrayList<>();

    private String turnoDeTrabalho;
    private Integer casosResolvidos; // associa-se com quando o tópico é finalizado pelo próprio suporte ou ao usuário escolher a melhor resposta.
    private Double avaliacaoAtendimento;
    private Date dataAdmissao;


    @Column(name = "ativo")
    private boolean ativo = true;

    @Override
    public TipoUsuario obterTipoUsuario() {
        return TipoUsuario.SUPORTE;
    }

    @Override
    public boolean isAtivo() {
        return ativo;
    }

    @Override
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    //responsável por gerenciar fórum, no momento

}
