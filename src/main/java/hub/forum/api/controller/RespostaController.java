package hub.forum.api.controller;

import hub.forum.api.domain.resposta.dto.DadosDetalhamentoResposta;
import hub.forum.api.domain.resposta.dto.DadosListagemResposta;
import hub.forum.api.domain.resposta.dto.DadosRegistroReposta;
import hub.forum.api.domain.resposta.service.RespostaService;
import hub.forum.api.domain.topico.dto.DadosFechamentoTopico;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.util.PageResponse;
import hub.forum.api.infra.security.anotacoes.AutorizacaoAtualizarResposta;
import hub.forum.api.infra.security.anotacoes.AutorizacaoEscolherResolvido;
import hub.forum.api.infra.security.anotacoes.AutorizacaoResponderTopicos;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("topicos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Respostas", description = "Endpoints para gerenciamento de respostas dos tópicos")
@Slf4j
public class RespostaController {

    @Autowired
    private RespostaService service;


    @PostMapping("/{topicoId}/respostas")
    @Operation(summary = "Registrar nova resposta")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Resposta registrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tópico não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @AutorizacaoResponderTopicos
    public ResponseEntity<DadosDetalhamentoResposta> salvarResposta(
            @RequestBody @Valid DadosRegistroReposta dados,
            @PathVariable Long topicoId,
            @AuthenticationPrincipal Usuario autorResposta,
            UriComponentsBuilder uriBuilder) {

        log.debug("Iniciando salvamento de resposta para tópico ID: {}", topicoId);
        var resposta = service.salvarResposta(dados, topicoId, autorResposta);

        var uri = uriBuilder.path("/topicos/{topicoId}/respostas/{id}")
                .buildAndExpand(topicoId, resposta.id())
                .toUri();

        log.info("Resposta ID: {} criada com sucesso para tópico ID: {}", resposta.id(), topicoId);
        return ResponseEntity.created(uri).body(resposta);
    }


    @PutMapping("/{topicoId}/{respostaId}/atualizar")
    @Operation(summary = "Atualizar resposta existente")
    @AutorizacaoAtualizarResposta
    public ResponseEntity<DadosDetalhamentoResposta> atualizarResposta(
            @PathVariable Long topicoId,
            @PathVariable Long respostaId,
            @RequestBody @Valid DadosRegistroReposta dados,
            @AuthenticationPrincipal Usuario autorResposta) {

       var respostaAtualizada = service.atualizarResposta(topicoId,respostaId,dados, autorResposta);
       return ResponseEntity.ok(respostaAtualizada);
    }


    @GetMapping("/{topicoId}/respostas")
    @Operation(summary = "Listar respostas do tópico")
    public ResponseEntity<PageResponse<DadosListagemResposta>> listarRespostasDoTopico(
            @PageableDefault(sort = {"dataCriacao"}, direction = Sort.Direction.DESC)
            Pageable paginacao,
            @PathVariable Long topicoId) {

        log.info("Listando respostas do tópico ID: {}", topicoId);
        var respostasTopico = service.listarRespostasDoTopico(topicoId, paginacao);
        return ResponseEntity.ok(new PageResponse<>(respostasTopico));
    }


    @PutMapping("/{topicoId}/{respostaId}/melhor-resposta")
    @Operation(summary = "Marcar resposta como solução")
    @AutorizacaoEscolherResolvido  // Já valida se é estudante e autor do tópico
    public ResponseEntity<DadosDetalhamentoResposta> marcarMelhorResposta(
            @PathVariable Long topicoId,
            @PathVariable Long respostaId,
            @RequestBody @Valid DadosFechamentoTopico dados,
            @AuthenticationPrincipal Usuario autorTopico) {

        //Se não for autor do tópico o acesso será negado. Verifique a anotação.
        log.info("Usuário com ID {} escolheu a resposta ID {} como solução para o tópico ID {}",
                autorTopico.getLogin(), respostaId, topicoId);

        var resposta = service.marcarMelhorResposta(topicoId, respostaId, dados, autorTopico);
        return ResponseEntity.ok(resposta);
    }
}
