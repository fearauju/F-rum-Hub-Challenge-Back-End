package hub.forum.api.domain.matricula.service;

import hub.forum.api.domain.curso.repository.CursoRepository;
import hub.forum.api.domain.matricula.MatriculaCurso;
import hub.forum.api.domain.matricula.dto.DadosCursoMatriculado;
import hub.forum.api.domain.matricula.dto.DadosDetalhamentoMatricula;
import hub.forum.api.domain.matricula.repository.MatriculaCursoRepository;
import hub.forum.api.domain.matricula.repository.MatriculaRepository;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.estudante.Estudante;
import hub.forum.api.domain.usuario.estudante.repository.EstudanteRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private EstudanteRepository estudanteRepository;

    @Autowired
    private MatriculaCursoRepository matriculaCursoRepository;


    @Transactional
    public DadosDetalhamentoMatricula renovarMatriculaEstudante(Long estudanteId) {

        // Verificar se estudante existe
        if(!estudanteRepository.existsById(estudanteId)){
            log.error("Tentativa de renovação de matrícula para estudante inexistente. ID: {}", estudanteId);
            throw new ValidacaoException("Estudante não está cadastrado");
        }

        // Buscar matrícula atual
        var matricula = matriculaRepository.findByEstudanteIdWithCursos(estudanteId)
                .orElseThrow(() -> {
                    log.error("Matrícula não encontrada para estudante ID: {}", estudanteId);
                    return new ValidacaoException("Matrícula não encontrada");
                });

        // Verificar se pode ser renovada
        if(!matricula.podeSerRenovada()){
            log.warn("Tentativa de renovação de matrícula inválida. Estudante ID: {}, Expiração: {}",
                    estudanteId, matricula.getDataExpiracaoAssinatura());
            throw new ValidacaoException("A matrícula não pode ser renovada. " +
                    "Verifique se está ativa ou se já expirou. Data de expiração: " +
                    matricula.getDataExpiracaoAssinatura());
        }

        // Renovar matrícula
        log.info("Renovando matrícula do estudante ID: {}", estudanteId);
        matricula.renovarMatricula();
        matriculaRepository.save(matricula);

        log.info("Matrícula renovada com sucesso para estudante ID: {}", estudanteId);
        return new DadosDetalhamentoMatricula(matricula);
    }

    @Transactional
    public void inscreverEmCurso(Long cursoId, Usuario usuarioLogado) {

        var estudante = estudanteRepository.findByUsuarioId(usuarioLogado.getId())
                .orElseThrow(() -> new ValidacaoException("Estudante não encontrado"));

        log.info("Iniciando processo de inscrição em curso para estudante ID: {}", estudante.getId());

        try {
            // Buscar e validar matrícula ativa
            var matricula = matriculaRepository.findByEstudanteIdWithCursos(estudante.getId())
                    .orElseThrow(() -> {
                        log.error("Matrícula não encontrada para estudante ID: {}", estudante.getId());
                        return new ValidacaoException("Matrícula não encontrada");
                    });

            // Buscar e validar curso
            var curso = cursoRepository.findById(cursoId)
                    .orElseThrow(() -> {
                        log.error("Curso não encontrado. ID: {}", cursoId);
                        return new ValidacaoException("Curso não encontrado");
                    });

            // Validar formação
            if (!matricula.getFormacao().equals(curso.getFormacao())) {
                log.error("Tentativa de inscrição em curso de formação diferente. " +
                                "Formação do estudante: {}, Formação do curso: {}",
                        matricula.getFormacao().getFormacao(),
                        curso.getFormacao().getFormacao());
                throw new ValidacaoException(
                        "Não é possível se inscrever em cursos de outras formações. " +
                                "Sua formação atual é: " + matricula.getFormacao().getFormacao()
                );
            }

            // Verifica se já está inscrito
            if (matriculaCursoRepository.existsByMatriculaIdAndCursoId(
                    matricula.getId(), curso.getId())) {
                throw new ValidacaoException("Você já está inscrito neste curso");
            }

            log.info("Inscrevendo estudante ID: {} no curso ID: {}", estudante.getId(), curso.getId());

            // Inicializar totalDeAlunos se necessário
            if (curso.getTotalDeAlunos() == null) {
                curso.setTotalDeAlunos(0);
                cursoRepository.save(curso);
            }

            // Criar inscrição no curso
            var matriculaCurso = new MatriculaCurso(matricula, curso);
            matriculaCursoRepository.save(matriculaCurso);

            // Incrementar total de alunos
            curso.incrementarTotalAlunos();
            cursoRepository.save(curso);

            log.info("Estudante {} inscrito com sucesso no curso {}. Total de alunos: {}",
                    estudante.getUsuario().getPerfil().getNome(),
                    curso.getCurso(),
                    curso.getTotalDeAlunos());

        } catch (Exception e) {
            log.error("Erro ao inscrever estudante no curso: {}", e.getMessage());
            throw new ValidacaoException("Erro ao realizar inscrição no curso: " + e.getMessage());
        }
    }

    @Transactional
    public void concluirCurso(Long matriculaId, Long cursoId) {
        var matriculaCurso = matriculaCursoRepository
                .findByMatriculaIdAndCursoId(matriculaId, cursoId)
                .orElseThrow(() -> new ValidacaoException("Inscrição não encontrada"));

        matriculaCurso.getMatricula().validarMatriculaAtiva();
        matriculaCurso.validarNaoConcluido();
        matriculaCurso.concluir();
    }

    public List<DadosCursoMatriculado> listarCursosMatriculados(Long matriculaId) {
        return matriculaCursoRepository
                .findByMatriculaId(matriculaId)
                .stream()
                .map(DadosCursoMatriculado::new)
                .toList();
    }
}
