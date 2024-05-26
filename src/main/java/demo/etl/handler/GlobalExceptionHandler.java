package demo.etl.handler;

import demo.etl.dto.resp.GeneralResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralResponse> handleAllExceptions(Exception e, WebRequest request){
        GeneralResponse response = new GeneralResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "Something went wrong");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<GeneralResponse> handleDataIntegrityViolationExceptions(Exception e, WebRequest request){
        GeneralResponse response = new GeneralResponse(HttpStatus.UNPROCESSABLE_ENTITY.toString(),
                "Duplicate item found");
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e, WebRequest request){
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage()
                ));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
