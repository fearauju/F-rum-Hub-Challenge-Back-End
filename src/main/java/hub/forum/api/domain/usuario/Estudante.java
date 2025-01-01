package hub.forum.api.domain.usuario;

import hub.forum.api.domain.matricula.Matricula;
import hub.forum.api.domain.usuario.converterStrings.ConverterListaDeString;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "estudantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("ESTUDANTE")
public class Estudante extends Usuario{

    private Integer AnoDeIngresso;

    @Column(name = "interesses_academicos")
    @Convert(converter = ConverterListaDeString.class)
    private List<String> interessesAcademicos; //Áreas de estudo ou temas que o estudante tem interesse.

    private Integer Pontuacao;//Sistema de pontuação com base na interação com o conteúdo.
    private Boolean StatusDaMatricula; // se não renovar após a data de assinatura.
    private Integer cargaHorariaConcluida;


    @OneToMany(mappedBy = "estudante", cascade = CascadeType.ALL)
    private List<Matricula> matriculas;

    @Override
    public TipoUsuario obterTipoUsuario() {
        return TipoUsuario.ESTUDANTE;
    }
}
