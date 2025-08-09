package vuttr.VUTTR.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value =  HttpStatus.BAD_REQUEST)
public class NonEmptyException extends RuntimeException {
    public NonEmptyException(){
        super("Campo não pode ser vazio");
    }
public NonEmptyException(String message){super(message);}
}
