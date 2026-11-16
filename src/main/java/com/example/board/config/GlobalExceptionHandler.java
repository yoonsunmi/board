package com.example.board.config;

import com.example.board.enums.ErrorCode;
import com.example.board.exception.BoardException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BoardException.class)
    public ModelAndView handleBoardException(BoardException e) {
        ErrorCode errorCode = e.getErrorCode();

        ModelAndView mav = new ModelAndView("error");

        HttpStatus status = switch (errorCode) {
            case PAGE_NOT_FOUND, BOARD_NOT_FOUND -> HttpStatus.NOT_FOUND;
        };

        mav.setStatus(status);
        mav.addObject("status", status.value());
        mav.addObject("message", errorCode.getMessage());

        return mav;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(error -> errors.putIfAbsent(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

}
