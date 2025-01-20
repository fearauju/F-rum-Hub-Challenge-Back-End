package hub.forum.api.infra.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import hub.forum.api.domain.escola.AreaFormacao;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class TratadorDeErros {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErroValidacaoDto>> tratarErro400(MethodArgumentNotValidException ex){

        var erroInterno = ex.getFieldErrors()
                .stream()
                .map(DadosErroValidacao::new)
                .toList();

        var erroExposto = erroInterno
                .stream()
                .map(e -> new ErroValidacaoDto(e.campo(),e.mensagemErro()))
                .toList();

        return ResponseEntity.badRequest().body(erroExposto);
    }


    @Setter
    @Getter
    public static class ErroValidacaoDto{

        private String campo;
        private String mensagemErro;

        public ErroValidacaoDto(String campo, String mensagemErro){
            this.campo = campo;
            this.mensagemErro = mensagemErro;
        }
    }

    private record DadosErroValidacao(String campo, String mensagemErro){

        public DadosErroValidacao(FieldError fieldError){
            this(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }


    @Data
    @AllArgsConstructor
    public static class ErrorResponse {
        private int status;
        private String message;
        private LocalDateTime timestamp;
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<String> tratarValidacaoException(ValidacaoException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> tratarErroAcessoNegado(AccessDeniedException ex) {
        log.error("Acesso negado: ", ex);

        var errorResponse = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Acesso negado. Você não tem permissão para realizar esta operação.",
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> tratarErroAuthentication(AuthenticationException ex) {
        log.error("Falha na autenticação: ", ex);

        int status = HttpStatus.UNAUTHORIZED.value();
        String mensagem = "Falha na autenticação.";

        // Verifica se a exceção é InternalAuthenticationServiceException para obter a causa
        if (ex instanceof InternalAuthenticationServiceException && ex.getCause() != null) {
            Throwable causa = ex.getCause();
            if (causa instanceof FalhasDeLoginExceptions) {
                status = HttpStatus.TOO_MANY_REQUESTS.value(); // 429
                mensagem = causa.getMessage();
            } else if (causa instanceof AutenticacaoException) {
                mensagem = causa.getMessage();
            } else if (causa instanceof UsernameNotFoundException) {
                mensagem = "Usuário não encontrado.";
            }
        } else {
            // Caso a exceção diretamente seja uma das específicas
            if (ex instanceof FalhasDeLoginExceptions) {
                status = HttpStatus.TOO_MANY_REQUESTS.value(); // 429
                mensagem = ex.getMessage();
            } else if (ex instanceof AutenticacaoException) {
                mensagem = ex.getMessage();
            } else if (ex instanceof LockedException) {
                mensagem = "Conta bloqueada temporariamente.";
            } else if (ex instanceof DisabledException) {
                mensagem = "Conta desativada.";
            } else if (ex instanceof UsernameNotFoundException) {
                mensagem = "Usuário não encontrado.";
            }
        }

        var errorResponse = new ErrorResponse(
                status,
                mensagem,
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

    // Tratamento para Conflitos de Concorrência
    @ExceptionHandler(ConcurrentModificationException.class)
    public ResponseEntity<String> handleConcurrentModification(ConcurrentModificationException ex) {
        log.error("Erro de concorrência: ", ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Outro usuário já atualizou este recurso. Por favor, tente novamente.");
    }

    // Tratamento para Violação de Integridade de Dados

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<DadosErroValidacao> tratarErroIntegridade(DataIntegrityViolationException ex) {
        log.error("Erro de integridade de dados: ", ex);

        var mensagem = "Erro interno do servidor";

        if (ex.getMessage().contains("unique_titulo_mensagem")) {
            mensagem = "Já existe um tópico similar cadastrado";
        }

        return ResponseEntity.badRequest().body(new DadosErroValidacao(
                "topico",
                mensagem
        ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> tratarErro400(HttpMessageNotReadableException ex) {
        // Log para debug
        log.debug("Causa da exceção: ", ex.getCause());

        if (ex.getMessage().contains("hub.forum.api.domain.escola.AreaFormacao")) {
            String mensagem = String.format(
                    "Área de formação inválida. Valores permitidos: %s",
                    Arrays.stream(AreaFormacao.values())
                            .map(AreaFormacao::getDescricao)
                            .collect(Collectors.joining(", "))
            );
            return ResponseEntity.badRequest().body(mensagem);
        }

        // Se não for relacionado à AreaFormacao, mantém a mensagem padrão
        return ResponseEntity.badRequest().body("Formato da requisição inválido");
    }

    // Tratamento para Conflitos de Otimização de Bloqueio
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimisticLockingFailure(ObjectOptimisticLockingFailureException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito de atualização. Por favor, tente novamente.");
    }

    // Tratamento genérico para outras exceções não mapeadas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> tratarErroGenerico(Exception ex) {
        log.error("Erro não esperado: ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocorreu um erro interno no servidor.");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> tratarErro404(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(EstudanteSemMatriculaException.class)
    public ResponseEntity<String> tratarErroEstudanteSemMatricula(EstudanteSemMatriculaException ex) {
        log.warn("Tentativa de login de estudante sem matrícula ativa");
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> tratarErroCredenciaisInvalidas(BadCredentialsException ex) {
        log.warn("Tentativa de login com credenciais inválidas");
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Credenciais inválidas");
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<String> tratarErroAutenticacaoInterno(InternalAuthenticationServiceException ex) {
        // A mensagem original da EstudanteSemMatriculaException está na causa
        if (ex.getCause() instanceof EstudanteSemMatriculaException) {
            log.warn("Tentativa de login de estudante sem matrícula ativa no sistema");
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ex.getCause().getMessage());
        }

        // Para outros erros internos de autenticação
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Falha na autenticação");
    }

    @ExceptionHandler({BloqueioTemporarioException.class, BloqueioPermanenteException.class})
    public ResponseEntity<String> tratarErroBloqueioTemporario(RuntimeException ex) {
        log.warn("Tentativa de login bloqueada: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(ex.getMessage());
    }
}
