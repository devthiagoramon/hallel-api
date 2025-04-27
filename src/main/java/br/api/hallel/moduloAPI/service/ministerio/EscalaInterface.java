package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.*;
import br.api.hallel.moduloAPI.dto.v2.ministerio.EscalaMinisterioResponseWithInfosV2;
import br.api.hallel.moduloAPI.model.Eventos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface EscalaInterface {
    EscalaMinisterioResponse createEscalaMinisterio(
            Eventos evento, String ministerioId);

    EscalaMinisterioResponse editarDataEscalaMinisterio(
            String escalaMinisterioId, Date date);

    List<String> listMembroMinisterioCanInviteToEscala(String idEscala, int page, int size );

    EscalaMinisterioResponse alterarEscalaConvidarMembroMinisterio(String idEscala, List<String> idsMembrosMinisterio);

    EscalaMinisterioResponse alterarEscalaDesconvidarMembroMinisterio(String idEscala, List<String> idsMembrosMinisterio);

    EscalaMinisterioResponse alterarEscalaConfirmandoMembroMinisterio(
            String idEscala, List<String> idsMembrosMinisterio);

    EscalaMinisterioResponse alterarEscalaNaoConfirmandoMembroMinisterio(
            String idEscala,
            List<NaoConfirmadoEscalaDTOAdm> naoConfirmadoEscalaDtoAdm);

    List<EscalaMinisterioWithEventoInfoResponse> listEscalaMinisterio();

    List<EscalaMinisterioWithEventoInfoResponse> listEscalaMinisterioMembroIdCanParticipate(
            String membroId, LocalDateTime start, LocalDateTime end);

    List<SimpleEscalaMinisterioResponse> listEscalaMinisterioIdsByMembroIdThatCanParticipate(String membroId, LocalDateTime start, LocalDateTime end);

    List<EscalaMinisterioWithEventoInfoResponse> listEscalaMinisterioConfirmedMembro(
            String membroId, LocalDateTime start, LocalDateTime end);

    List<SimpleEscalaMinisterioResponse> listEscalaMinisterioIdsByMembroIdThatConfirmed(String membroId, LocalDateTime start, LocalDateTime end);

    List<EscalaMinisterioWithEventoInfoResponse> listEscalaMinisterioRangeDate(
            LocalDateTime start, LocalDateTime end);

    List<SimpleEscalaMinisterioResponse> listEscalaMinisterioIdsByMinisterioIdAndRangeDate(String idMinisterio, LocalDateTime start, LocalDateTime end);

    List<EscalaMinisterioWithEventoInfoResponse> listEscalaMinisterioRangeDateByMinisterioId(
            String idMinisterio, LocalDateTime start,
            LocalDateTime end);

    EscalaMinisterioResponseWithInfos listEscalaMinisterioByIdWithInfos(
            String idEscalaMinisterio);


    EscalaMinisterioResponseWithInfosV2 listEscalaMinisterioByIdWithInfosV2(String idEscalaMinisterio);

    List<NaoConfirmadoEscalaMinisterioWithInfos> listMotivosAusenciaMembroEventoByIdEscalasMinisterio(
            String idEscala);

    void deleteEscalasWithDeletingEvento(String idEvento);

    EscalaMinisterioResponseWithInfosV2 adicionarRemoverRepertorioInEscala(
            String idEscalaMinisterio,
            EscalaRepertorioDTO escalaRepertorioDTO);

}
