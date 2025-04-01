package me.rogerioferreira.designpatterns.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import me.rogerioferreira.designpatterns.dtos.ErrorMessage;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorMessage> handleRuntimeException(RuntimeException e) {
    return ResponseEntity.internalServerError().body(new ErrorMessage(e.getMessage()));
  }
}
