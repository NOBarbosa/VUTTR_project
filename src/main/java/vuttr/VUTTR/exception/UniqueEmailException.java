package vuttr.VUTTR.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UniqueEmailException extends  RuntimeException{
public UniqueEmailException(){
    super("Email já cadastrado");
}
public UniqueEmailException (String message){super((message));}
}
