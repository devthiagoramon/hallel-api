package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.*;

import java.util.List;

public interface RepertorioInterface {
    RepertorioResponse createRepertorio(RepertorioDTO repertorioDTO);
    List<RepertorioResponse> listRepertoriosByMinisterioId(String ministerioId);
    RepertorioResponse listRepertorioById(String idRepertorio);
    RepertorioResponse editRepertorio(String idRepertorio, RepertorioDTOEdit repertorioDTO);
    void deleteRepertorio(String idRepertorio);
    RepertorioResponseWithInfos adicionarRemoverMusicasRepertorio(String idRepertorio, RepertorioMusicDTO repertorioMusicDTO);
    RepertorioResponseWithInfos adicionarRemoverDancaRepertorio(String idRepertorio, RepertorioDancaDTO repertorioDancaDTO);
    RepertorioResponseWithFullInfos listRepertorioWithDancesAndMusic(String idRepertorio);

}
