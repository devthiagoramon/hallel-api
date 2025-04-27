package br.api.hallel.moduloAPI.dto.v1.ministerio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class MusicaMinisterioDTOEdit {
    private String titulo;
    private int duracao;
    private String tom;
    private String escala;
    private String compasso;
    private String chaveHarmonica;
}
