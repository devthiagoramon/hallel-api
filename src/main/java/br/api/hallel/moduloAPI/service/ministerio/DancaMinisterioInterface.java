package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.DancaMinisterioDTO;
import br.api.hallel.moduloAPI.dto.v1.ministerio.DancaMinisterioDTOEdit;
import br.api.hallel.moduloAPI.dto.v1.ministerio.DancaMinisterioResponse;

import java.util.List;

public interface DancaMinisterioInterface {
    DancaMinisterioResponse createDancaMinisterio(
            DancaMinisterioDTO dancaMinisterioDTO);

    List<DancaMinisterioResponse> getAllDancaMinisterio(String ministerioId);

    DancaMinisterioResponse getDancaMinisterioById(
            String idDancaMinisterio);

    DancaMinisterioResponse updateDancaMinisterio(
            String idDancaMinisterio,
            DancaMinisterioDTOEdit dancaMinisterioDTOEdit);

    void deleteDancaMinisterio(String idDancaMinisterio);
}
