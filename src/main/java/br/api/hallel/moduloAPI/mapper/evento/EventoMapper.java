package br.api.hallel.moduloAPI.mapper.evento;

import br.api.hallel.moduloAPI.dto.v1.evento.EventoDTO;
import br.api.hallel.moduloAPI.dto.v1.evento.EventoResponse;
import br.api.hallel.moduloAPI.dto.v1.evento.EventosPublicResponse;
import br.api.hallel.moduloAPI.mapper.MapperUtil;
import br.api.hallel.moduloAPI.model.Eventos;
import org.modelmapper.ModelMapper;

public class EventoMapper {
    private static final ModelMapper MAPPER = MapperUtil.modelMapper;

    public static Eventos dtoToEvento(EventoDTO eventoDTO){
        return MAPPER.map(eventoDTO, Eventos.class);
    }

    public static EventoResponse modelToResponse(Eventos evento){
        return MAPPER.map(evento, EventoResponse.class);
    }

    public static EventosPublicResponse modelToPublicResponse(
            Eventos eventos) {
        return MAPPER.map(eventos, EventosPublicResponse.class);
    }
}
