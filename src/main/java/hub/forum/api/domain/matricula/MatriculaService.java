package hub.forum.api.domain.matricula;

import hub.forum.api.domain.usuario.TipoUsuario;
import hub.forum.api.domain.usuario.UsuarioRepository;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public boolean verificarStatusMatricula(Long estudanteId) {

        log.debug("Verificando matrícula ativa para estudante {}", estudanteId);
        var matricula = matriculaRepository.findMatriculaByEstudanteId(estudanteId);

        if (matricula == null) {
            log.warn("Nenhuma matrícula encontrada para o estudante {}", estudanteId);
            return true;
        }

        var hoje = LocalDateTime.now();
        return !matricula.getExpiracaoAssinatura().isAfter(hoje);
    }


    public DadosDetalhamentoMatricula atualizarMatriculaEstudante(@Valid DadosEstudanteMatriculado dados) {

        var usuario = usuarioRepository.getReferenceById(dados.usuario_Id());


        if(!usuarioRepository.existsById(dados.estudante_Id())){
            throw new ValidacaoException("Estudante não encontrado");
        }

        var estudante = usuarioRepository.getReferenceById(dados.estudante_Id());

        if(estudante.obterTipoUsuario() != TipoUsuario.ESTUDANTE){
            throw new ValidacaoException("Este usuário não é um estudante");
        }

        var hoje = LocalDateTime.now();

        return matriculaRepository.UpdateDataAssinaturaById(hoje,estudante.getId());
    }
}
