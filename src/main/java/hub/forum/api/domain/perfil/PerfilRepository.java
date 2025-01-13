package hub.forum.api.domain.perfil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil,Long> {

    @Query("""
        SELECT p FROM Perfil p
        WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nomePerfil, '%'))
    """)
    Page<Perfil> findByListasDoNome(String nomePerfil, Pageable paginacao);

    boolean existsByIdAndUsuarioId(Long perfilID, Long usuarioID);

}
