package br.api.hallel.moduloAPI.dto.v2.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.EventosShortResponse;
import br.api.hallel.moduloAPI.dto.v1.ministerio.MinisterioResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EscalaMinisterioResponseWithInfosV2 {

    private String id;
    private String ministerioId;
    private String eventoId;
    private Date date;
    private List<String> membrosMinisterioConvidadosIds;
    private List<String> membrosMinisterioConfimadoIds;
    private List<String> membrosMinisterioNaoConfirmadoIds;
    private List<String> repertorioIds;
    private String ensaioMinisterioId;
    private boolean isEnsaio;

}
