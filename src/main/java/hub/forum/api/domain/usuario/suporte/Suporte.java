package hub.forum.api.domain.usuario.suporte;

import hub.forum.api.domain.topico.dto.DadosFechamentoTopico;
import hub.forum.api.domain.usuario.TipoUsuario;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.suporte.dto.DadosAtualizacaoSuporte;
import hub.forum.api.domain.usuario.suporte.dto.DadosCadastroSuporte;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "suportes")
@Getter
@Setter
@DiscriminatorValue("SUPORTE")
@EqualsAndHashCode(of = "id")
@Slf4j
public class Suporte  {


    @Id
    private Long id; // Mesmo ID do usuário

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;

    @ElementCollection
    @CollectionTable(
            name = "suporte_especializacoes",
            joinColumns = @JoinColumn(name = "suporte_id")
    )
    @Column(name = "especializacao")
    private Set<String> especializacoes = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "turno_trabalho")
    private TurnoTrabalho turnoTrabalho;

    private Integer casosResolvidos; // Associa-se com o tópico. Quando é finalizado pelo próprio suporte ou ao usuário escolher a melhor resposta.
    private Double avaliacaoSuporte;
    private String motivoAvaliacao;
    private LocalDate dataAdmissao;

    @Transient
    public TipoUsuario obterTipoUsuario() {
        return TipoUsuario.SUPORTE;
    }

    public void adicionarCasosResolvidos(DadosFechamentoTopico dados, Suporte suporte){
        this.casosResolvidos++;
        this.avaliacaoSuporte = (suporte.getAvaliacaoSuporte() + dados.avaliacaoSuporte())/2;

        if(dados.motivoAvaliacao() != null){
            this.motivoAvaliacao = dados.motivoAvaliacao();
        }
    }


    public void cadastrarSuporte(DadosCadastroSuporte dados) {

        this.especializacoes = new HashSet<>(dados.especializacoes());
        this.casosResolvidos = 0;
        this.avaliacaoSuporte = 0.0;
        this.turnoTrabalho = dados.turnoTrabalho();
        this.dataAdmissao =  dados.dataAdmissao();
    }

    public void atualizarSuporte( DadosAtualizacaoSuporte dados) {

        if(dados.especializacoes() != null){
            this.especializacoes.addAll(dados.especializacoes());
        }

        if(dados.turnoTrabalho() != null){
            this.turnoTrabalho = dados.turnoTrabalho();
        }
    }
}
