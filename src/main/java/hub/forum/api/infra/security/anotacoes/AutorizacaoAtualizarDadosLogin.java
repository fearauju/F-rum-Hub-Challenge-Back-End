package hub.forum.api.infra.security.anotacoes;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
// hasAnyRole: verifica MÚLTIPLOS papéis (OU lógico)
@PreAuthorize("hasAnyRole('ADMINISTRADOR','SUPORTE','ESTUDANTE','PROFESSOR') and" +
        " @segurancaService.podeAtualizarDadosUsuario(#id)")
public @interface AutorizacaoAtualizarDadosLogin {
}
