package hub.forum.api.infra.exceptions;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class TratadorDeErros {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErroValidacaoDto>>tratarErro400(MethodArgumentNotValidException ex){

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



    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> tratarErroGenerico(Exception ex) {
        log.error("Erro não esperado: ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocorreu um erro interno no servidor.");
    }

    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<String> erroToken(Exception ex){
        return ResponseEntity.badRequest().body(ex.getCause().getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> tratarErro404(){
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<String>tratarErro400(HttpMessageNotReadableException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<String> tratarErroConsulta(JpaSystemException ex) {
        log.error("Erro ao executar consulta JPA: ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao processar a requisição. Por favor, tente novamente.");
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<String> tratarErroValidacao(ValidacaoException ex) {
        log.error("Erro de validação: ", ex);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> tratarErroNotFound(ResponseStatusException ex){
        return ResponseEntity.status(ex.getStatusCode()).body("Erro: ex.getReason()");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> tratarErroIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> tratarErroBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> tratarErroAuthentication() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação");
    }

    @ExceptionHandler(ConcurrentModificationException.class)
    public ResponseEntity<String> handleConcurrentModification(ConcurrentModificationException ex) {
        log.error("Erro de concorrência: ", ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Outro usuário já atualizou este recurso. Por favor, tente novamente.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> ErroDeIntegridadeDeDados(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimisticLockingFailure(ObjectOptimisticLockingFailureException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito de atualização. Por favor, tente novamente.");
    }


    @ExceptionHandler(JobExecutionException.class)
    public ResponseEntity<String> handleJobExecutionException(JobExecutionException ex) {
        log.error("Erro na execução do job", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro no processamento em lote: " + ex.getMessage());
    }

    @ExceptionHandler(BatchProcessingException.class)
    public ResponseEntity<String> handleBatchProcessingException(BatchProcessingException ex) {
        log.error("Erro no processamento batch", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro no processamento: " + ex.getMessage());
    }
}

