package hub.forum.api.controller;

import hub.forum.api.domain.resposta.*;
import hub.forum.api.infra.security.anotacoes.AutorizacaoAtualizarResposta;
import hub.forum.api.infra.security.anotacoes.AutorizacaoEscolherResolvido;
import hub.forum.api.infra.security.anotacoes.AutorizacaoResponderTopicos;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("topicos/")
@Slf4j
public class RespostaController {

    @Autowired
    private RespostaService service;


    @PostMapping("{topicoID}/respostas")
    @AutorizacaoResponderTopicos
    public ResponseEntity<DadosDetalhamentoResposta> salvarResposta(@RequestBody @Valid DadosRegistroReposta dados,
                                                                    @PathVariable Long topicoID) {
       var resposta = service.salvarResposta(dados, topicoID);
       return ResponseEntity.ok(resposta);
    }


    @PutMapping("{topicoID}/respostas")
    @AutorizacaoAtualizarResposta
    public ResponseEntity<DadosDetalhamentoResposta> atualizarResposta(@PathVariable Long topicoID,
                                                      @RequestBody @Valid DadosRegistroReposta dados) {

       var respostaAtualizada = service.atualizarResposta(topicoID,dados);
       return ResponseEntity.ok(respostaAtualizada);
    }


    @GetMapping("/{topicoID}/respostas")
    public ResponseEntity<Page<DadosListagemResposta>> listarRespostas(@PageableDefault(sort = {"dataCriacao"})
                                                                       Pageable paginacao, @PathVariable Long topicoID){

        var respostasTopico = service.listarRespostas(topicoID,paginacao);
        return ResponseEntity.ok(respostasTopico);
    }


    @PutMapping("/{topicoID}/respostas/{respostaID}")
    @AutorizacaoEscolherResolvido
    public ResponseEntity<?> marcarSolucao(@PathVariable Long topicoID, @PathVariable Long respostaID) {

        log.debug("Marcando resposta {} como solução do tópico {}", respostaID, topicoID);
        service.marcarMelhorResposta(topicoID, respostaID);
        return ResponseEntity.noContent().build();
    }
}
