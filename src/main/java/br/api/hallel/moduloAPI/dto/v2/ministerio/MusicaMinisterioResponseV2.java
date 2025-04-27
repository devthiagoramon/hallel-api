package br.api.hallel.moduloAPI.dto.v2.ministerio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MusicaMinisterioResponseV2 {
    private String id;
    private String ministerioId;
    private String titulo;
    private int duracao;
    private String tom;
    private String escala;
    private String compasso;
    private String chaveHarmonica;
    private String letra;
}
