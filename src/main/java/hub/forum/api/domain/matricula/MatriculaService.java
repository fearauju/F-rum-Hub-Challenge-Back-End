package hub.forum.api.domain.matricula;

import hub.forum.api.domain.curso.CursoRepository;
import hub.forum.api.domain.curso.DadosInscricaoCurso;
import hub.forum.api.domain.usuario.UsuarioRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;


    public boolean verificarStatusMatricula(Long estudanteID) {

        return matriculaRepository.findMatriculaAtivaByEstudanteId(estudanteID)
                .map(Matricula::isAtiva)
                .orElse(false);
    }


    @Transactional
    public DadosDetalhamentoMatricula renovarMatriculaEstudante(Long estudanteID) {

        var matricula = matriculaRepository.findMatriculaAtivaByEstudanteId(estudanteID)
                .orElseThrow(() -> new ValidacaoException("Matrícula não encontrada"));

        log.info("Renovando matrícula do estudante ID: {}", estudanteID);
        matricula.renovarMatricula();

        log.info("Matrícula renovada com sucesso para estudante ID: {}", estudanteID);
        return new DadosDetalhamentoMatricula(matricula);
    }

    public DadosDetalhamentoMatricula buscarMatriculaEstudante(Long estudanteID) {

        var matricula = matriculaRepository.findMatriculaByEstudanteId(estudanteID);

        return new DadosDetalhamentoMatricula(matricula);
    }

    @Transactional
    public void inscreverEmCurso(DadosInscricaoCurso dados) {
        var matricula = matriculaRepository.findMatriculaByEstudanteId(dados.estudanteID());

        if(matricula == null){
            throw new ValidacaoException("matricula não encontrada");
        }

        var curso = cursoRepository.findById(dados.cursoID())
                .orElseThrow(() -> new ValidacaoException("Curso não encontrado"));

        if (matricula.possuiCurso(curso)) {
            throw new ValidacaoException("Estudante já está inscrito neste curso");
        }

        matricula.adicionarCurso(curso);
        log.info("Estudante {} inscrito no curso {}", dados.estudanteID(), dados.cursoID());
    }
}
