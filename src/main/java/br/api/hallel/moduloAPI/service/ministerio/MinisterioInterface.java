package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.*;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MinisterioDTOV2;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MinisterioResponseV2;
import br.api.hallel.moduloAPI.model.*;
import br.api.hallel.moduloAPI.payload.resposta.MembroResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

interface MinisterioInterface {

    // Ministerio
    MinisterioResponse createMinisterio(
            MinisterioDTO ministerioDTO, MultipartFile fileImage);

    MinisterioResponseV2 createMinisterioV2(MinisterioDTOV2 ministerioDTOV2, MultipartFile fileImage);

    MinisterioResponse editMinisterio(String idMinisterio,
                                      MinisterioDTO ministerioDTO);

    MinisterioResponseV2 editMinisterioV2(String idMinisterio, MinisterioDTOV2 ministerioDTOV2, MultipartFile fileImage);
    MinisterioResponseV2 editImageMinisterio(String idMinisterio, MultipartFile fileImage);

    void deleteMinisterio(String idMinisterio);

    List<MinisterioResponse> listMinisterios();

    MinisterioWithCoordsResponse listMinisterioWithCoordById(String idMinisterio);

    Slice<MinisterioWithCoordsResponse> listMinisteriosWithCoords(
            Pageable page);

    List<MinisterioWithCoordsResponse> listMInisteriosWithCoordsByName(
            String name);

    List<MinisterioPublicResponse> listMinisterioPublic(int page, int size);

    MinisterioResponse listMinisterioById(String idMinisterio);

    MinisterioResponse alterarCoordenadoresInMinisterio(
            String idMinisterio,
            EditCoordMinisterioDTO editCoordMinisterioDTO);

    // Coordenador
    Boolean validateCoordenadorInMinisterio(
            String idMinisterio, String idUser);

    List<EventosShortResponse> listEventosThatMinisterioIsIn(String ministerioId);

    MinisterioResponseV2 listMinisterioV2ById(String idMinisterio);
}
