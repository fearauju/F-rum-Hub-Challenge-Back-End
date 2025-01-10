package hub.forum.api.controller;

import hub.forum.api.domain.topico.*;
import hub.forum.api.domain.topico.DadosFechamentoTopico;
import hub.forum.api.infra.security.anotacoes.AutorizacaoApagarTopicos;
import hub.forum.api.infra.security.anotacoes.AutorizacaoAtualizarTopico;
import hub.forum.api.infra.security.anotacoes.AutorizacaoCriarTopico;
import hub.forum.api.infra.security.anotacoes.AutorizacaoFecharTopico;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/topicos")
@Slf4j
public class TopicoController {

    @Autowired
    private TopicoService service;

    @PostMapping
    @AutorizacaoCriarTopico
    public ResponseEntity<DadosDetalhamentoTopico> criarTopico(@RequestBody @Valid DadosCadastroTopico dados, UriComponentsBuilder uriComponentsBuilder) {
        // implementação

        log.debug("Validando os dados antes de criar o objeto tópico");
        var topico = service.criarTopico(dados);

        var uri = uriComponentsBuilder.path("/topicos/{cursoID}").buildAndExpand(topico.topicoID()).toUri();
        return ResponseEntity.created(uri).body(topico);
    }

    @PutMapping("/{topicoID}/atualizar")
    @AutorizacaoAtualizarTopico
    public ResponseEntity<DadosDetalhamentoTopico> atualizarTopico(@PathVariable Long topicoId, @Valid DadosAtualizacaoTopico dados) {

        log.debug("verificando o serviço para atualizar tópico");
        var topico  = service.atualizarTopico(topicoId, dados);
        return ResponseEntity.ok(topico); // retornar dados detalhamento do tópico
    }

    @GetMapping("nao_resolvidos")
    public ResponseEntity<Page<DadosListagemTopico>> listarTopicosNaoResolvidos(@PageableDefault(sort = {"dataCriacao"})Pageable paginacao){

         log.debug("Buscando tópicos não resolvidos e organizando por paginas");
         var topicosNaoResolvidos = service.listarTopicosNaoResolvidos(paginacao);
         return ResponseEntity.ok(topicosNaoResolvidos);
    }

    @GetMapping("/resolvidos")
    public ResponseEntity<Page<DadosListagemTopico>> listarTopicosResolvidos(@PageableDefault(sort = {"dataCriacao"}) Pageable paginacao){

        log.debug("Buscando tópicos resolvidos e organizando por paginas");
        var topicosResolvidos = service.listarTopicosResolvidos(paginacao);
        return ResponseEntity.ok(topicosResolvidos);
    }

    @GetMapping("total_nao_resolvidos")
    public ResponseEntity<Integer> totalTopicosNaoResolvidos(){

        var total = service.totalTopicosNaoResolvidos();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total_resolvidos")
    public ResponseEntity<Integer> totalTopicosResolvidos(){
        var total = service.totalTopicosResolvidos();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/sem_respostas")
    public ResponseEntity<Page<Topico>> topicoSemResposta(@PageableDefault(sort = {"dataCriacao"}) Pageable paginacao){

        var topico = service.topicoSemRespostas(paginacao);
        return  ResponseEntity.ok(topico);
    }

    @DeleteMapping("/{topicoID}")
    @AutorizacaoApagarTopicos
    public ResponseEntity<?> deletarTopico(@PathVariable Long topicoId){

        log.debug("Buscando tópico para deletar");
        service.deletarTopico(topicoId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{topicoID}/fechar")
    @AutorizacaoFecharTopico
    public ResponseEntity<String> fecharTopico(@PathVariable Long topicoID, @Valid DadosFechamentoTopico dados){

        log.debug("Validando tópico antes de fechar");
        service.fecharTopico(topicoID, dados);
        return ResponseEntity.ok("Tópico Fechado");
    }
}