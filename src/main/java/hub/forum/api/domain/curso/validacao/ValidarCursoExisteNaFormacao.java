package hub.forum.api.domain.curso.validacao;

import hub.forum.api.domain.curso.CursoRepository;
import hub.forum.api.domain.validacao.ValidadorBase;
import hub.forum.api.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarCursoExisteNaFormacao implements ValidadorBase<DadosValidacaoCurso> {

    @Autowired
    private CursoRepository repository;

    @Override
    public void validar(DadosValidacaoCurso dados) {

        if (repository.existsByCursoIgnoreCase(dados.curso())) {
            throw new ValidacaoException("Já existe um curso com este nome");
        }
    }
}