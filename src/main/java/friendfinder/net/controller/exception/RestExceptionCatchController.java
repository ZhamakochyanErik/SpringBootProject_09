package friendfinder.net.controller.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionCatchController {

    private static final Logger LOGGER = LogManager.getLogger();

    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionCatch(Exception e){
        LOGGER.error(e.getMessage(),e);
        return ResponseEntity
                .status(500)
                .build();
    }
}