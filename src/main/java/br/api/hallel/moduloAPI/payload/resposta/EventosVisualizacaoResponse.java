package br.api.hallel.moduloAPI.payload.resposta;

import br.api.hallel.moduloAPI.financeiroNovo.model.PagamentoEntradaEvento;
import br.api.hallel.moduloAPI.model.Eventos;
import br.api.hallel.moduloAPI.model.LocalEvento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventosVisualizacaoResponse {
    private String id;
    private String titulo;
    private String descricao;
    private Date date;
    private String banner;
    private String fileImageUrl;
    private LocalEvento localEvento;
    private Boolean destaque;
    private String horario;
    private List<String> palestrantes;
    private List<PagamentoEntradaEvento> pagamentoEntradaEventoList;

    public EventosVisualizacaoResponse toListEventosResponse(Eventos eventos) {
        EventosVisualizacaoResponse response = new EventosVisualizacaoResponse();
        response.setId(eventos.getId());
        response.setTitulo(eventos.getTitulo());
        response.setDescricao(eventos.getDescricao());
        response.setDate(eventos.getDate());
        response.setFileImageUrl(eventos.getFileImageUrl());
        response.setDestaque(eventos.getDestaque());
        response.setLocalEvento(eventos.getLocalEvento());
        response.setHorario(eventos.getHorario());
        response.setPalestrantes(eventos.getPalestrantes());
        response.setBanner(eventos.getBanner());
        return response;
    }
}
