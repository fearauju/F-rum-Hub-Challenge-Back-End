package hub.forum.api.util;

import hub.forum.api.domain.usuario.TipoUsuario;
import hub.forum.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "professores")
@DiscriminatorValue("PROFESSOR")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
public class ProfessorTest extends Usuario {

    @Column(name = "titularidade_academica")
    private String titularidadeAcademica;

    @Column(name = "anos_experiencia")
    private Integer anosExperiencia;

    @Column(name = "total_horas_lecionadas")
    private Integer totalHorasLecionadas;

    @Column(name = "data_de_admissao")
    private LocalDate dataDeAdmissao;

    // Remova o @OneToOne aqui, pois já existe no Usuario

    @Override
    public TipoUsuario obterTipoUsuario() {
        return TipoUsuario.PROFESSOR;
    }
}