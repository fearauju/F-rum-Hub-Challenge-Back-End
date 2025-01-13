package hub.forum.api.domain.curso.validacao;

import hub.forum.api.domain.curso.repository.CursoRepository;
import hub.forum.api.domain.util.ValidadorBase;
import hub.forum.api.infra.exceptions.ValidacaoException;
import hub.forum.api.infra.security.SegurancaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarProfessorDoCurso implements ValidadorBase<DadosValidacaoCurso> {
    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private SegurancaService segurancaService;

    @Override
    public void validar(DadosValidacaoCurso dados) {
        var professorID = segurancaService.getUsuarioLogado().getId();
        var curso = cursoRepository.findByCurso(dados.curso());

        if (!cursoRepository.existsByIdAndProfessorId(curso.getId(), professorID)) {
            throw new ValidacaoException("Somente professor do curso pode realizar esta alteração");
        }
    }
}
