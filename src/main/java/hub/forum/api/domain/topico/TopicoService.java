package hub.forum.api.domain.topico;

import hub.forum.api.domain.curso.CursoRepository;
import hub.forum.api.domain.topico.validacao.DadosValidacaoTopico;
import hub.forum.api.domain.usuario.TipoUsuario;
import hub.forum.api.domain.usuario.UsuarioRepository;
import hub.forum.api.domain.validacao.ValidadorBase;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private List<ValidadorBase<DadosValidacaoTopico>> validadores;

    @Transactional
    public DadosDetalhamentoTopico criarTopico(@Valid DadosCadastroTopico dados) {

        var dadosValidacao = new DadosValidacaoTopico(
                null,
                dados.usuarioID(),
                dados.cursoID(),
                dados.formacao(),
                dados.titulo(),
                dados.mensagem(),
                false
        );

        validadores.forEach(v -> v.validar(dadosValidacao));

        var curso = cursoRepository.getReferenceById(dados.cursoID());


        log.debug("buscando autor do tópico");
        var autor = usuarioRepository.getReferenceById(dados.usuarioID());

        var topico = new Topico();
        log.info("Iniciando cadastro do tópico");
        topico.cadastrarTopico(dados, curso, autor);

        log.info("salvando o objeto tópico no banco de dados");
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
    public void deletarTopico(Long topicoID) {


        var DadosValidacao = new DadosValidacaoTopico(
                topicoID,
                null,
                null,
                null,
                null,
                null,
                false
        );

        validadores.forEach(v -> v.validar(DadosValidacao));

        // A anotação @AutorizacaoApagarTopicos já verifica existência e permissão

        var topico = topicoRepository.getReferenceById(topicoID);
        // Seria redudante exclusão lógica
        // Farei exclusão permanente de um tópico.


        topicoRepository.deleteById(topico.getId());
    }


    public Page<DadosListagemTopico> listarTopicosNaoResolvidos(Pageable paginacao) {

        return topicoRepository.topicosNaoResolvidos(paginacao).map(DadosListagemTopico::new);
    }


    public Page<DadosListagemTopico> listarTopicosResolvidos(Pageable paginacao) {

        return topicoRepository.topicosResolvidos(paginacao).map(DadosListagemTopico::new);
    }


    public Integer totalTopicosNaoResolvidos() {

        var totalNaoResolvidos = topicoRepository.totalTopicosNaoResolvidos();

        if(totalNaoResolvidos == null){
            return 0;
        }

        return totalNaoResolvidos;
    }


    public Integer totalTopicosResolvidos() {

        var totalResolvidos = topicoRepository.totalTopicosResolvidos();

        if(totalResolvidos == null){
            return 0;
        }

        return totalResolvidos;
    }

    public Page<Topico> topicoSemRespostas(Pageable paginacao) {

        return topicoRepository.listarTopicosSemRespostas(paginacao);
    }


    @Transactional
    public void fecharTopico(Long topicoID, DadosFechamentoTopico dados) {

        var dadosValidacao = new DadosValidacaoTopico(
                topicoID,
                dados.usuarioID(),
                null,
                null,
                null,
                null,
                false
        );

        validadores.forEach(v -> v.validar(dadosValidacao));

        var usuario = usuarioRepository.findById(dados.usuarioID())
                .orElseThrow(() ->
                        new ValidacaoException("Usuário não encontrado"));

      var topico = topicoRepository.getReferenceById(topicoID);
      topico.marcarComoResolvido();

      if(usuario.obterTipoUsuario() == TipoUsuario.SUPORTE){

         var suporte = usuarioRepository.findUsuarioSuporte(usuario.getId());
         suporte.adicionarCasosResolvidos(dados, suporte);
          usuarioRepository.save(suporte);
      }

      topicoRepository.save(topico);

    }
}
