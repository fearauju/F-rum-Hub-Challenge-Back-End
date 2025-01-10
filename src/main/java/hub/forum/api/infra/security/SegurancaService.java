package hub.forum.api.infra.security;

import hub.forum.api.domain.curso.CursoRepository;
import hub.forum.api.domain.perfil.PerfilRepository;
import hub.forum.api.domain.resposta.RespostaRepository;
import hub.forum.api.domain.topico.TopicoRepository;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.infra.exceptions.ValidacaoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


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

    public boolean podeAtualizarDadosUsuario(Long usuarioID) {

            if (!getUsuarioLogado().getId().equals(usuarioID)) {
                log.warn("Tentativa de atualização não autorizada: Usuário {} tentou atualizar dados do usuário {}",
                        getUsuarioLogado().getId(), usuarioID);

                return false;
            }

            return true;
    }

    //Perfil
    public boolean podeAtualizarPerfil(Long perfilID) {
        return perfilRepository.existsByIdAndUsuarioId(perfilID, getUsuarioLogado().getId());
    }

    //Curso
    public boolean podeAtualizarCurso(Long cursoID, Long professorID) {
        return cursoRepository.existsByIdAndProfessorId(cursoID, professorID);
    }


    //Tópico
    public boolean podeAtualizarTopico(Long topicoID) {
        return topicoRepository.findByIdAndAutorId(topicoID, getUsuarioLogado().getId()).isPresent();
    }


    //Resposta
    public boolean podeEscolherMelhorResposta(Long topicoID) {
        return topicoRepository.findByIdAndAutorId(topicoID, getUsuarioLogado().getId()).isPresent();
    }

    public boolean podeAtualizarResposta(Long topicoID,Long respostaID) {
        return respostaRepository.findByIdAndTopicoId(respostaID, topicoID).isPresent();
    }
}