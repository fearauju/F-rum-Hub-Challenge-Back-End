package hub.forum.api.domain.escola;

import hub.forum.api.domain.usuario.TipoUsuario;
import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.UsuarioRepository;
import hub.forum.api.domain.usuario.ValidarPermissoesDeAcesso.ValidarPermissoesUsuario;
import hub.forum.api.infra.exceptions.ValidacaoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class EscolaService {

    @Autowired
    private EscolaRepository escolaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    List<ValidarPermissoesUsuario> validarPermissao;

    private TipoUsuario obterTipoUsuario(Long userId) {
        return usuarioRepository.findById(userId)
                .map(Usuario::obterTipoUsuario)
                .orElseThrow(() -> new ValidacaoException("Usuário não encontrado"));
    }

    public DadosDetalhamentoEscola cadastrarEscola(DadosCadastroEscola dados){

        log.debug("Iniciando cadastro de escola para usuário ID: {}", dados.id_usuario());

        if (!usuarioRepository.existsById(dados.id_usuario())) {
            log.error("Usuário não encontrado: {}", dados.id_usuario());
            throw new ValidacaoException("Usuário inexistente");
        }

        try {
            TipoUsuario tipoUsuario = obterTipoUsuario(dados.id_usuario());
            log.debug("Tipo de usuário encontrado: {}", tipoUsuario);

            validarPermissao.forEach(v -> v.validarPermissoes(dados.id_usuario(), tipoUsuario));

            Escola escola = new Escola(dados.nomeEscola());
            escolaRepository.save(escola);

            log.info("Escola cadastrada com sucesso: {}", escola.getId());
            return new DadosDetalhamentoEscola(dados.id_usuario(), escola.getId(), escola.getNomeEscola());

        } catch (Exception e) {
            log.error("Erro ao cadastrar escola: ", e);
            throw e;
        }
    }
}
