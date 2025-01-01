package hub.forum.api.domain.usuario;

import hub.forum.api.domain.formacao.Formacao;
import hub.forum.api.domain.usuario.converterStrings.ConverterListaDeString;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "professores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("PROFESSOR")
public class Professor extends Usuario implements InativacaoUsuario {

    @Column(name = "especializacoes")
    @Convert(converter = ConverterListaDeString.class)
    private List<String> especializacoes = new ArrayList<>();

    private String titularidadeAcademica;

    @Column(name = "cursos_lecionados")
    @Convert(converter = ConverterListaDeString.class)
    private List<String>cursosLecionados = new ArrayList<>();

    private Integer totalHorasLecionadas;
    private Integer anosDeExperiencia;
    private Date dataDeAdmissao;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<Formacao> formacao;

    @Override
    public TipoUsuario obterTipoUsuario() {
        return TipoUsuario.PROFESSOR;
    }

    @Column(name = "ativo")
    private boolean ativo = true;

    @Override
    public boolean isAtivo() {
        return ativo;
    }

    @Override
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
