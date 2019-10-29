package io.omlett.coffeehaus.player.service.web.config;

import java.util.Date;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    return this.handleExceptionInternal(ex, logAndTimestampException(ex), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    return this.handleExceptionInternal(ex, logAndTimestampException(ex), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    return this.handleExceptionInternal(ex, logAndTimestampException(ex), headers, status, request);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handleBadRequestException(IllegalArgumentException ex) {
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(httpStatus).body(logAndTimestampException(ex));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleUnknownException(Exception ex) {
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    return ResponseEntity.status(httpStatus).body(logAndTimestampException(ex));
  }

  private ExceptionResponseBody logAndTimestampException(Exception ex) {
    log.debug(ex.getMessage(), ex);
    String message =
        StringUtils.isEmpty(ex.getMessage()) ? "No message available" : ex.getMessage();
    return ExceptionResponseBody.of(message, new Date());
  }

  @Value(staticConstructor = "of")
  private static class ExceptionResponseBody {

    private final String message;
    private final Date timestamp;
  }
}
