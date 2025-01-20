package hub.forum.api.domain.usuario.suporte.validacao;

import hub.forum.api.domain.usuario.repository.UsuarioRepository;
import hub.forum.api.domain.usuario.suporte.repository.SuporteRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarSuporteJaCadastrado implements ValidadorSuporte {

    @Autowired
    private SuporteRepository suporteRepository;

    @Override
    public void validar(DadosValidacaoSuporte dados) {
        if (suporteRepository.existsById(dados.usuarioId())) {
            throw new ValidacaoException("Usuário já está cadastrado como suporte");
        }
    }
}