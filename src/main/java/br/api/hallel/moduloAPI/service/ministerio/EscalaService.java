package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.*;
import br.api.hallel.moduloAPI.dto.v2.ministerio.EscalaMinisterioResponseWithInfosV2;
import br.api.hallel.moduloAPI.mapper.ministerio.MinisterioMapper;
import br.api.hallel.moduloAPI.model.*;
import br.api.hallel.moduloAPI.repository.EscalaMinisterioRepository;
import br.api.hallel.moduloAPI.repository.MembroMinisterioRepository;
import br.api.hallel.moduloAPI.repository.MinisterioRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class EscalaService implements EscalaInterface {
    @Autowired
    EscalaMinisterioRepository escalaMinisterioRepository;
    @Autowired
    MembroMinisterioRepository membroMinisterioRepository;
    @Autowired
    NaoConfirmadoEscalaMinisterioService naoConfirmadoEscalaMinisterioService;

    @Autowired
    private MinisterioRepository ministerioRepository;

    @Override
    public EscalaMinisterioResponse createEscalaMinisterio(
            Eventos evento, String ministerioId) {
        log.info("Creating escala for ministerio " + ministerioId + "...");
        EscalaMinisterio escalaMinisterioToModel = new EscalaMinisterio();
        escalaMinisterioToModel.setMinisterioId(ministerioId);
        escalaMinisterioToModel.setDate(evento.getDate());
        escalaMinisterioToModel.setEventoId(evento.getId());
        escalaMinisterioToModel.setMembrosMinisterioConvidadosIds(new ArrayList<>());
        Optional<Ministerio> optional = ministerioRepository.findById(ministerioId);
        if (optional.isEmpty()) return null;
        Ministerio ministerio = optional.get();
        escalaMinisterioToModel.getMembrosMinisterioConvidadosIds()
                               .addAll(List.of(ministerio.getCoordenadorId(),
                                       ministerio.getViceCoordenadorId()));
        escalaMinisterioToModel.setMembrosMinisterioConfimadoIds(new ArrayList<>());
        escalaMinisterioToModel.setMembrosMinisterioNaoConfirmadoIds(new ArrayList<>());
        EscalaMinisterio escalaMinisterioSaved = this.escalaMinisterioRepository.save(escalaMinisterioToModel);
        log.info("Escala " + escalaMinisterioSaved.getId() + " created for event " + evento.getTitulo() + " to ministerio " + ministerioId);
        return MinisterioMapper.toEscalaMinisterioResponse(escalaMinisterioSaved);
    }

    @Override
    public EscalaMinisterioResponse editarDataEscalaMinisterio(
            String escalaMinisterioId,
            Date date) {
        log.info("Editing escala " + escalaMinisterioId + "...");
        Optional<EscalaMinisterio> escalaMinisterioOptional = this.escalaMinisterioRepository.findById(escalaMinisterioId);
        if (escalaMinisterioOptional.isEmpty()) {
            throw new RuntimeException("Can't find escala with id " + escalaMinisterioId);
        }
        EscalaMinisterio oldEscalaMinisterio = escalaMinisterioOptional.get();
        oldEscalaMinisterio.setDate(date);
        EscalaMinisterio escalaMinisterioUpdated = this.escalaMinisterioRepository.save(oldEscalaMinisterio);
        return MinisterioMapper.toEscalaMinisterioResponse(escalaMinisterioUpdated);
    }

    @Override
    public List<String> listMembroMinisterioCanInviteToEscala(
            String idEscala, int page, int size) {
        EscalaMinisterio escalaMinisterio = getEscalaMinisterioById(idEscala);
        Pageable pageable = PageRequest.of(page, size);
        Page<MembroMinisterio> membrosMinisterio = membroMinisterioRepository.findByMinisterioId(escalaMinisterio.getMinisterioId(), pageable);
        if (escalaMinisterio.getMembrosMinisterioConvidadosIds() == null
                || escalaMinisterio.getMembrosMinisterioConfimadoIds() == null)
            return new ArrayList<>();
        return membrosMinisterio.stream()
                                .map(MembroMinisterio::getMembroId)
                                .filter(membroId ->
                                        !escalaMinisterio.getMembrosMinisterioConvidadosIds()
                                                         .contains(membroId)
                                                && !escalaMinisterio.getMembrosMinisterioConfimadoIds()
                                                                    .contains(membroId)
                                                && !this
                                                .naoConfirmadoEscalaMinisterioService
                                                .verifyIfMembroMinisterioIsAlreadyNotConfirmed(idEscala, membroId))

                                .toList();
    }

    @Override
    public EscalaMinisterioResponse alterarEscalaConvidarMembroMinisterio(
            String idEscala, List<String> idsMembrosMinisterio) {
        log.info("Inviting members into escala {}", idEscala);
        EscalaMinisterio escalaMinisterio = getEscalaMinisterioById(idEscala);

        if (escalaMinisterio.getMembrosMinisterioConvidadosIds() == null) {
            escalaMinisterio.setMembrosMinisterioConvidadosIds(new ArrayList<>());
        }

        idsMembrosMinisterio.forEach(idMembro -> {
            if (!escalaMinisterio.getMembrosMinisterioConvidadosIds()
                                 .contains(idMembro)) {
                escalaMinisterio.getMembrosMinisterioConvidadosIds()
                                .add(idMembro);
            } else {
                log.info("Membro ministerio " + idMembro + " invited!");
            }
        });
        EscalaMinisterio escalaMinisterioEdited = this.escalaMinisterioRepository.save(escalaMinisterio);
        return MinisterioMapper.toEscalaMinisterioResponse(escalaMinisterioEdited);
    }

    @Override
    public EscalaMinisterioResponse alterarEscalaDesconvidarMembroMinisterio(
            String idEscala, List<String> idsMembrosMinisterio) {
        log.info("Uninviting members into escala {}", idEscala);
        EscalaMinisterio escalaMinisterio = getEscalaMinisterioById(idEscala);

        if (escalaMinisterio.getMembrosMinisterioConvidadosIds() == null) {
            escalaMinisterio.setMembrosMinisterioConvidadosIds(new ArrayList<>());
        }

        idsMembrosMinisterio.forEach(idMembro -> {

            if (escalaMinisterio.getMembrosMinisterioConvidadosIds()
                                .contains(idMembro)) {
                escalaMinisterio.getMembrosMinisterioConvidadosIds()
                                .remove(idMembro);
            } else {
                log.info("Membro ministerio " + idMembro + " isn't invited!");
            }
        });
        EscalaMinisterio escalaMinisterioEdited = this.escalaMinisterioRepository.save(escalaMinisterio);
        return MinisterioMapper.toEscalaMinisterioResponse(escalaMinisterioEdited);
    }

    @Override
    public EscalaMinisterioResponse alterarEscalaConfirmandoMembroMinisterio(
            String idEscala, List<String> idsMembrosMinisterio) {
        log.info("Editing escala ministerio " + idEscala + " confirming members...");
        EscalaMinisterio escalaMinisterio = getEscalaMinisterioById(idEscala);

        if (escalaMinisterio.getMembrosMinisterioConfimadoIds() == null) {
            escalaMinisterio.setMembrosMinisterioConfimadoIds(new ArrayList<>());
        }

        idsMembrosMinisterio.forEach(idMembro -> {
            if (escalaMinisterio.getMembrosMinisterioConvidadosIds()
                                .contains(idMembro)) {
                escalaMinisterio.getMembrosMinisterioConvidadosIds()
                                .removeIf(item -> item.equals(idMembro));
                escalaMinisterio.getMembrosMinisterioConfimadoIds()
                                .add(idMembro);
            } else {
                log.info("Membro ministerio " + idMembro + " isn't invited!");
            }
        });

        EscalaMinisterio escalaMinisterioEdited = this.escalaMinisterioRepository.save(escalaMinisterio);
        return MinisterioMapper.toEscalaMinisterioResponse(escalaMinisterioEdited);
    }

    @Override
    public EscalaMinisterioResponse alterarEscalaNaoConfirmandoMembroMinisterio(
            String idEscala,
            List<NaoConfirmadoEscalaDTOAdm> naoConfirmadoEscalaDtoAdm) {
        log.info("Editing escala ministerio " + idEscala + " recusing members...");
        EscalaMinisterio escalaMinisterio = getEscalaMinisterioById(idEscala);

        if (escalaMinisterio.getMembrosMinisterioNaoConfirmadoIds() == null) {
            escalaMinisterio.setMembrosMinisterioNaoConfirmadoIds(new ArrayList<>());
        }

        naoConfirmadoEscalaDtoAdm.forEach(naoConfirmado -> {
            if (escalaMinisterio.getMembrosMinisterioConvidadosIds()
                                .contains(naoConfirmado.getIdMembroMinisterio())) {
                NaoConfirmadoEscalaMinisterio naoConfirmadoEscalaMinisterio =
                        naoConfirmadoEscalaMinisterioService.createNaoConfirmadoEscalaMinisterio(
                                new NaoConfirmarEscalaDTO(
                                        naoConfirmado.getIdMembroMinisterio(),
                                        idEscala,
                                        naoConfirmado.getMotivo()));
                escalaMinisterio.getMembrosMinisterioNaoConfirmadoIds()
                                .add(naoConfirmadoEscalaMinisterio.getId());
            } else {
                log.info("Membro ministerio " + naoConfirmado.getIdMembroMinisterio() + " isn't invited!");
            }
        });

        EscalaMinisterio escalaMinisterioEdited = this.escalaMinisterioRepository.save(escalaMinisterio);
        return MinisterioMapper.toEscalaMinisterioResponse(escalaMinisterioEdited);
    }


    @Override
    public List<EscalaMinisterioWithEventoInfoResponse> listEscalaMinisterio() {
        log.info("Listing all the escala ministerio...");
        return this.escalaMinisterioRepository.findAllWithEventosInfos();
    }

    @Override
    public List<EscalaMinisterioWithEventoInfoResponse> listEscalaMinisterioMembroIdCanParticipate(
            String membroId, LocalDateTime start, LocalDateTime end) {
        return this.escalaMinisterioRepository.findAllWithEventosInfosCanParticipateByMembroId(membroId, start, end);
    }

    @Override
    public List<SimpleEscalaMinisterioResponse> listEscalaMinisterioIdsByMembroIdThatCanParticipate(
            String membroId, LocalDateTime start, LocalDateTime end) {
        return this.escalaMinisterioRepository.findEscalaMinisterioIdsByMembroIdCanPaticipate(membroId, start, end);
    }

    @Override
    public List<EscalaMinisterioWithEventoInfoResponse> listEscalaMinisterioConfirmedMembro(
            String membroId, LocalDateTime start, LocalDateTime end) {
        return this.escalaMinisterioRepository.findAllWithEventosInfosConfirmedByMembroId(membroId, start, end);
    }

    @Override
    public List<SimpleEscalaMinisterioResponse> listEscalaMinisterioIdsByMembroIdThatConfirmed(
            String membroId, LocalDateTime start, LocalDateTime end) {
        return this.escalaMinisterioRepository.findEscalaMinisterioIdsByMembroIdParticipate(membroId, start, end);
    }

    @Override
    public List<EscalaMinisterioWithEventoInfoResponse> listEscalaMinisterioRangeDate(
            LocalDateTime start, LocalDateTime end) {
        return this.escalaMinisterioRepository.findAllWithEventosInfosRangeDate(start, end);
    }

    @Override
    public List<SimpleEscalaMinisterioResponse> listEscalaMinisterioIdsByMinisterioIdAndRangeDate(
            String idMinisterio, LocalDateTime start,
            LocalDateTime end) {
        return this.escalaMinisterioRepository.findAllIdsByMinisterioIdAndRangeDate(idMinisterio, start, end);
    }

    @Override
    public List<EscalaMinisterioWithEventoInfoResponse> listEscalaMinisterioRangeDateByMinisterioId(
            String idMinisterio, LocalDateTime start,
            LocalDateTime end) {
        return this.escalaMinisterioRepository.findAllWithEventosInfosRangeDateByMinisterioId(idMinisterio, start, end);
    }

    @Override
    public EscalaMinisterioResponseWithInfos listEscalaMinisterioByIdWithInfos(
            String idEscalaMinisterio) {
        log.info("Listing info of escala " + idEscalaMinisterio);
        return this.escalaMinisterioRepository.findWithAllInfosById(idEscalaMinisterio);
    }

    @Override
    public EscalaMinisterioResponseWithInfosV2 listEscalaMinisterioByIdWithInfosV2(
            String idEscalaMinisterio) {
        log.info("V2: Listing info of escala " + idEscalaMinisterio);
        return this.escalaMinisterioRepository.findByIdWithAllInfos(idEscalaMinisterio);
    }

    @Override

    public List<NaoConfirmadoEscalaMinisterioWithInfos> listMotivosAusenciaMembroEventoByIdEscalasMinisterio(
            String idEscala) {
        log.info("Listing motivos ausencia membro in escala...");
        return naoConfirmadoEscalaMinisterioService.naoConfirmadoEscalaMinisterioRepository.findAllWithEscalaId(idEscala);
    }

    @Override
    public void deleteEscalasWithDeletingEvento(String idEvento) {
        log.info("Deleting escala when deleting evento " + idEvento + "...");
        List<EscalaMinisterio> escalas = escalaMinisterioRepository.findByEventoId(idEvento);
        for (EscalaMinisterio escalaMinisterio : escalas) {
            if (escalaMinisterio.getMembrosMinisterioNaoConfirmadoIds() != null) {
                escalaMinisterio.getMembrosMinisterioNaoConfirmadoIds()
                                .forEach(naoConfirmadoEscalaMinisterioService::deleteNaoConfirmadoEscalaMinisterio);
            }
            escalaMinisterioRepository.delete(escalaMinisterio);
            log.info("Escala " + escalaMinisterio.getId() + " date " + escalaMinisterio.getDate() + " deleted");
        }
    }

    @Override
    public EscalaMinisterioResponseWithInfosV2 adicionarRemoverRepertorioInEscala(
            String idEscalaMinisterio,
            EscalaRepertorioDTO escalaRepertorioDTO) {
        log.info("Adding or removing repertorio of escala " + idEscalaMinisterio + "...");
        EscalaMinisterio escalaMinisterio = getEscalaMinisterioById(idEscalaMinisterio);
        List<String> repertorioIds = new ArrayList<>();
        if (escalaMinisterio.getRepertorioIds() != null) {
            repertorioIds = escalaMinisterio.getRepertorioIds();
        }
        if (escalaRepertorioDTO.getRepertorioIdsAdd() != null) {
            repertorioIds.addAll(escalaRepertorioDTO.getRepertorioIdsAdd());
        }
        if (escalaRepertorioDTO.getRepertorioIdsRemove() != null) {
            repertorioIds = repertorioIds.stream()
                                         .filter(repertorio -> !(escalaRepertorioDTO.getRepertorioIdsRemove()
                                                                                    .contains(repertorio)))
                                         .toList();
        }
        escalaMinisterio.setRepertorioIds(repertorioIds);
        escalaMinisterioRepository.save(escalaMinisterio);
        return listEscalaMinisterioByIdWithInfosV2(idEscalaMinisterio);
    }


//    private EscalaMinisterio getEscalaMinisterioWithConvitesMembro(
//            EscalaMinisterio escalaMinisterio) {
//        log.info("Getting escala ministerio with convites for all membros...");
//        EscalaMinisterio escalaMinisterioReturn = new EscalaMinisterio(escalaMinisterio.getId(),
//                escalaMinisterio.getMinisterioId(),
//                escalaMinisterio.getEventoId(),
//                escalaMinisterio.getDate());
//        List<MembroMinisterio> membroMinisterioList = membroMinisterioRepository.findByMinisterioId(escalaMinisterio.getMinisterioId());
//        List<String> idsMembrosFromMinisterio = new ArrayList<>();
//        membroMinisterioList.forEach(membroMinisterio -> {
//            idsMembrosFromMinisterio.add(membroMinisterio.getMembroId());
//        });
//        escalaMinisterioReturn.setMembrosMinisterioConvidadosIds(idsMembrosFromMinisterio);
//        return escalaMinisterioReturn;
//    }

    public EscalaMinisterio getEscalaMinisterioById(
            String idEscalaMinisterio) {
        Optional<EscalaMinisterio> optional = escalaMinisterioRepository.findById(idEscalaMinisterio);
        if (optional.isEmpty()) {
            throw new RuntimeException("Can't get the ministerio by this id");
        }
        return optional.get();
    }
}
