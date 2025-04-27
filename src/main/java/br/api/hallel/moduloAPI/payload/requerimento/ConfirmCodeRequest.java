package br.api.hallel.moduloAPI.payload.requerimento;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmCodeRequest {
    private String email;
    private String codigo;
}
