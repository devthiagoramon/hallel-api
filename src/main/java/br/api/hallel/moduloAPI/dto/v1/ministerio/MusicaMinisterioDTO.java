package br.api.hallel.moduloAPI.dto.v1.ministerio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MusicaMinisterioDTO {
    private String ministerioId;
    private String titulo;
    private int duracao;
    private String tom;
    private String escala;
    private String compasso;
    private String chaveHarmonica;
}
