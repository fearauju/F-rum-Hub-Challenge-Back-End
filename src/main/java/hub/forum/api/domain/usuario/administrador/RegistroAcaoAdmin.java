package hub.forum.api.domain.usuario.administrador;

import hub.forum.api.domain.util.ConverterListaDeString;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "registro_acoes_admin")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class RegistroAcaoAdmin {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrador_id")
    private Administrador administrador;

    @Column(name = "acoes_executadas")
    @Convert(converter = ConverterListaDeString.class)
    private List<String> acoesExecutadas;

    @Column(name = "data_execucao")
    private LocalDateTime dataExecucao;

    @Column(name = "detalhes")
    private String detalhes;

    public RegistroAcaoAdmin(Administrador administrador, List<String> acoes, String detalhes) {
        this.administrador = administrador;
        this.acoesExecutadas = acoes;
        this.dataExecucao = LocalDateTime.now();
        this.detalhes = detalhes;
    }
}