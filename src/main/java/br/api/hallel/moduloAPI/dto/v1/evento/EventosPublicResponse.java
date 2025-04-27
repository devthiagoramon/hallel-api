package br.api.hallel.moduloAPI.dto.v1.evento;

import br.api.hallel.moduloAPI.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventosPublicResponse {

    private String id;
    private String titulo;
    private String descricao;
    private Date date;
    private LocalEvento localEvento;
    private String fileImageUrl;
    private String banner;
    private Boolean destaque;
    private List<String> palestrantes;
    private List<VoluntarioEvento> voluntarios;
    private Double valorDoEvento;
    private List<String> ministeriosAssociados;
}
