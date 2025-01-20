package hub.forum.api.domain.topico.validacao;

import hub.forum.api.domain.curso.repository.CursoRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class validarCursoFormacao  {

    @Autowired
    private CursoRepository cursoRepository;


    public void validar(DadosValidacaoTopico dados) {

        if (dados.cursoId() != null && dados.formacao() != null) {
            var curso = cursoRepository.findById(dados.cursoId())
                    .orElseThrow(() -> new ValidacaoException("Curso não encontrado"));

            if (!curso.getFormacao().getFormacao().equals(dados.formacao())) {
                throw new ValidacaoException("Curso não pertence à formação informada");
            }
        }
    }
}
