package hub.forum.api.domain.topico.validacao;

import hub.forum.api.domain.curso.repository.CursoRepository;
import hub.forum.api.domain.matricula.Matricula;
import hub.forum.api.domain.matricula.repository.MatriculaRepository;
import hub.forum.api.domain.usuario.estudante.repository.EstudanteRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ValidarFormacaoEstudante implements ValidadorTopico {

    @Autowired
    private EstudanteRepository  estudanteRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Override
    public void validar(DadosValidacaoTopico dados) {
        if (!dados.isEstudante()) {
            return;
        }

        log.debug("Validando formação do estudante para criação de tópico");

        var estudante = estudanteRepository.findByIdWithMatriculaAtiva(dados.autorId())
                .orElseThrow(() -> {
                    log.error("Estudante não encontrado ID: {}", dados.autorId());
                    return new ValidacaoException("Estudante não encontrado");
                });

        // Pegar a formação da matrícula ativa
        var formacaoEstudante = estudante.getMatriculas().stream()
                .filter(Matricula::isAtiva)
                .findFirst()
                .map(Matricula::getFormacao)
                .orElseThrow(() -> {
                    log.error("Matrícula ativa não encontrada para estudante ID: {}", dados.autorId());
                    return new ValidacaoException("Matrícula ativa não encontrada");
                });

        var curso = cursoRepository.findByIdWithFormacao(dados.cursoId())
                .orElseThrow(() -> {
                    log.error("Curso não encontrado ID: {}", dados.cursoId());
                    return new ValidacaoException("Curso não encontrado");
                });

        if (!formacaoEstudante.equals(curso.getFormacao())) {
            log.error("Tentativa de criar tópico em curso de outra formação. " +
                            "Formação do estudante: {}, Formação do curso: {}",
                    formacaoEstudante.getFormacao(),
                    curso.getFormacao().getFormacao());

            throw new ValidacaoException(
                    "Você só pode criar tópicos para cursos da sua formação: " +
                            formacaoEstudante.getFormacao()
            );
        }
    }
}
