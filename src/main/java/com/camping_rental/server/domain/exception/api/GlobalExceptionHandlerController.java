package com.camping_rental.server.domain.exception.api;

import com.camping_rental.server.domain.exception.dto.AccessDeniedPermissionException;
import com.camping_rental.server.domain.exception.dto.InvalidUserException;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.message.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandlerController {
    /*
    400
     */
    @ExceptionHandler(value = {NotMatchedFormatException.class})
    public ResponseEntity<?> notMatchedFormatException(NotMatchedFormatException ex) {
        Message message = new Message();
        log.warn("ERROR STACKTRACE => {}", ex.getStackTrace());

        message.setStatus(HttpStatus.BAD_REQUEST);
        message.setMessage("not_matched_format");
        message.setMemo(ex.getMessage());
        return new ResponseEntity<>(message, message.getStatus());
    }

    /*
    401
     */
    @ExceptionHandler(value = {InvalidUserException.class})
    public ResponseEntity<?> invalidUserException(InvalidUserException ex) {
        Message message = new Message();
        log.warn("ERROR STACKTRACE => {}", ex.getStackTrace());

        message.setStatus(HttpStatus.UNAUTHORIZED);
        message.setMessage("invalid_user");
        message.setMemo(ex.getMessage());
        return new ResponseEntity<>(message, message.getStatus());
    }

    /*
    403
     */
    @ExceptionHandler(value = {AccessDeniedPermissionException.class})
    public ResponseEntity<?> accessDeniedPermissionException(AccessDeniedPermissionException ex) {
        Message message = new Message();
        log.warn("ERROR STACKTRACE => {}", ex.getStackTrace());

        message.setStatus(HttpStatus.FORBIDDEN);
        message.setMessage("access_denied");
        message.setMemo(ex.getMessage());
        return new ResponseEntity<>(message, message.getStatus());
    }
}
