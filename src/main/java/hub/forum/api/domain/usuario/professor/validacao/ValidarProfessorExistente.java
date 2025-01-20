package hub.forum.api.domain.usuario.professor.validacao;

import hub.forum.api.domain.usuario.professor.repository.ProfessorRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarProfessorExistente implements ValidadorProfessor {

    @Autowired
    private ProfessorRepository professorRepository;

    @Override
    public void validar(DadosValidacaoProfessor dados) {

        if (professorRepository.existsById(dados.usuarioId())) {
            throw new ValidacaoException("Usuário já está cadastrado como professor");
        }

    }
}