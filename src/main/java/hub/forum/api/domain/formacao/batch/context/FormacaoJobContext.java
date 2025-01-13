package hub.forum.api.domain.formacao.batch.context;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.formacao.dto.DadosCadastroFormacao;
import hub.forum.api.domain.formacao.Formacao;
import hub.forum.api.domain.usuario.professor.Professor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// DTO para transferência de dados entre os ‘steps’:
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormacaoJobContext {
    private DadosCadastroFormacao dadosCadastro;
    private Formacao formacao;
    private List<Professor> professores;
    private List<Curso> cursos;
}
