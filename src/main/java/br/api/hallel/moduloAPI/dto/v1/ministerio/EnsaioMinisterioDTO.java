package br.api.hallel.moduloAPI.dto.v1.ministerio;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnsaioMinisterioDTO implements Serializable {

    private String idMinisterio;

    @NotNull(message = "Titulo must be inserted")
    @NotBlank(message = "Titulo can't be blank")
    private String titulo;
    @NotNull(message = "Descricao must be inserted")
    @NotBlank(message = "Descricao can't be blank")
    private String descricao;

    @NotNull(message = "Date must be inserted")
    @Future(message = "Date must be in future")
    private LocalDateTime date;

    @Nullable
    private String idEscalaMinisterioAssociated;
}
