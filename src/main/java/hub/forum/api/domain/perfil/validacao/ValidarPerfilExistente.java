package hub.forum.api.domain.perfil.validacao;

import hub.forum.api.domain.perfil.OperacaoPerfil;
import hub.forum.api.domain.perfil.repository.PerfilRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ValidarPerfilExistente implements ValidadorPerfil<DadosValidacaoPerfil> {

    @Autowired
    private PerfilRepository perfilRepository;

    @Override
    public void validar(DadosValidacaoPerfil dados, OperacaoPerfil operacao) {
        if (operacao == OperacaoPerfil.CRIACAO) {
            if (perfilRepository.existsById(dados.usuarioId())) {
                log.warn("Tentativa de criar perfil duplicado para usuário ID: {}", dados.usuarioId());
                throw new ValidacaoException("Este usuário já possui um perfil cadastrado");
            }
        }
    }
}
