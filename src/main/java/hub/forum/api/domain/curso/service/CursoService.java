package hub.forum.api.domain.curso.service;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.curso.repository.CursoRepository;
import hub.forum.api.domain.curso.dto.DadosAtualizacaoCurso;
import hub.forum.api.domain.curso.dto.DadosDetalhamentoCurso;
import hub.forum.api.domain.curso.dto.DadosListagemCurso;
import hub.forum.api.domain.curso.dto.DadoscadastroCurso;
import hub.forum.api.domain.formacao.repository.FormacaoRepository;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.professor.Professor;
import hub.forum.api.domain.usuario.professor.repository.ProfessorRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CursoService {

    @Autowired
    private FormacaoRepository formacaoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ProfessorRepository professorRepository;



    @Transactional
    public DadosDetalhamentoCurso cadastrarCurso(
            Long formacaoId, DadoscadastroCurso dados) {

        var formacao = formacaoRepository.findById(formacaoId)
                .orElseThrow(() -> new ValidacaoException("Formação não encontrada"));


        // Coletar e validar professores
        List<String> nomesProfessores = dados.professores().stream()
                .distinct()
                .collect(Collectors.toList());

        List<Professor> professores = professorRepository.findByUsuarioPerfilNomeIn(nomesProfessores);

        if (professores.size() != nomesProfessores.size()) {
            List<String> nomesEncontrados = professores.stream()
                    .map(prof -> prof.getUsuario().getPerfil().getNome())
                    .collect(Collectors.toList());

            List<String> nomesNaoEncontrados = nomesProfessores.stream()
                    .filter(nome -> !nomesEncontrados.contains(nome))
                    .collect(Collectors.toList());
            throw new ValidacaoException("Professores não encontrados: " + String.join(", ", nomesNaoEncontrados));
        }

        var curso = new Curso();

        curso.cadastrarCurso(dados, formacao, professores);

        cursoRepository.save(curso);

        return new DadosDetalhamentoCurso(curso);
    }


    @Transactional
    public DadosDetalhamentoCurso atualizarCurso(
            Long cursoId,
            DadosAtualizacaoCurso dados,
            Usuario professor) {

        var curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new ValidacaoException("Curso não encontrado"));

        if (!cursoRepository.existsByIdAndProfessorId(cursoId, professor.getId())) {
            throw new ValidacaoException("Apenas professores vinculados ao curso podem atualizá-lo");
        }

        curso.atualizarDadosCurso(dados);
        return new DadosDetalhamentoCurso(curso);
    }

    public Page<DadosListagemCurso> cursosPorFormacao(Pageable paginacao, Long formacaoId) {

        log.debug("Buscando cursos da formação informada");
        return cursoRepository.findByFormacaoId(formacaoId, paginacao)
                .map(DadosListagemCurso::new);
    }

    public DadosDetalhamentoCurso detalharCurso(Long formacaoId, Long cursoId) {
        var curso = cursoRepository.findByIdAndFormacaoId(cursoId, formacaoId)
                .orElseThrow(() -> new ValidacaoException("Curso não encontrado"));
        return new DadosDetalhamentoCurso(curso);
    }
}
