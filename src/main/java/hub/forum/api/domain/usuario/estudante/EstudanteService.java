package hub.forum.api.domain.usuario.estudante;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class EstudanteService {

        @Autowired
        private EstudanteRepository repository;

        public DadosDetalhamentoEstudante cadastrarEstudante(Long id, @Valid DadosCadastroEstudante dados) {

            return null;
        }

        public Page<DadosDetalhamentoEstudante> listarEstudantesMatriculados(Pageable paginacao) {

            return repository.listarEstudantesMatriculados(paginacao).map(DadosDetalhamentoEstudante::new);
        }
}
