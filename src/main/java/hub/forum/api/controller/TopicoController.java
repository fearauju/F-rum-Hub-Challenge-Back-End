package hub.forum.api.controller;

import hub.forum.api.domain.topico.*;
import hub.forum.api.infra.security.anotacoes.AutorizacaoApagarTopicos;
import hub.forum.api.infra.security.anotacoes.AutorizacaoAtualizacaoTopico;
import hub.forum.api.infra.security.anotacoes.AutorizacaoCriarTopico;
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
    private TopicoService topicoService;

    @PostMapping
    @AutorizacaoCriarTopico
    public ResponseEntity<DadosDetalhamentoTopico> criarTopico(@RequestBody @Valid DadosCadastroTopico dados, UriComponentsBuilder uriComponentsBuilder) {
        // implementação

        log.debug("Validando os dados antes de criar o objeto tópico");
        var topico = topicoService.criarTopico(dados);

        var uri = uriComponentsBuilder.path("/topicos/{cursoID}").buildAndExpand(topico.topicoID()).toUri();
        return ResponseEntity.created(uri).body(topico);
    }

    @PutMapping("/{topicoId}/atualizar-topico")
    @AutorizacaoAtualizacaoTopico
    public ResponseEntity<DadosDetalhamentoTopico> atualizarTopico(@PathVariable Long topicoId, @Valid DadosAtualizacaoTopico dados) {

        log.debug("verificando o serviço para atualizar tópico");
       var topico  = topicoService.atualizarTopico(topicoId, dados);
        return ResponseEntity.ok(topico); // retornar dados detalhamento do tópico
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> ListarTopicos(@PageableDefault(size = 10, sort = {"dataCriacao"})Pageable paginacao){

         log.debug("Buscando tópicos e organizando por paginas");
          var page = topicoService.listarTopicos(paginacao);
          return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{topicoId}")
    @AutorizacaoApagarTopicos
    public ResponseEntity<?> deletarTopico(@PathVariable Long topicoId){

        topicoService.deletarTopico(topicoId);
        return ResponseEntity.noContent().build();
    }
}