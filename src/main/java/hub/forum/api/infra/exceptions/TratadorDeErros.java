package hub.forum.api.infra.exceptions;

import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.Setter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.List;

@RestControllerAdvice
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

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> tratarErro404(){
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<String>tratarErro400(HttpMessageNotReadableException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> tratarErro500(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + ex.getLocalizedMessage());
    }

//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<String> tratarErroBadCredentials() {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
//    }
//
//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<String> tratarErroAuthentication() {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação");
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<String> tratarErroAcessoNegado() {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado");
}
