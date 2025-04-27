package br.api.hallel.moduloAPI.dto.v2.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.EventosShortResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EscalaMinisterioWithEventoInfoResponseV2 {
    private String id;
    private String eventoId;
    private String ministerioId;
    private Date date;
    private List<String> repertorioIds;
}
