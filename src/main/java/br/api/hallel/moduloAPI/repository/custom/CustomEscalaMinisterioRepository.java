package br.api.hallel.moduloAPI.repository.custom;

import br.api.hallel.moduloAPI.dto.v1.ministerio.EscalaMinisterioResponseWithInfos;
import br.api.hallel.moduloAPI.dto.v1.ministerio.EscalaMinisterioWithEventoInfoResponse;
import br.api.hallel.moduloAPI.dto.v2.ministerio.EscalaMinisterioResponseWithInfosV2;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface CustomEscalaMinisterioRepository {
    List<EscalaMinisterioWithEventoInfoResponse> findAllWithEventosInfos();

    List<EscalaMinisterioWithEventoInfoResponse> findAllWithEventosInfosRangeDate(
            LocalDateTime start, LocalDateTime end);

    EscalaMinisterioResponseWithInfos findWithAllInfosById(
            String idEscalaMinisterio);
    EscalaMinisterioResponseWithInfosV2 findWithAllInfosByIdV2(
            String idEscalaMinisterio);

    List<EscalaMinisterioWithEventoInfoResponse> findAllWithEventosInfosCanParticipateByMembroId(
            String membroId, LocalDateTime start, LocalDateTime end);

    List<EscalaMinisterioWithEventoInfoResponse> findAllWithEventosInfosConfirmedByMembroId(
            String membroId,
            LocalDateTime start,
            LocalDateTime end);

    List<EscalaMinisterioWithEventoInfoResponse> findAllWithEventosInfosRangeDateByMinisterioId(
            String idMinisterio,
            LocalDateTime start,
            LocalDateTime end);
}
