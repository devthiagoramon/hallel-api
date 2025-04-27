package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.*;

import java.time.LocalDateTime;
import java.util.List;

public interface EnsaioMinisterioInterface {

    EnsaioMinisterioResponse createEnsaioMinisterio(
            EnsaioMinisterioDTO dto);

    EnsaioMinisterioResponse editEnsaioMinisterio(String idEnsaio,
                                                  EnsaioMinisterioDTO dto);

    List<EnsaioMinisterioResponse> listEnsaioMinisterioByMinisterioId(
            String idMinisterio);

    EnsaioMinisterioResponse listEnsaioMinisterioById(
            String idEnsaioMinisterio);

    List<SimpleEscalaMinisterioResponse> listEscalasThatCanAssociateIntoEnsaioMinisterio(String idMinisterio,
            LocalDateTime from);

    EnsaioMinisterioResponse associateEscalaIntoEnsaioMinisterio(
            String idEscala, String idEnsaio);

    StatusMembroMinisterioInEscala getStatusMembroInEnsaioMinisterio(String idEnsaio, String idMembro);

    void deleteEnsaioMinisterio(String idEnsaio);
}
