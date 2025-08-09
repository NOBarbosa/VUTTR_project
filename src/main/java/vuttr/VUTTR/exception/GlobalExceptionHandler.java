package vuttr.VUTTR.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Erro> handleGenericException(Exception e){
        String message = e.getMessage();
        if(e instanceof MethodArgumentTypeMismatchException){
            message = "Tipo inválido";
        }
        Erro erro = new Erro(message, 400);
        e.printStackTrace();
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler
    public ResponseEntity<Erro> handleNotFoundException(NotFoundException e){
        Erro erro =  new Erro(e.getMessage(), 404);
        return ResponseEntity.badRequest().body(erro);

    }

    @ExceptionHandler
    public ResponseEntity<Erro> handleUniqueEmailException(UniqueEmailException e){
        Erro erro =  new Erro(e.getMessage(), 404);
        return ResponseEntity.badRequest().body(erro);

    }

    @ExceptionHandler
    public ResponseEntity<Erro> handleNonEmptyException(NonEmptyException e){
        Erro erro =  new Erro(e.getMessage(), 400);
        return ResponseEntity.badRequest().body(erro);


    }
}
