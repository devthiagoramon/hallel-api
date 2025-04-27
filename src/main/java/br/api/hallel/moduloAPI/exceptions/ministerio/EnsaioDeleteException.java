package br.api.hallel.moduloAPI.exceptions.ministerio;

import java.io.Serial;
import java.io.Serializable;

public class EnsaioDeleteException extends RuntimeException implements
        Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public EnsaioDeleteException(String message) {
        super(message);
    }

}
