package br.api.hallel.moduloAPI.dto.v1.ministerio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class DancaMinisterioResponse {
    private String id;
    private String ministerioId;
    private String nome;
    private String descricao;
    private String linkVideo;
}
