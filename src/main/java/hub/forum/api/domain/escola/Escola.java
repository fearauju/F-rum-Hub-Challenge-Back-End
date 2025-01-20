package hub.forum.api.domain.escola;

import hub.forum.api.domain.escola.dto.DadosAtualizacaoEscola;
import hub.forum.api.domain.escola.dto.DadosCadastroEscola;
import hub.forum.api.domain.formacao.Formacao;
import hub.forum.api.domain.formacao.dto.DadosCadastroFormacao;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "escolas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Escola {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String nomeEscola;

    @Enumerated(EnumType.STRING)
    @Column(name = "area_formacao", nullable = false)
    private AreaFormacao areaFormacao;

    @OneToMany(mappedBy = "escola", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @OrderBy("nome ASC")
    private List<Formacao> formacao = new ArrayList<>();


    public void cadastrarEscola(DadosCadastroEscola dados) {

        this.nomeEscola = dados.nomeEscola().trim();
        this.areaFormacao = dados.areaFormacao();
    }

    public void atualizarInformacoes(DadosAtualizacaoEscola dados) {

            if(dados.nomeEscola() != null) {
                this.nomeEscola = dados.nomeEscola().trim();
            }

            if(dados.areaFormacao() != null){
                this.areaFormacao = dados.areaFormacao();
            }
    }

    public void validarFormacao(DadosCadastroFormacao dados) {
        if (!this.areaFormacao.equals(dados.areaFormacao())) {
            throw new ValidacaoException(
                    "A formação não pode ser cadastrada na escola de " +
                            this.areaFormacao + ". Área da formação: " + dados.areaFormacao()
            );
        }
    }
}
