package hub.forum.api.controller;

import hub.forum.api.domain.escola.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/escolas")
public class EscolaController {

    @Autowired
    EscolaService service;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoEscola> cadastrar(@RequestBody @Valid DadosCadastroEscola dados, UriComponentsBuilder uriComponentsBuilder){

        var detalhamentoEscola =  service.cadastrarEscola(dados);

        var uri = uriComponentsBuilder.path("/escolas/{id}").buildAndExpand(detalhamentoEscola.id_escola()).toUri();

        return  ResponseEntity.created(uri).body(new DadosDetalhamentoEscola(detalhamentoEscola));
    }
}
