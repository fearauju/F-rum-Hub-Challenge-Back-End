package hub.forum.api.domain.topico;

import hub.forum.api.domain.curso.CursoRepository;
import hub.forum.api.domain.usuario.UsuarioRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Transactional
    public DadosDetalhamentoTopico criarTopico(@Valid DadosCadastroTopico dados) {

        var usuario = usuarioRepository.getReferenceById(dados.usuarioID());

        log.debug("Verificando usuário");
        if(!usuario.obterTipoUsuario().podeAtualizarTopicos()){
            log.warn("Usuário {} não tem permissões para criar tópico", usuario.obterTipoUsuario());
            throw new ValidacaoException("Somente estudantes podem criar tópicos");
        }


        var curso = cursoRepository.findById(dados.cursoID())
                .orElseThrow(() -> new ValidacaoException("Curso não encontrado"));

        log.debug("Validando se o curso pertence à formação informada");
        if(!curso.getFormacao().getFormacao().equals(dados.formacao())) {
            throw new ValidacaoException("Curso não pertence à formação informada");
        }

        var topico = new Topico();
        topico.cadastrarTopico(dados);

        log.debug("salvando o objeto tópico no banco de dados");
        topicoRepository.save(topico);

        return new DadosDetalhamentoTopico(topico);
    }


    @Transactional
    public DadosDetalhamentoTopico atualizarTopico(Long topicoId, DadosAtualizacaoTopico dados) {

        log.debug("Buscando o tópico com ID {} para atualização", topicoId);

        // Carrega o tópico para atualização
        var topico = topicoRepository.getReferenceById(topicoId);

        log.debug("Atualizando informações do tópico");

        // Atualiza os campos do tópico
        topico.atualizarTopico(dados);

        log.info("Tópico com ID {} atualizado com sucesso", topicoId);

        return new DadosDetalhamentoTopico(topico);
    }

    @Transactional
    public void deletarTopico(Long topicoId) {

        if(!topicoRepository.existsById(topicoId)){
            throw new ValidacaoException("Tópico inexistente");
        }
        // Seria redudante exclusão lógica, em casos como funcionários faz sentido usá-la. Farei exclusão permanente de um tópico.
        topicoRepository.deleteById(topicoId);
    }

    @Transactional
    public Page<DadosListagemTopico> listarTopicos(Pageable paginacao) {

        return topicoRepository.findAll(paginacao).map(DadosListagemTopico::new);
    }
}
