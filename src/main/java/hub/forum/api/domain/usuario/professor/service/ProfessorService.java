package hub.forum.api.domain.usuario.professor.service;

import hub.forum.api.domain.usuario.OperacaoDominio;
import hub.forum.api.domain.usuario.professor.Professor;
import hub.forum.api.domain.usuario.professor.dto.DadosAtualizacaoProfessor;
import hub.forum.api.domain.usuario.professor.dto.DadosCadastroProfessor;
import hub.forum.api.domain.usuario.professor.dto.DadosDetalhamentoProfessor;
import hub.forum.api.domain.usuario.professor.repository.ProfessorRepository;
import hub.forum.api.domain.usuario.professor.validacao.DadosValidacaoProfessor;
import hub.forum.api.domain.usuario.professor.validacao.ValidadorProfessor;
import hub.forum.api.domain.usuario.repository.UsuarioRepository;
import hub.forum.api.domain.usuario.validacao.DadosValidacaoUsuario;
import hub.forum.api.domain.usuario.validacao.ValidadorBase;
import hub.forum.api.domain.util.PageResponse;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;


@Service
@Slf4j
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private UsuarioRepository  usuarioRepository;

    @Autowired
    @Qualifier("validadorUsuarioExistente")
    private ValidadorBase<DadosValidacaoUsuario> validadorBase;

    @Autowired
    private List<ValidadorProfessor> validadores;



    @Transactional
    public DadosDetalhamentoProfessor cadastrarProfessor(Long usuarioId, DadosCadastroProfessor dados) {
        log.info("Iniciando cadastro do professor para usuário ID: {}", usuarioId);

        // Valida existência do usuário usando o validador base
        validadorBase.validar(
                new DadosValidacaoUsuario(usuarioId, null, null, null, null, OperacaoDominio.CADASTRO_PROFESSOR),
                OperacaoDominio.CADASTRO_PROFESSOR
        );

        // Validações específicas do professor
        var dadosValidacao = new DadosValidacaoProfessor(
                usuarioId,
                new HashSet<>(dados.especializacoes()),
                dados.titularidadeAcademica(),
                dados.anosExperiencia(),
                dados.dataAdmissao());

        validadores.forEach(v -> v.validar(dadosValidacao));

        var usuario = usuarioRepository.getReferenceById(usuarioId);
        var professor = new Professor();
        professor.setUsuario(usuario);
        professor.cadastrarProfessor(dados);

        professorRepository.save(professor);

        log.info("Professor cadastrado com sucesso: ID {}", professor.getId());
        return new DadosDetalhamentoProfessor(professor);
    }

    @Transactional
    public PageResponse<DadosDetalhamentoProfessor> listarEquipeProfessores(Pageable paginacao) {
        log.debug("Listando professores. Página: {}, Tamanho: {}",
                paginacao.getPageNumber(),
                paginacao.getPageSize());

        var page = professorRepository.listarusuariosProfessores(paginacao)
                .map(DadosDetalhamentoProfessor::new);

        log.info("Encontrados {} professores", page.getTotalElements());
        return new PageResponse<>(page);
    }

    @Transactional
    public DadosDetalhamentoProfessor atualizarCadastro(DadosAtualizacaoProfessor dados, Long professorId) {

        if (dados.titularidadeAcademica() == null && dados.especializacoes() == null) {
            throw new ValidacaoException("Nenhum dado foi fornecido para atualização");
        }

        var professor = professorRepository.findById(professorId)
                .orElseThrow(() -> {
                    log.error("Não foi encontrado professor com ID {}", professorId);
                   return new ValidacaoException("Professor não encontrado");
                });

        professor.atualizar(dados);

        log.info("Professor ID {} atualizado com sucesso", professorId);
        return new DadosDetalhamentoProfessor(professor);
    }
}
