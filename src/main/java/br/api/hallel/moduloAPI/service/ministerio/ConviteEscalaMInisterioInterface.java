package br.api.hallel.moduloAPI.service.ministerio;


import br.api.hallel.moduloAPI.dto.v1.ministerio.ConvidadoEscalaMinisterioDTO;
import br.api.hallel.moduloAPI.dto.v1.ministerio.ConviteEscalaMinisterioDTO;
import br.api.hallel.moduloAPI.dto.v1.ministerio.ConviteEscalaResponse;

public interface ConviteEscalaMInisterioInterface {

    public ConviteEscalaResponse createConviteEscala(
            ConviteEscalaMinisterioDTO dto);

    public Boolean deleteConviteEscala(String conviteEscalaId);

    public Boolean resendConviteEscala(String phoneUser,
                                       String conviteEscalaId,
                                       String eventoId);

    public ConviteEscalaResponse editConviteEscalaAndResend(
            String convidadoEscalaId,
            String conviteEscalaId, ConviteEscalaMinisterioDTO dto);

    public ConviteEscalaResponse listConviteEscala(
            String conviteEscalaId);

}
