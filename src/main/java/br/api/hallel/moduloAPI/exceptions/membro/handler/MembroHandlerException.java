package br.api.hallel.moduloAPI.exceptions.membro.handler;

import br.api.hallel.moduloAPI.exceptions.ExceptionResponse;
import br.api.hallel.moduloAPI.exceptions.membro.MembroEditImageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class MembroHandlerException {

    @ExceptionHandler(MembroEditImageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionResponse> handleEditMembroImageException(
            MembroEditImageException ex, WebRequest request) {
        ExceptionResponse exResponse = new ExceptionResponse(ex.getMessage(), new Date(), request.getDescription(false));
        return new ResponseEntity<>(exResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
