package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.DefineFunctionsDTO;
import br.api.hallel.moduloAPI.dto.v1.ministerio.FuncaoMinisterioDTO;
import br.api.hallel.moduloAPI.dto.v1.ministerio.MembroMinisterioWithInfosResponse;
import br.api.hallel.moduloAPI.model.FuncaoMinisterio;

import java.util.List;

public interface FuncaoMinisterioInterface {
    // Função ministerio
    FuncaoMinisterio createFuncaoMinisterio(
            FuncaoMinisterioDTO funcaoMinisterioDTO);

    List<FuncaoMinisterio> listFuncaoOfMinisterio(
            String idMinisterio);

    FuncaoMinisterio listFuncaoMinisterioById(
            String idFuncaoMinisterio);

    FuncaoMinisterio editFuncaoMinisterio(
            String idFuncaoMinisterio,
            FuncaoMinisterioDTO funcaoMinisterioDTO);

    void deleteFuncaoMinisterio(String idFuncaoMinisterio);

    MembroMinisterioWithInfosResponse defineFunctionsToMembroMinisterio(
            DefineFunctionsDTO defineFunctionsDTO);
}
