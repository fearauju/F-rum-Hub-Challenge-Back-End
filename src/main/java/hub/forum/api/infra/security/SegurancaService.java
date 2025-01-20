package hub.forum.api.infra.security;

import hub.forum.api.domain.curso.repository.CursoRepository;
import hub.forum.api.domain.perfil.repository.PerfilRepository;
import hub.forum.api.domain.resposta.repository.RespostaRepository;
import hub.forum.api.domain.topico.repository.TopicoRepository;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.professor.Professor;
import hub.forum.api.infra.exceptions.ValidacaoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
public class SegurancaService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private RespostaRepository  respostaRepository;

    @Autowired
    private PerfilRepository perfilRepository;


    //Usuario
    public Usuario getUsuarioLogado() {
        try {
            return (Usuario) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
        } catch (Exception e) {
            log.error("Erro ao obter usuário logado", e);
            throw new ValidacaoException("Erro ao identificar usuário");
        }
    }


    public boolean podeAtualizarDadosUsuario(Long usuarioId) {

            if (!getUsuarioLogado().getId().equals(usuarioId)) {
                log.warn("Tentativa de atualização não autorizada: Usuário {} tentou atualizar dados do usuário {}",
                        getUsuarioLogado().getId(), usuarioId);

                return false;
            }

            return true;
    }

    //Perfil
    public boolean podeAtualizarPerfil(Long perfilId) {
        return perfilRepository.existsByIdAndUsuarioId(perfilId, getUsuarioLogado().getId());
    }

    //Curso
    public boolean podeAtualizarCurso(Long cursoId) {

        var curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new ValidacaoException("Curso não encontrado"));

        Set<Long> professorIds = curso.getProfessores().stream()
                .map(Professor::getId)
                .collect(Collectors.toSet());

        return cursoRepository.existsByIdAndProfessores(
                cursoId,
                professorIds,
                curso.getProfessores().size()
        );
    }


    //Tópico
    public boolean podeAtualizarTopico(Long topicoId) {
        return topicoRepository.findByIdAndAutorId(topicoId, getUsuarioLogado().getId()).isPresent();
    }


    //Resposta
    public boolean podeEscolherMelhorResposta(Long topicoId) {
        log.info("Verificando se o usuário é o autor do tópico");
        return topicoRepository.findByIdAndAutorId(topicoId, getUsuarioLogado().getId()).isPresent();
    }

    public boolean podeAtualizarResposta(Long topicoId,Long respostaId) {
        return respostaRepository.findByIdAndTopicoId(respostaId, topicoId).isPresent();
    }
}