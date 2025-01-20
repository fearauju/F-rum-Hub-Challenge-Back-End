package hub.forum.api.domain.usuario.administrador.service;

import hub.forum.api.domain.usuario.Usuario;
import hub.forum.api.domain.usuario.administrador.dto.DadosRegistroAcao;
import hub.forum.api.domain.usuario.administrador.repository.AdministradorRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdministradorService {

    @Autowired
    private AdministradorRepository repository;

    @Transactional
    public void registrarAcao(DadosRegistroAcao dados, Usuario usuario) {

        var administrador = repository.findByUsuarioAdministrador(usuario.getId());

        log.info("salvando registros das ações sistemicas");
        administrador.registrarAcao(dados);

        repository.save(administrador);
    }
}
