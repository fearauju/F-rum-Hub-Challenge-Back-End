package hub.forum.api.domain.usuario.estudante;

import hub.forum.api.domain.matricula.Matricula;
import hub.forum.api.domain.usuario.TipoUsuario;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.util.ConverterListaDeString;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "estudantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("ESTUDANTE")
public class Estudante extends Usuario {

    private Integer anoIngresso;

    @Column(name = "interesses_academicos")
    @Convert(converter = ConverterListaDeString.class)
    private List<String> interessesAcademicos;

    @Column(name = "cursos_inscrito")
    @Convert(converter = ConverterListaDeString.class)
    private List<String> cursosInscrito; //feito posteriormente, quando o estudante se inscrever em algum dos cursos disponíveis.
                                        //Com a entidade matriculaCurso

    @Column(precision = 10, scale = 2)
    private BigDecimal pontuacao;//Sistema de pontuação com base na interação com o conteúdo dos cursos. Feito posteriormente
    private Integer cargaHorariaConcluida;

    @OneToMany(mappedBy = "estudante", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Matricula> matriculas;

    @Override
    public TipoUsuario obterTipoUsuario() {
        return TipoUsuario.ESTUDANTE;
    }
}
