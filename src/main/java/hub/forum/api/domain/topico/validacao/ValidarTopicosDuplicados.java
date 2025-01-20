package hub.forum.api.domain.topico.validacao;

import hub.forum.api.domain.curso.repository.CursoRepository;
import hub.forum.api.domain.formacao.repository.FormacaoRepository;
import hub.forum.api.domain.topico.repository.TopicoRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ValidarTopicosDuplicados implements ValidadorTopico {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository  cursoRepository;

    @Override
    public void validar(DadosValidacaoTopico dados) {
        var tituloNormalizado = normalizarTexto(dados.titulo());
        var mensagemNormalizada = normalizarTexto(dados.mensagem());

        // Busca o curso e a sua formação
        var curso = cursoRepository.findById(dados.cursoId())
                .orElseThrow(() -> new ValidacaoException("Curso não encontrado"));

        var topicoExiste = topicoRepository.existsByTituloEMensagemNaFormacao(
                tituloNormalizado,
                mensagemNormalizada,
                curso.getFormacao().getId() // Usa o ID da formação do curso
        );

        if (topicoExiste) {
            log.warn("Tentativa de criar tópico duplicado. Título: {}, Curso: {}, Formação: {}",
                    dados.titulo(), dados.cursoId(), curso.getFormacao().getFormacao());
            throw new ValidacaoException("Já existe um tópico similar cadastrado nesta formação");
        }
    }

    private String normalizarTexto(String texto) {
        return texto.toLowerCase()
                .trim()
                .replaceAll("\\s+", " ");
    }
}

