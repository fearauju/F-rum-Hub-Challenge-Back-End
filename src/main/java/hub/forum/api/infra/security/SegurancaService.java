package hub.forum.api.infra.security;

import hub.forum.api.domain.curso.CursoRepository;
import hub.forum.api.domain.escola.EscolaRepository;
import hub.forum.api.domain.formacao.FormacaoRepository;
import hub.forum.api.domain.topico.TopicoRepository;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.UsuarioRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SegurancaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private FormacaoRepository formacaoRepository;

    @Autowired
    private EscolaRepository escolaRepository;


    //uso comum
    private Usuario getUsuarioLogado() {
        return (Usuario) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }


    //Escola

    public boolean podeAtualizarEscola(Long escolaId) {
        return escolaRepository.existsById(escolaId);
    }

    //Formação

    public boolean podeCadastrarFormacao(Long escolaId) {
        return escolaRepository.existsById(escolaId);
    }

    public boolean podeAtualizarFormacao(Long formacaoId) {
        return formacaoRepository.existsById(formacaoId);
    }


    // curso

    public boolean podeAtualizarCurso(Long cursoID, Long professorID) {
        return cursoRepository.existsByIdAndProfessorId(cursoID, professorID);
    }


    //Topico

    public boolean podeCriarTopico(String titulo, String mensagem) {

        if (topicoRepository.existsByTituloOrMensagem(titulo,mensagem)) {

            throw new ValidacaoException("Já exite um tópico com esse título ou com a mesma dúvida.");
        }

        return true;
    }

    public boolean podeAtualizarTopico(Long topicoID, Long usuarioID){

        return topicoRepository.findByIdAndAutorId(topicoID, usuarioID)
                .isPresent();
    }


    //Resposta
    public boolean podeResponderNoForum(Long topicoID){
// Verifica se o tópico está aberto (não resolvido)
        return !topicoRepository.isTopicoResolvido(topicoID);
    }

    public boolean podeEscolherMelhorResposta(Long topicoID, Long usuarioID){

        return topicoRepository.findByIdAndAutorId(topicoID, usuarioID)
                .isPresent();
    }

    public boolean podeAtualizarResposta(Long topicoID, Long usuarioID){

        return topicoRepository.findByIdAndAutorId(topicoID, usuarioID)
                .isPresent();
    }

}