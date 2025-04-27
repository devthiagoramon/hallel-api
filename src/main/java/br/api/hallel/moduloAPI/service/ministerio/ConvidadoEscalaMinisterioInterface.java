package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.*;

import java.util.List;

public interface ConvidadoEscalaMinisterioInterface {
    public ConvidadoEscalaMinisterioWithConviteResponse createConvidadoAndSendMessage(
            ConvidadoEscalaMinisterioDTO dto);

    public Boolean deleteConvidadoAndSendMessage(String idConvidadoEscalaMinisterio);

    public Boolean deleteConviteOfConvidado(String idConvidadoEscalaMinisterio);
    public List<ConvidadoEscalaMinisterioUserResponse> listConvidadosUserInfos(String idEscalaMinisterio);
    public List<ConvidadoEscalaMinisterioWithConviteResponse> listConvidadosWithConvites(String idEscalaMinsterio);
    public ConvidadoEscalaMinisterioWithInfos listConvidadoInfosById(String idConvidadoEscalaMinisterio);
    public ConvidadoEscalaMinisterioWithConviteResponse editConvidado(
            String idConvidado,
            EditConvidadoEscalaMinisterioDTO dto);


}
