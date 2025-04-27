package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.*;
import br.api.hallel.moduloAPI.exceptions.ministerio.ConvidadoEscalaMinisterioNotFound;
import br.api.hallel.moduloAPI.exceptions.ministerio.MessageTelegramNotSendToConvidadoException;
import br.api.hallel.moduloAPI.mapper.ministerio.MinisterioMapper;
import br.api.hallel.moduloAPI.model.ConvidadoEscalaMinisterio;
import br.api.hallel.moduloAPI.model.ConviteEscalaMinisterio;
import br.api.hallel.moduloAPI.model.EscalaMinisterio;
import br.api.hallel.moduloAPI.model.Eventos;
import br.api.hallel.moduloAPI.repository.ConvidadoEscalaMinisterioRepository;
import br.api.hallel.moduloAPI.service.eventos.EventosService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ConvidadoEscalaMinisterioService
        implements ConvidadoEscalaMinisterioInterface {


    @Autowired
    private ConvidadoEscalaMinisterioRepository repository;
    @Autowired
    private ConviteEscalaMinisterioService conviteEscalaMinisterioService;
    @Autowired
    private EscalaService escalaService;
    @Autowired
    private EventosService eventosService;

    @Autowired
    private TelegramService telegramService;

    @Override
    public ConvidadoEscalaMinisterioWithConviteResponse createConvidadoAndSendMessage(
            ConvidadoEscalaMinisterioDTO dto) {
        log.info("Creating convidado and send message to telegram...");
        ConviteEscalaResponse conviteEscala;
        EscalaMinisterio escalaMinisterioById = escalaService.getEscalaMinisterioById(dto.getEscalaMinisterioId());
        Eventos eventoEscala = eventosService.listarEventoById(escalaMinisterioById.getEventoId());
        boolean responseSendMessage = telegramService.sendMessageWithEventToContact(dto.getTelefone(), dto.getMensagem(), eventoEscala);

        if (!responseSendMessage) {
            throw new MessageTelegramNotSendToConvidadoException("Can't send message to convidado number %s".formatted(dto.getTelefone()));
        }

        if (dto.getConviteEscalaId() == null) {
            conviteEscala = conviteEscalaMinisterioService.createConviteEscala(new ConviteEscalaMinisterioDTO(dto.getMensagem()));
        } else {
            conviteEscala = conviteEscalaMinisterioService.listConviteEscala(dto.getConviteEscalaId());
        }

        ConvidadoEscalaMinisterio convidadoEscalaMinisterio = new ConvidadoEscalaMinisterio();
        convidadoEscalaMinisterio.setEmail(dto.getEmail());
        convidadoEscalaMinisterio.setNome(dto.getNome());
        convidadoEscalaMinisterio.setTelefone(dto.getTelefone());
        convidadoEscalaMinisterio.setConviteEscalaId(conviteEscala.getId());
        convidadoEscalaMinisterio.setEscalaMinisterioId(dto.getEscalaMinisterioId());


        ConvidadoEscalaMinisterio convidadoEscalaInserted = repository.insert(convidadoEscalaMinisterio);
        log.info("Convidado id {} created", convidadoEscalaInserted.getId());
        return MinisterioMapper.toConvidadoWithConviteResponse(convidadoEscalaInserted);
    }

    @Override
    public Boolean deleteConvidadoAndSendMessage(
            String idConvidadoEscalaMinisterio) {
        log.info("Delete convidado {} and send message of cancel his participation...", idConvidadoEscalaMinisterio);

        Optional<ConvidadoEscalaMinisterio> optional = this.repository.findById(idConvidadoEscalaMinisterio);

        if (optional.isEmpty()) {
            throw new ConvidadoEscalaMinisterioNotFound("Can't find convidado by id %s".formatted(idConvidadoEscalaMinisterio));
        }

        ConvidadoEscalaMinisterio convidadoEscalaMinisterio = optional.get();
        EscalaMinisterio escalaMinisterioById = escalaService.getEscalaMinisterioById(convidadoEscalaMinisterio.getEscalaMinisterioId());
        Eventos eventos = eventosService.listarEventoById(escalaMinisterioById.getEventoId());
        boolean messageSended = telegramService.sendMessageUninvite(convidadoEscalaMinisterio.getTelefone(), null, eventos);
        if (!messageSended) {
            throw new MessageTelegramNotSendToConvidadoException("Can't send message to convidado number %s".formatted(convidadoEscalaMinisterio.getTelefone()));
        }
        this.conviteEscalaMinisterioService.deleteConviteEscala(convidadoEscalaMinisterio.getConviteEscalaId());

        this.repository.delete(convidadoEscalaMinisterio);
        return true;
    }

    @Override
    public Boolean deleteConviteOfConvidado(
            String idConvidadoEscalaMinisterio) {
        log.info("Delete convite of convidado {}", idConvidadoEscalaMinisterio);
        Optional<ConvidadoEscalaMinisterio> optional = this.repository.findById(idConvidadoEscalaMinisterio);

        if (optional.isEmpty()) {
            throw new ConvidadoEscalaMinisterioNotFound("Can't find convidado by id %s".formatted(idConvidadoEscalaMinisterio));
        }

        ConvidadoEscalaMinisterio convidadoEscalaMinisterio = optional.get();

        return conviteEscalaMinisterioService.deleteConviteEscala(convidadoEscalaMinisterio.getConviteEscalaId());
    }

    @Override
    public List<ConvidadoEscalaMinisterioUserResponse> listConvidadosUserInfos(
            String idEscalaMinisterio) {
        log.info("List convidados with user infos...");
        return this.repository.listAllConvidadosUserInfosByEscalaId(idEscalaMinisterio);
    }

    @Override
    public List<ConvidadoEscalaMinisterioWithConviteResponse> listConvidadosWithConvites(
            String idEscalaMinsterio) {
        log.info("Listing convidados with convites infos of escala {}...", idEscalaMinsterio);
        return this.repository.listAllConvidadosWithConvitesInfosByEscalaId(idEscalaMinsterio);
    }

    @Override
    public ConvidadoEscalaMinisterioWithInfos listConvidadoInfosById(
            String idConvidadoEscalaMinisterio) {
        log.info("Listing convidados infos by id {}...", idConvidadoEscalaMinisterio);
        Optional<ConvidadoEscalaMinisterio> optional = this.repository.findById(idConvidadoEscalaMinisterio);

        if (optional.isEmpty()) {
            throw new ConvidadoEscalaMinisterioNotFound("Can't find convidado by id %s".formatted(idConvidadoEscalaMinisterio));
        }

        return MinisterioMapper.toConvidadoEscalaMinisterioWithInfos(optional.get());
    }

    @Override
    public ConvidadoEscalaMinisterioWithConviteResponse editConvidado(
            String idConvidado,
            EditConvidadoEscalaMinisterioDTO dto) {
        log.info("Editing convidado {} infos...", idConvidado);
        Optional<ConvidadoEscalaMinisterio> optional = this.repository.findById(idConvidado);

        if (optional.isEmpty()) {
            throw new ConvidadoEscalaMinisterioNotFound("Can't find convidado by id %s".formatted(idConvidado));
        }

        ConvidadoEscalaMinisterio convidadoOld = optional.get();
        convidadoOld.setNome(dto.getNome());
        convidadoOld.setEmail(dto.getEmail());
        if (!convidadoOld.getTelefone().equals(dto.getTelefone())) {
            convidadoOld.setTelefone(dto.getTelefone());
            log.info("Sending message to new telefone of guest {}...", convidadoOld.getId());
            EscalaMinisterio escalaMinisterioById = escalaService.getEscalaMinisterioById(convidadoOld.getEscalaMinisterioId());
            Eventos eventoEscala = eventosService.listarEventoById(escalaMinisterioById.getEventoId());
            ConviteEscalaResponse conviteEscalaResponse = conviteEscalaMinisterioService.listConviteEscala(convidadoOld.getConviteEscalaId());
            telegramService.sendMessageWithEventToContact(convidadoOld.getTelefone(), conviteEscalaResponse.getMensagem(), eventoEscala);
        }
        ConvidadoEscalaMinisterio convidadoUpdated = this.repository.save(convidadoOld);
        return MinisterioMapper.toConvidadoWithConviteResponse(convidadoUpdated);
    }
}
