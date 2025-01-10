package hub.forum.api.domain.usuario;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.usuario.converterStrings.ConverterListaDeString;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "professores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("PROFESSOR")
public class Professor extends Usuario   {



    @Column(name = "especializacoes")
    @Convert(converter = ConverterListaDeString.class)
    private List<String> especializacoes = new ArrayList<>();

    @Column(name = "titularidade_academica")
    private String titularidadeAcademica;

    @Column(name = "cursos_lecionados")
    @Convert(converter = ConverterListaDeString.class)
    private List<String>cursosLecionados = new ArrayList<>();

    @Column(name = "total_horas_lecionadas")
    private Integer totalHorasLecionadas;

    @Column(name = "anos_experiencia")
    private Integer anosExperiencia;

    @Column(name = "data_de_admissao")
    private LocalDate dataDeAdmissao;

    @OneToMany(mappedBy = "professores", cascade = CascadeType.ALL)
    private List<Curso> cursos = new ArrayList<>();

    @Override
    public TipoUsuario obterTipoUsuario() {
        return TipoUsuario.PROFESSOR;
    }

    public void cadastrarProfessor(@Valid DadosCadastroProfessor dados) {

        this.especializacoes = dados.especializacoes();
        this.titularidadeAcademica = dados.titularidade_academica();
        this.cursosLecionados = dados.cursos_lecionados();
        this.anosExperiencia = dados.anos_experiencia();
        this.dataDeAdmissao = dados.data_admissao();
    }
}
