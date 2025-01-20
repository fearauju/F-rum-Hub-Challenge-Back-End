package hub.forum.api.domain.topico.validacao;

import hub.forum.api.domain.topico.repository.TopicoRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarTopicoFechado  {

    @Autowired
    private TopicoRepository topicoRepository;


    public void validar(DadosValidacaoTopico dados) {

        if(dados.topicoId() != null) {
            if (topicoRepository.topicoResolvido(dados.topicoId())) {
                throw new ValidacaoException("Tópico já está fechado. Abra um novo tópico.");
            }
        }
    }
}