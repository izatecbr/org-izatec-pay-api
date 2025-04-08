package com.izatec.pay.infra.business;

import com.izatec.pay.infra.response.Response;
import com.izatec.pay.infra.response.ResponseFactory;
import org.springframework.cglib.proxy.UndeclaredThrowableException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> handleGeneral(Exception e, WebRequest request) {
        String message = "";
        if (e.getClass().isAssignableFrom(UndeclaredThrowableException.class)) {
            UndeclaredThrowableException exception = (UndeclaredThrowableException) e;
            Class<? extends Throwable> exceptionClass = exception.getUndeclaredThrowable().getClass();
            return handleBusinessException((BusinessException) exception.getUndeclaredThrowable(), request);
        } else {
            BusinessMessage be = BusinessMessage.E501;
            Response error = ResponseFactory.error(Integer.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), be.getMessage().concat(message), be.getSuggestion());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return handleExceptionInternal(e, error, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
        }

    }
    @ExceptionHandler({BusinessException.class})
    private ResponseEntity<Object> handleBusinessException(BusinessException be, WebRequest request) {
        Response error = ResponseFactory.error(be.getErrorCode(), be.getMessage(), be.getSuggestion());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity response = handleExceptionInternal(be, error, headers, HttpStatus.resolve(be.getHttpStatus()), request);

        return response;
    }
}
