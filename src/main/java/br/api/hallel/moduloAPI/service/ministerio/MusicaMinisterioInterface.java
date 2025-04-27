package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.MusicaMinisterioDTO;
import br.api.hallel.moduloAPI.dto.v1.ministerio.MusicaMinisterioDTOEdit;
import br.api.hallel.moduloAPI.dto.v1.ministerio.MusicaMinisterioResponse;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MusicaMinisterioDTOV2;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MusicaMinisterioResponseV2;

import java.util.List;

public interface MusicaMinisterioInterface {
    MusicaMinisterioResponse createMusicaMinisterio(
            MusicaMinisterioDTO musicaMinisterioDTO);

    List<MusicaMinisterioResponseV2> getAllMusicaMinisterios(
            String ministerioId);

    MusicaMinisterioResponse getMusicaMinisterioById(
            String idMusicaMinisterio);

    MusicaMinisterioResponse updateMusicaMinisterio(
            String idMusicaMinisterio,
            MusicaMinisterioDTOEdit musicaMinisterioDTO);

    void deleteMusicaMinisterio(String idMusicaMinisterio);

    MusicaMinisterioResponseV2 createMusicaMinisterioV2(
            MusicaMinisterioDTOV2 dtoV2);

    MusicaMinisterioResponseV2 updateMusicaMinisterioV2(
            String idMusicaMinisterio, MusicaMinisterioDTOV2 dtoV2);

    MusicaMinisterioResponseV2 getMusicaMinisterioWithLetra(String idMusicaMinisterio);
    String getLetraOfMusicaMinisterio(String idMusicaMinisterio);
}
