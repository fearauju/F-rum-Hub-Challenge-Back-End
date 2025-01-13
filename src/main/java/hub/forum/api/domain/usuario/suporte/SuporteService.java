package hub.forum.api.domain.usuario.suporte;

import hub.forum.api.domain.usuario.validacao_regras.DadosValidacaoUsuario;
import hub.forum.api.domain.util.ValidadorBase;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SuporteService {

    @Autowired
    private SuporteRepository repository;

    @Autowired
    private List<ValidadorBase<DadosValidacaoUsuario>> validadores;

    @Transactional
    public DadosDetalhamentoSuporte cadastrarSuporte(Long id, @Valid DadosCadastroSuporte dados) {

        var suporte = repository.findUsuarioSuporte(id);

        if(suporte == null){
            throw new ValidacaoException("Usuário não encontrado");
        }


        var validarDados = new DadosValidacaoUsuario(
                suporte.getId(),
                suporte.getLogin(),
                suporte.obterTipoUsuario()
        );

        validadores.forEach(v -> v.validar(validarDados));

        suporte.cadastrarSuporte(dados);
        repository.save(suporte);

        return new DadosDetalhamentoSuporte(suporte);
    }

    public Page<DadosDetalhamentoSuporte> listarEquipeSuporte(Pageable paginacao) {
        return repository.listaUsuariosSuporte(paginacao).map(DadosDetalhamentoSuporte::new);
    }
}
