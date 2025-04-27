package br.api.hallel.moduloAPI.exceptions.ministerio;

public class MessageTelegramNotSendToConvidadoException
        extends RuntimeException {
    public MessageTelegramNotSendToConvidadoException(String message) {
        super(message);
    }
}
