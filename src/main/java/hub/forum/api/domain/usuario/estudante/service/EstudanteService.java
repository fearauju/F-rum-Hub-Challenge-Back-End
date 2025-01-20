package hub.forum.api.domain.usuario.estudante.service;

import hub.forum.api.domain.formacao.Formacao;
import hub.forum.api.domain.formacao.repository.FormacaoRepository;
import hub.forum.api.domain.matricula.Matricula;
import hub.forum.api.domain.matricula.repository.MatriculaCursoRepository;
import hub.forum.api.domain.perfil.Perfil;
import hub.forum.api.domain.perfil.repository.PerfilRepository;
import hub.forum.api.domain.usuario.estudante.Estudante;
import hub.forum.api.domain.usuario.estudante.dto.*;
import hub.forum.api.domain.usuario.estudante.repository.EstudanteRepository;
import hub.forum.api.domain.usuario.repository.UsuarioRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
@Slf4j
public class EstudanteService {

    @Autowired
    private EstudanteRepository estudanteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FormacaoRepository formacaoRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private MatriculaCursoRepository matriculaCursoRepository;

    @Transactional
    public DadosDetalhamentoEstudante cadastrarEstudante(Long usuarioId, DadosCadastroEstudante dados) {

            // Buscar usuário existente
            var usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> {
                        log.error("Não foi encontrado um usuário cadastrado com este ID {}", usuarioId);
                        return new ValidacaoException("Usuário não encontrado");
                    });

            // Verificar se já existe um estudante
            if (estudanteRepository.existsById(usuarioId)) {
                throw new ValidacaoException("Usuário já cadastrado como estudante");
            }

            // Criar e persistir Perfil
            Perfil perfil = new Perfil();
            perfil.setUsuario(usuario);
            perfil.setNome(dados.nome());
            perfil.setDescricaoPessoal(dados.descricaoPessoal());
            perfil.setDataNascimento(dados.dataNascimento());
            perfilRepository.save(perfil);

            // Atualizar a referência do perfil no usuário
            usuario.setPerfil(perfil);
            usuarioRepository.save(usuario);

            // Criar e configurar Estudante
            Estudante estudante = new Estudante();
            estudante.setUsuario(usuario);
            estudante.cadastrarEstudante(dados);

        // Buscar apenas as formações da área escolhida
        List<Formacao> formacoesAreaEscolhida = formacaoRepository
                .findByEscolaAreaFormacao(dados.areaFormacao());

        if (formacoesAreaEscolhida.isEmpty()) {
            log.error("Nenhuma formação encontrada para a área: {}", dados.areaFormacao());
            throw new ValidacaoException(
                    "Nenhuma formação disponível para a área: " + dados.areaFormacao().getDescricao()
            );
        }

        // Criar matrículas apenas para formações da área escolhida
        for (Formacao formacao : formacoesAreaEscolhida) {
            Matricula matricula = new Matricula();
            matricula.cadastrarMatriculaEstudante(formacao, estudante);
            estudante.getMatriculas().add(matricula);
        }

            // Persistir o estudante
            estudante = estudanteRepository.save(estudante);

            log.info("Estudante {} cadastrado com sucesso com ID {}", estudante, estudante.getId());
            return new DadosDetalhamentoEstudante(estudante);
    }

        public Page<DadosDetalhamentoEstudante> listarEstudantesMatriculados (Pageable paginacao){

            return estudanteRepository.listarEstudantesMatriculados(paginacao).map(DadosDetalhamentoEstudante::new);
        }

    public DadosDetalhamentoEstudante buscarEstudante(Long estudanteId) {

        var estudante = estudanteRepository.findByEstudante(estudanteId);

        return new DadosDetalhamentoEstudante(estudante);
    }

    public DadosDetalhamentoEstudante atualizar(@Valid DadosAtualizacaoEstudante dados, Long usuarioId) {

        if(dados.interessesAcademicos() == null && dados.nivelAcademico() == null){
            throw new ValidacaoException("Nenhum dado foi fornecido para atualização");
        }

        var estudante = estudanteRepository.findById(usuarioId)
                .orElseThrow(() -> {
                    log.error("Não foi encontrado um estudante registrado com o ID {}", usuarioId);
                    return new ValidacaoException("Estudante não encontrado");
                });

        estudante.atualizarEstudante(dados);
        estudanteRepository.save(estudante);

        return  new DadosDetalhamentoEstudante(estudante);
    }

    @Transactional
    public void registrarConclusaoCurso(Long estudanteId, Long cursoId) {
        var estudante = estudanteRepository.findById(estudanteId)
                .orElseThrow(() -> new ValidacaoException("Estudante não encontrado"));

        var matriculaCurso = matriculaCursoRepository
                .findByMatriculaEstudanteIdAndCursoId(estudanteId, cursoId)
                .orElseThrow(() -> new ValidacaoException("Matrícula no curso não encontrada"));

        if (!matriculaCurso.isConcluido()) {
            matriculaCurso.concluir();
            estudante.registrarCursoConcluido();
            estudante.atualizarProgressoCurso((int) matriculaCurso.getCurso().getDuracao().toHours());

            estudanteRepository.save(estudante);
            matriculaCursoRepository.save(matriculaCurso);
        }
    }

    public DadosProgressoEstudante obterProgresso(Long estudanteId) {
        var estudante = estudanteRepository.findById(estudanteId)
                .orElseThrow(() -> new ValidacaoException("Estudante não encontrado"));

        return new DadosProgressoEstudante(
                estudante.getCargaHorariaConcluida(),
                estudante.getCursosConcluidos(),
                estudante.getMediaAvaliacoes(),
                estudante.getBadgesConquistadas().size(),
                estudante.getUltimaAtividade()
        );
    }

    public Set<DadosDetalhamentoBadges> listarBadges(Long estudanteId) {

        return estudanteRepository.findAllConquistas(estudanteId);
    }
}

