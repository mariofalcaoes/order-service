package com.order.exception;

import com.order.domain.model.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class RestControllerAdviceExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessErrors(BusinessException exception) {
        ApiError apiErrorMessage = new ApiError(exception.getMessage());
        return new ResponseEntity<>(apiErrorMessage, exception.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericErrors(Exception exception) {
        ApiError apiErrorMessage = new ApiError(exception.getMessage());
        return new ResponseEntity<>(apiErrorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        String errorMessage = "Validation error(s): " +
                fieldErrors.stream()
                        .map(error -> String.format("%s %s", error.getField(), error.getDefaultMessage()))
                        .collect(Collectors.joining(", "));

        log.error(errorMessage, ex);

        return new ApiError(errorMessage);
    }

}
