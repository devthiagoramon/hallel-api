package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.NaoConfirmarEnsaioDTO;
import br.api.hallel.moduloAPI.dto.v1.ministerio.NaoConfirmarEscalaDTO;
import br.api.hallel.moduloAPI.model.NaoConfirmadoEscalaMinisterio;

import java.util.List;

public interface NaoConfirmadoEscalaMinisterioInterface {
    NaoConfirmadoEscalaMinisterio createNaoConfirmadoEscalaMinisterio(
            NaoConfirmarEscalaDTO naoConfirmarEscalaDTO);
    NaoConfirmadoEscalaMinisterio createNaoConfirmadoEscalaMinisterioEnsaio(
            String idEscala, NaoConfirmarEnsaioDTO naoConfirmarEnsaioDTO);

    NaoConfirmadoEscalaMinisterio editNaoConfirmadoEscalaMinisterio(
            String idNaoConfirmadoEscala,
            NaoConfirmarEscalaDTO naoConfirmarEscalaDTO);

    List<NaoConfirmadoEscalaMinisterio> listNaoConfirmadoEscalaMinisterioByIdMembroMinisterio(
            String idMemmbroMinisterio);

    NaoConfirmadoEscalaMinisterio listNaoConfirmadoEscalaMinisterioById(
            String idNaoConfirmadoEscalaMinisterio);

    void deleteNaoConfirmadoEscalaMinisterio(
            String idNaoConfirmadoEscalaMinisterio);

    Boolean verifyIfMembroMinisterioIsAlreadyNotConfirmed(String idEscala, String idMembroMinisterio);
}
