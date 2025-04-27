package br.api.hallel.moduloAPI.exceptions.main;

public class SolicitarCadastroException extends RuntimeException{

    public SolicitarCadastroException(String message){
        super(message);
    }

    public SolicitarCadastroException (Throwable t){
        super(t);
    }

}
