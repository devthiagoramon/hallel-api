package br.api.hallel.moduloAPI.dto.v1.ministerio;

import br.api.hallel.moduloAPI.model.DancaMinisterio;
import br.api.hallel.moduloAPI.model.MusicaMinisterio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RepertorioResponseWithFullInfos {
    private String id;
    private String ministerioId;
    private String nome;
    private String descricao;
    private List<MusicaMinisterio> musicas;
    private List<DancaMinisterio> dancasMinisterio;
}
