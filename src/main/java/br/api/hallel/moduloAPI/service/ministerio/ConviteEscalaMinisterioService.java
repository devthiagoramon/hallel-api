package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.ConvidadoEscalaMinisterioDTO;
import br.api.hallel.moduloAPI.dto.v1.ministerio.ConviteEscalaMinisterioDTO;
import br.api.hallel.moduloAPI.dto.v1.ministerio.ConviteEscalaResponse;
import br.api.hallel.moduloAPI.exceptions.ministerio.ConvidadoEscalaMinisterioNotFound;
import br.api.hallel.moduloAPI.exceptions.ministerio.ConviteEscalaMinisterioNotFoundException;
import br.api.hallel.moduloAPI.mapper.ministerio.MinisterioMapper;
import br.api.hallel.moduloAPI.model.ConvidadoEscalaMinisterio;
import br.api.hallel.moduloAPI.model.ConviteEscalaMinisterio;
import br.api.hallel.moduloAPI.model.Eventos;
import br.api.hallel.moduloAPI.repository.ConvidadoEscalaMinisterioRepository;
import br.api.hallel.moduloAPI.repository.ConviteEscalaMinisterioRepository;
import br.api.hallel.moduloAPI.service.eventos.EventosService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Log4j2
public class ConviteEscalaMinisterioService
        implements ConviteEscalaMInisterioInterface {

    @Autowired
    private ConviteEscalaMinisterioRepository repository;
    @Autowired
    private ConvidadoEscalaMinisterioRepository convidadoEscalaMinisterioRepository;
    @Autowired
    private EventosService eventosService;

    @Autowired
    private TelegramService telegramService;

    @Override
    public ConviteEscalaResponse createConviteEscala(
            ConviteEscalaMinisterioDTO dto) {
        log.info("Creating convite escala ministério...");
        ConviteEscalaMinisterio conviteEscalaDTO = new ConviteEscalaMinisterio();
        conviteEscalaDTO.setDateEdit(null);
        conviteEscalaDTO.setDateSend(new Date());
        conviteEscalaDTO.setEnviado(true);
        conviteEscalaDTO.setMensagem(dto.getMensagem());

        ConviteEscalaMinisterio conviteInserted = repository.save(conviteEscalaDTO);

        return MinisterioMapper.toConviteEscalaResponse(conviteInserted);
    }

    @Override
    public Boolean deleteConviteEscala(String conviteEscalaId) {
        log.info("Delete convite escala ministério id {}...", conviteEscalaId);
        Optional<ConviteEscalaMinisterio> optional = this.repository.findById(conviteEscalaId);
        if (optional.isEmpty()) {
            throw new ConviteEscalaMinisterioNotFoundException("Can't find convite escala ministerio id %s".formatted(conviteEscalaId));
        }
        this.repository.delete(optional.get());
        return true;
    }

    @Override
    public Boolean resendConviteEscala(String phoneUser,
                                       String conviteEscalaId,
                                       String eventoId) {
        log.info("Resend message for number {}...", phoneUser);
        Optional<ConviteEscalaMinisterio> optional = this.repository.findById(conviteEscalaId);
        if (optional.isEmpty()) {
            throw new ConviteEscalaMinisterioNotFoundException("Can't find convite escala ministerio id %s".formatted(conviteEscalaId));
        }
        ConviteEscalaMinisterio conviteEscala = optional.get();
        Eventos evento = eventosService.listarEventoById(eventoId);
        boolean messageSended = telegramService.sendMessageWithEventToContact(phoneUser, conviteEscala.getMensagem(), evento);
        if (messageSended) {
            conviteEscala.setDateEdit(new Date());
            repository.save(conviteEscala);
        }
        return messageSended;
    }

    @Override
    public ConviteEscalaResponse editConviteEscalaAndResend(
            String convidadoEscalaId,
            String conviteEscalaId,
            ConviteEscalaMinisterioDTO dto) {
        log.info("Editing convite {} and resend message to {}", conviteEscalaId, convidadoEscalaId);
        Optional<ConviteEscalaMinisterio> optional = this.repository.findById(conviteEscalaId);
        if (optional.isEmpty()) {
            throw new ConviteEscalaMinisterioNotFoundException("Can't find convite escala ministerio id %s".formatted(conviteEscalaId));
        }
        ConviteEscalaMinisterio conviteEscalaOld = optional.get();
        Optional<ConvidadoEscalaMinisterio> optionalConvidado = this.convidadoEscalaMinisterioRepository.findById(convidadoEscalaId);
        if (optionalConvidado.isEmpty()) {
            throw new ConvidadoEscalaMinisterioNotFound("Can't find convidado escala ministerio by id %s".formatted(convidadoEscalaId));
        }
        ConvidadoEscalaMinisterio convidadoEscalaMinisterio = optionalConvidado.get();
        conviteEscalaOld.setMensagem(dto.getMensagem());
        conviteEscalaOld.setEnviado(false);
        conviteEscalaOld.setDateEdit(new Date());
        boolean messageSended = telegramService.sendMessageToContact(convidadoEscalaMinisterio.getTelefone(), conviteEscalaOld.getMensagem());

        if (messageSended) {
            conviteEscalaOld.setDateEdit(new Date());
            conviteEscalaOld.setEnviado(true);
        }
        ConviteEscalaMinisterio conviteEscalaSaved = repository.save(conviteEscalaOld);
        return MinisterioMapper.toConviteEscalaResponse(conviteEscalaSaved);
    }

    @Override
    public ConviteEscalaResponse listConviteEscala(
            String conviteEscalaId) {
        log.info("Listing convite escala {}...", conviteEscalaId);
        Optional<ConviteEscalaMinisterio> optional = this.repository.findById(conviteEscalaId);
        if (optional.isEmpty()) {
            throw new ConviteEscalaMinisterioNotFoundException("Can't find convite escala ministerio id %s".formatted(conviteEscalaId));
        }
        return MinisterioMapper.toConviteEscalaResponse(optional.get());
    }
}
