package com.gs.core.web.mvc.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;

import static com.gs.core.web.mvc.config.ExceptionConstant.MESSAGE_500;

/**
 * author: linjuntan
 * date: 2018/2/24
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ErrorEntity> handleResourceNotFoundException(NoHandlerFoundException nhre) {
        log.error(nhre.getMessage());
        return new ResponseEntity<>(new ErrorEntity(String.valueOf(HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND.name()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {IOException.class, RuntimeException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorEntity> illegalParamsExceptionHandler(Exception ex) {
       log.error(ex.getMessage(), ex);
       return new ResponseEntity<>(new ErrorEntity(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
               MESSAGE_500), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = BaseWebException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorEntity> baseWebExceptionHandler(BaseWebException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(ex.toErrorEntity(), HttpStatus.BAD_REQUEST);
    }
}
