package hub.forum.api.domain.perfil.validacao;

import hub.forum.api.domain.perfil.PerfilRepository;
import hub.forum.api.domain.validacao.ValidadorBase;
import hub.forum.api.infra.exceptions.ValidacaoException;
import hub.forum.api.infra.security.SegurancaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ValidarProprietarioPerfil implements ValidadorBase<DadosValidacaoPerfil> {

    @Autowired
    private SegurancaService segurancaService;

    @Override
    public void validar(DadosValidacaoPerfil dados) {

        if (!segurancaService.getUsuarioLogado().getId().equals(dados.usuarioID())) {

            log.warn("Usuário {} tentou alterar os dados de perfil do usuário com ID: {}",
                    segurancaService.getUsuarioLogado().getLogin(), dados.usuarioID());
            throw new ValidacaoException("Usuário não pode fazer alterações neste perfil");
        }
    }
}
