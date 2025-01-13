package hub.forum.api.domain.topico.validacao;

import hub.forum.api.domain.topico.TopicoRepository;
import hub.forum.api.domain.util.ValidadorBase;
import hub.forum.api.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarTopicoExistente implements ValidadorBase<DadosValidacaoTopico> {

    @Autowired
    private TopicoRepository topicoRepository;

    @Override
    public void validar(DadosValidacaoTopico dados) {

        if (!topicoRepository.existsById(dados.topicoID())) {
            throw new ValidacaoException("Tópico não encontrado");
        }
    }
}
