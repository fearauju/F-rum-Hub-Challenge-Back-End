package hub.forum.api.domain.perfil.repository;

import hub.forum.api.domain.perfil.Perfil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PerfilRepository extends JpaRepository<Perfil,Long> {

    @Query("""
        SELECT p FROM Perfil p
        WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nomePerfil, '%'))
    """)
    Page<Perfil> findByListasDoNome(String nomePerfil, Pageable paginacao);

    boolean existsByIdAndUsuarioId(Long perfilid, Long usuarioid);

}
