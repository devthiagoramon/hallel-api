package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.*;
import br.api.hallel.moduloAPI.model.MembroMinisterio;
import br.api.hallel.moduloAPI.payload.resposta.MembroResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface MembroMinisterioInterface {
    Slice<MembroResponse> listMembrosToAddIntoThisMinisterio(
            String idMinisterio, int page, int size);

    Slice<MembroMinisterioWithInfosResponse> listMembrosFromMinisterio(
            String idMinisterio, int page, int size);

    MembroMinisterioWithInfosResponse listMembroMinisterioById(
            String idMembroMinisterio);

    MembroMinisterio listMembroMinisterioByMembroIdAndMinisterioId(String idMembro, String idMinisterio);

    MembroMinisterio addMembroMinisterio(
            AddMembroMinisterioDTO addMembroMinisterioDTO);

    void removerMembroMinisterio(String idMembroMinisterio);

    void removerMembroMinisterio(MembroMinisterio membroMinisterio);

    StatusParticipacaoEscalaMinisterio getStatusParticipacaoEscala(
            String idMembroMinisterio, String idEscalaMinisterio);

    Boolean confirmarParticipacaoEscala(String idMembro,
                                        String idEscalaMinisterio);

    Boolean confirmarParticipacaoEnsaio(String idMembro, String idEnsaioMinisterio);

    Boolean recusarParticipacaoEscala(
            NaoConfirmarEscalaDTO naoConfirmarEscalaDTO);

    Boolean recusarParticipacaoEnsaio(NaoConfirmarEnsaioDTO naoConfirmarEnsaioDTO);

    List<MinisterioResponse> listMinisterioThatMembroParticipateByMembroId(String idMembro);

    List<String> listMinisterioV2ThatMembroParticipateByMembroId(
            String idMembro);


    StatusMembroMinisterio listStatusMembroMinisterioByMinisterioIdAndMembroId(String idMinisterio, String membroId);

    List<RepertorioResponseWithFullInfos> listRepertorioAsMembroMinisterioByMinisterioId(String idMinisterio);
    List<MusicaMinisterioResponse> listMusicaMinisterioAsMembroMinisterioByMinisterioId(String idMinisterio);
    List<DancaMinisterioResponse> listDancaMinisterioAsMembroMinisterioByMinisterioId(String idMinisterio);

}
