package hub.forum.api.domain.usuario;

import hub.forum.api.domain.formacao.Formacao;
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
public class Professor extends Usuario {

    private List<String> especializacoes = new ArrayList<>();
    private String titularidadeAcademica;
    private List<String>cursosLecionados = new ArrayList<>();
    private Integer totalHorasLecionadas;
    private Integer anosDeExperiencia;
    private Date dataDeAdmissao;
    private Boolean ativo;

    @Override
    public TipoUsuario obterTipoUsuario() {
        return TipoUsuario.PROFESSOR;
    }

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<Formacao> formacao;
}
