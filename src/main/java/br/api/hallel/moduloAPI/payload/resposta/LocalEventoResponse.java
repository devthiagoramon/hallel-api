package br.api.hallel.moduloAPI.payload.resposta;

import br.api.hallel.moduloAPI.model.LocalEvento;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalEventoResponse {
    private String localizacao;
    private Double longitude;
    private Double latitude;

    public LocalEventoResponse toLocalEventoReponse(LocalEvento localEvento) {
        LocalEventoResponse localEventoResponse = new LocalEventoResponse();
        localEventoResponse.setLocalizacao(localEvento.getLocalizacao());
        localEventoResponse.setLongitude(localEvento.getLongitude());
        localEventoResponse.setLatitude(localEvento.getLatitude());
        return localEventoResponse;
    }
}
