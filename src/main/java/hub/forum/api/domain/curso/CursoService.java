package hub.forum.api.domain.curso;

import hub.forum.api.domain.formacao.FormacaoRepository;
import hub.forum.api.domain.usuario.UsuarioRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CursoService {

    @Autowired
    private FormacaoRepository formacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;


    @Transactional
    public Curso cadastrarCurso(DadoscadastroCurso dados) {

        var usuario = usuarioRepository.getReferenceById(dados.usuario_id());

        log.debug("Buscando a formação que contêm este curso");
        var formacao = formacaoRepository.getReferenceById(dados.formacao_id());

        var curso = new Curso(dados, formacao);

        log.debug("Cadastrando curso no banco de dados");
        return cursoRepository.save(curso);

    }

        @Transactional
        public DadosDetalhamentoCurso atualizarCurso(@Valid DadosAtualizacaoCurso dados) {

            // Validações iniciais
            log.debug("Verificando se a formação existe");
            if (!formacaoRepository.existsById(dados.formacao_id())) {
                throw new ValidacaoException("Formação não encontrada");
            }

            log.debug("Verificando se o curso existe dentro da formação informada");
            if (!cursoRepository.existsByIdINFormacao(dados.formacao_id(), dados.curso_id())) {
                throw new ValidacaoException("Curso não encontrado na formação informada");
            }

            // Carrega o curso existente do banco
            var curso = cursoRepository.getReferenceById(dados.curso_id());

            // Atualiza com os novos dados
            curso.atualizarDadosCurso(dados);

            log.debug("Retornando o objeto curso após a conversão para dados DTO");
            return new DadosDetalhamentoCurso(curso);

            //Como está numa transação a JPA monitora qualquer alteração
            // após os dados serem carregados do banco de dados
            // e faz o update automátco com as mudanças.

          // O fluxo correto é:

            // - Receber dados do JSON

            // - Validar permissões e regras

            // - Carregar entidade do banco

            // - Atualizar dados da entidade

            // - Deixar o JPA fazer o update

          //  Não é necessário usar save() porque o objeto carregado do banco já está sendo gerenciado pela JPA.
        }

    public Page<DadosListagemCurso> listarPorFormacao(Pageable paginacao, Long formacaoId) {

        log.debug("Buscando cursos da formação informada");
        return cursoRepository.findByFormacaoId(formacaoId, paginacao)
                .map(DadosListagemCurso::new);

        //GET /cursos/formacao/1?page=0&size=10&sort=curso,asc
    }
}
