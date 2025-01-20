package hub.forum.api.domain.perfil.validacao;

import hub.forum.api.domain.perfil.OperacaoPerfil;
import hub.forum.api.domain.usuario.repository.UsuarioRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class validarIDInformadoExiste implements ValidadorPerfil<DadosValidacaoPerfil> {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void validar(DadosValidacaoPerfil dados, OperacaoPerfil operacao) {

        // Q: Verifica se o ID do usuário existe
        var usuarioAlvo = usuarioRepository.findByIdAndAtivoTrue(dados.usuarioId());

        if(usuarioAlvo == null){
            log.error("Tentativa de criar perfil para usuário inexistente ID: {}", dados.usuarioId());
            throw new ValidacaoException("Usuário não encontrado");
        }
    }
}
