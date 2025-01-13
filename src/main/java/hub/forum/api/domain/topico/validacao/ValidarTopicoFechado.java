package hub.forum.api.domain.topico.validacao;

import hub.forum.api.domain.topico.TopicoRepository;
import hub.forum.api.domain.util.ValidadorBase;
import hub.forum.api.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarTopicoFechado implements ValidadorBase<DadosValidacaoTopico> {

    @Autowired
    private TopicoRepository topicoRepository;

    @Override
    public void validar(DadosValidacaoTopico dados) {
        if (topicoRepository.topicoResolvido(dados.topicoID())) {
            throw new ValidacaoException("Tópico já está fechado. Abra um novo tópico.");
        }
    }
}