package hub.forum.api.controller;

import hub.forum.api.domain.escola.EscolaRepository;
import hub.forum.api.domain.formacao.DadosCadastroFormacao;
import hub.forum.api.domain.formacao.DadosDetalhamentoFormacao;
import hub.forum.api.domain.formacao.Formacao;
import hub.forum.api.domain.formacao.FormacaoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/formacoes")
public class FormacaoController {

    @Autowired
    private FormacaoRepository formacaoRepository;

    @Autowired
    private EscolaRepository escolaRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<?>cadastrar(@RequestBody @Valid DadosCadastroFormacao dados, UriComponentsBuilder uriComponentsBuilder){

        var escola = escolaRepository.getReferenceById(dados.escola_id());

        var formacao = new Formacao(dados,escola);
        formacaoRepository.save(formacao);
        var uri = uriComponentsBuilder.path("/formacoes/{id}").buildAndExpand(formacao.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoFormacao(formacao));
    }
}
