package br.api.hallel.moduloAPI.exceptions.handler;

import br.api.hallel.moduloAPI.exceptions.ExceptionResponse;
import br.api.hallel.moduloAPI.exceptions.ministerio.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class MinisterioHandlerException {

    @ExceptionHandler({EnsaioCreateEditException.class})
    public ResponseEntity<ExceptionResponse> handleCreateEditEnsaioException(
            EnsaioCreateEditException ex, WebRequest request) {
        ExceptionResponse exResponse = new ExceptionResponse(ex.getMessage(), new Date(), request.getDescription(false));
        return new ResponseEntity<>(exResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EnsaioDeleteException.class})
    public ResponseEntity<ExceptionResponse> handleDeleteEnsaioException(
            EnsaioDeleteException ex, WebRequest request) {
        ExceptionResponse exResponse = new ExceptionResponse(ex.getMessage(), new Date(), request.getDescription(false));
        return new ResponseEntity<>(exResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AssociateEscalaInEnsaioExcpetion.class})
    public ResponseEntity<ExceptionResponse> handleAssociateEscalaInEnsaioException(
            EnsaioDeleteException ex, WebRequest request) {
        ExceptionResponse exResponse = new ExceptionResponse(ex.getMessage(), new Date(), request.getDescription(false));
        return new ResponseEntity<>(exResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EnsaioListException.class})
    public ResponseEntity<ExceptionResponse> handleListEnsaioException(
            EnsaioListException ex, WebRequest request) {
        ExceptionResponse exResponse = new ExceptionResponse(ex.getMessage(), new Date(), request.getDescription(false));
        return new ResponseEntity<>(exResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({StatusMembroInEnsaioException.class})
    public ResponseEntity<ExceptionResponse> handleStatusMembroInEnsaioException(
            EnsaioListException ex, WebRequest request) {
        ExceptionResponse exResponse = new ExceptionResponse(ex.getMessage(), new Date(), request.getDescription(false));
        return new ResponseEntity<>(exResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            ConfirmDeclineParticipationMembroInEnsaioException.class})
    public ResponseEntity<ExceptionResponse> handleConfirmDeclineParticipationMembroInEnsaioException(
            EnsaioListException ex, WebRequest request) {
        ExceptionResponse exResponse = new ExceptionResponse(ex.getMessage(), new Date(), request.getDescription(false));
        return new ResponseEntity<>(exResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(
            {MessageTelegramNotSendToConvidadoException.class})
    public ResponseEntity<ExceptionResponse> handleMessageTelegramNotSendToConvidadoException(
            EnsaioListException ex, WebRequest request) {
        ExceptionResponse exResponse = new ExceptionResponse(ex.getMessage(), new Date(), request.getDescription(false));
        return new ResponseEntity<>(exResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ConviteEscalaMinisterioNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleConviteEscalaMinisterioNotFoundException(
            EnsaioListException ex, WebRequest request
                                                                                           ){
        ExceptionResponse exResponse = new ExceptionResponse(ex.getMessage(), new Date(), request.getDescription(false));
        return new ResponseEntity<>(exResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ConvidadoEscalaMinisterioNotFound.class})
    public ResponseEntity<ExceptionResponse> handleConvidadoEscalaMinisterioNotFoundException(
            EnsaioListException ex, WebRequest request
                                                                                           ){
        ExceptionResponse exResponse = new ExceptionResponse(ex.getMessage(), new Date(), request.getDescription(false));
        return new ResponseEntity<>(exResponse, HttpStatus.NOT_FOUND);
    }


}
