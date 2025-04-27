package br.api.hallel.moduloAPI.dto.v1.ministerio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MusicaMinisterioResponse {
    private String id;
    private String ministerioId;
    private String titulo;
    private int duracao;
    private String tom;
    private String escala;
    private String compasso;
    private String chaveHarmonica;
}
