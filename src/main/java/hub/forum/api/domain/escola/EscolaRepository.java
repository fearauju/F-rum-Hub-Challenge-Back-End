package hub.forum.api.domain.escola;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EscolaRepository extends JpaRepository<Escola,Long> {


}
