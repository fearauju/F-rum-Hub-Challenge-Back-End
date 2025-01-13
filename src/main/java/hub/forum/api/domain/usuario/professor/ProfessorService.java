package hub.forum.api.domain.usuario.professor;

import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProfessorService {

    @Autowired
    private ProfessorRepository repository;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public DadosDetalhamentoProfessor cadastrarProfessor(Long usuarioId, DadosCadastroProfessor dados) {
        log.info("Iniciando cadastro do professor com dados: {}", dados);

        try {
            // Buscar usuário com lock otimista
            var usuario = em.find(Usuario.class, usuarioId);
            if (usuario == null) {
                throw new ValidacaoException("Usuário não encontrado");
            }

            Professor professor;
            if (usuario instanceof Professor) {
                professor = (Professor) usuario;
            } else {
                professor = new Professor();
                BeanUtils.copyProperties(usuario, professor, "id", "version");
                professor.setId(usuario.getId());
                professor.setVersion(usuario.getVersion()); // Copiar versão atual
            }

            professor.cadastrarProfessor(dados);

            // Sincronizar estado e versão
            em.flush();
            em.clear();

            // Buscar professor atualizado
            professor = em.find(Professor.class, usuarioId);

            log.info("Professor cadastrado com sucesso: {}", professor);
            return new DadosDetalhamentoProfessor(professor);

        } catch (Exception e) {
            log.error("Erro ao cadastrar professor: ", e);
            throw new ValidacaoException("Erro ao cadastrar professor: " + e.getMessage());
        }
    }

    public Page<DadosDetalhamentoProfessor> listarEquipeProfessores(Pageable paginacao) {
        return repository.listarusuariosProfessores(paginacao).map(DadosDetalhamentoProfessor::new);
    }
}
