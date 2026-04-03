package com.kiruthika.chatapp.user_service.exception;

import com.kiruthika.chatapp.user_service.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice

public class GlobalExceptionHndler {
    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenAction(ForbiddenActionException e){
        ErrorResponse errorResponse=ErrorResponse.builder().error(HttpStatus.FORBIDDEN.getReasonPhrase()).message(e.getMessage()).status(HttpStatus.FORBIDDEN.value()).timestamp(LocalDateTime.now()).build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e){
        ErrorResponse errorResponse=ErrorResponse.builder().error(HttpStatus.BAD_REQUEST.getReasonPhrase()).message(e.getMessage()).status(HttpStatus.BAD_REQUEST.value()).timestamp(LocalDateTime.now()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
