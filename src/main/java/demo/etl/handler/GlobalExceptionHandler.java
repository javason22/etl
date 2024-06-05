package demo.etl.handler;

import demo.etl.dto.resp.GeneralResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle all other exceptions
     *
     * @param e exception
     * @return response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralResponse> handleAllExceptions(Exception e){
        log.error("An unexpected error occurred", e);
        GeneralResponse response = new GeneralResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "Something went wrong");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle DataIntegrityViolationException that is thrown
     * when database constraint is violated
     *
     * @param e exception
     * @return response entity
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<GeneralResponse> handleDataIntegrityViolationExceptions(Exception e){
        log.error("An DataIntegrityViolationException occurred", e);
        GeneralResponse response = new GeneralResponse(HttpStatus.UNPROCESSABLE_ENTITY.toString(),
                "Duplicate item found");
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Handle DataRetrievalFailureException that is thrown
     * when request item is not found
     *
     * @param e exception
     * @return response entity
     */
    @ExceptionHandler(DataRetrievalFailureException.class)
    public ResponseEntity<GeneralResponse> handleDataRetrievalFailureExceptions(Exception e){
        log.error("An DataRetrievalFailureException occurred", e);
        GeneralResponse response = new GeneralResponse(HttpStatus.NOT_FOUND.toString(),
                "Request item not found: " + e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle MethodArgumentTypeMismatchException that is thrown when request parameter type is invalid.
     *
     * @param e exception
     * @return response entity
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<GeneralResponse> handleMethodArgumentTypeMismatchExceptions(MethodArgumentTypeMismatchException e){
        log.error("An MethodArgumentTypeMismatchException occurred", e);
        GeneralResponse response = new GeneralResponse(HttpStatus.BAD_REQUEST.toString(),
                MessageFormat.format("Invalid parameter type: request parameter:{0}, required type:{1}",
                        e.getName(), Objects.requireNonNull(e.getRequiredType()).getName()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Spring validation exception handler
     * Handle MethodArgumentNotValidException that is thrown when @Valid is violated.
     *
     * @param e exception
     * @return response entity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException e){
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage()
                ));
        Map <String, Object> response = Map.of("status", HttpStatus.BAD_REQUEST.toString(), "errors", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle HandlerMethodValidationException that is thrown when ValidUUID is violated.
     *
     * @param e exception
     * @return response entity
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, Object>> handleHandlerMethodValidationException(HandlerMethodValidationException e){
        log.error("Validation Failed", e);
        Map<String, String> errors = new HashMap<>();
        errors.put("id", "Invalid ID - UUID format is required");
        Map <String, Object> response = Map.of("status", HttpStatus.BAD_REQUEST.toString(), "errors", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle HttpMessageNotReadableException that is thrown when JSON request body is invalid.
     *
     * @param e exception
     * @return response entity
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GeneralResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        log.error("An HttpMessageNotReadableException occurred", e);
        GeneralResponse response = new GeneralResponse(HttpStatus.BAD_REQUEST.toString(),
                "Invalid request body");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
