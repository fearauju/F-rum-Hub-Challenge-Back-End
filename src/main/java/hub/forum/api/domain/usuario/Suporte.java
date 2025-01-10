package hub.forum.api.domain.usuario;

import hub.forum.api.domain.topico.DadosFechamentoTopico;
import hub.forum.api.domain.usuario.converterStrings.ConverterListaDeString;
import hub.forum.api.domain.usuario.validacao.DadosValidacaoUsuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "suportes")
@Getter
@Setter
@DiscriminatorValue("SUPORTE")
public class Suporte extends Usuario  {


    @Column(name = "especializacoes")
    @Convert(converter = ConverterListaDeString.class)
    private List<String>especializacoes = new ArrayList<>();

    private String turnoDeTrabalho;
    private Integer casosResolvidos; // Associa-se com o tópico. Quando é finalizado pelo próprio suporte ou ao usuário escolher a melhor resposta.
    private Double avaliacaoSuporte;
    private String motivoAvaliacao;
    private LocalDate dataAdmissao;

    @Override
    public TipoUsuario obterTipoUsuario() {
        return TipoUsuario.SUPORTE;
    }

    public void adicionarCasosResolvidos(DadosFechamentoTopico dados, Suporte suporte){
        this.casosResolvidos++;
        this.avaliacaoSuporte = suporte.getAvaliacaoSuporte() + dados.avaliacaoSuporte();

        if(dados.motivoAvaliacao() != null){
            this.motivoAvaliacao = dados.motivoAvaliacao();
        }
    }


    public void cadastrarSuporte(DadosCadastroSuporte dados) {
        this.especializacoes = dados.especializacoes();
        this.casosResolvidos = 0;
        this.avaliacaoSuporte = 0.0;
        this.turnoDeTrabalho = dados.turno_de_trabalho();
        this.dataAdmissao = dados.data_admissao();
    }
}
