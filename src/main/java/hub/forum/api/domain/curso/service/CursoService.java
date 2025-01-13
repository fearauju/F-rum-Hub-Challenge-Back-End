package hub.forum.api.domain.curso.service;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.curso.repository.CursoRepository;
import hub.forum.api.domain.curso.dto.DadosAtualizacaoCurso;
import hub.forum.api.domain.curso.dto.DadosDetalhamentoCurso;
import hub.forum.api.domain.curso.dto.DadosListagemCurso;
import hub.forum.api.domain.curso.dto.DadoscadastroCurso;
import hub.forum.api.domain.curso.validacao.DadosValidacaoCurso;
import hub.forum.api.domain.formacao.repository.FormacaoRepository;
import hub.forum.api.domain.util.ValidadorBase;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CursoService {

    @Autowired
    private FormacaoRepository formacaoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private  List<ValidadorBase<DadosValidacaoCurso>> validadores;


    @Transactional
    public DadosDetalhamentoCurso cadastrarCurso(
            Long formacaoID, DadoscadastroCurso dados) {

        var formacao = formacaoRepository.findById(formacaoID)
                .orElseThrow(() -> new ValidacaoException("Formação não encontrada"));

        var dadosValidacao = new DadosValidacaoCurso(
                dados.duracao(),
                dados.curso()
        );

        validadores.forEach(v -> v.validar(dadosValidacao));

        var curso = new Curso();
        curso.cadastrarCurso(dados, formacao);

        return new DadosDetalhamentoCurso(cursoRepository.save(curso));
    }


    @Transactional
    public DadosDetalhamentoCurso atualizarCurso(
            Long cursoID,
            DadosAtualizacaoCurso dados) {

        var curso = cursoRepository.findById(cursoID)
                .orElseThrow(() -> new ValidacaoException("Curso não encontrado"));

        curso.atualizarDadosCurso(dados);
        return new DadosDetalhamentoCurso(curso);
    }

    public Page<DadosListagemCurso> cursosPorFormacao(Pageable paginacao, Long formacaoID) {

        log.debug("Buscando cursos da formação informada");
        return cursoRepository.findByFormacaoId(formacaoID, paginacao)
                .map(DadosListagemCurso::new);
    }
}
