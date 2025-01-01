package hub.forum.api.infra.security.anotacoes;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('ESTUDANTE','PROFESSOR','SUPORTE') and @Seguranca.Service.podeResponderNoForum(authentication.principal.id)")
public @interface AutorizacaoResponderTopicos {
}
