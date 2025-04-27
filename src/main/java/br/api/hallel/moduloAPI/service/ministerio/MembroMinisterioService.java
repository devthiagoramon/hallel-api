package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.*;
import br.api.hallel.moduloAPI.exceptions.ministerio.ConfirmDeclineParticipationMembroInEnsaioException;
import br.api.hallel.moduloAPI.mapper.ministerio.MinisterioMapper;
import br.api.hallel.moduloAPI.model.EscalaMinisterio;
import br.api.hallel.moduloAPI.model.MembroMinisterio;
import br.api.hallel.moduloAPI.model.Ministerio;
import br.api.hallel.moduloAPI.model.NaoConfirmadoEscalaMinisterio;
import br.api.hallel.moduloAPI.payload.resposta.MembroResponse;
import br.api.hallel.moduloAPI.repository.*;
import br.api.hallel.moduloAPI.service.google.GoogleBucketService;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class MembroMinisterioService
        implements MembroMinisterioInterface {

    @Autowired
    MembroMinisterioRepository membroMinisterioRepository;
    @Autowired
    MembroRepository membroRepository;
    @Autowired
    EscalaMinisterioRepository escalaMinisterioRepository;
    @Autowired
    NaoConfirmadoEscalaMinisterioService naoConfirmadoEscalaMinisterioService;
    @Autowired
    EscalaService escalaService;
    @Autowired
    MinisterioRepository ministerioRepository;
    @Autowired
    RepertorioRepository repertorioRepository;
    @Autowired
    MusicaMinisterioRepository musicaMinisterioRepository;
    @Autowired
    DancaMinisterioRepository dancaMinisterioRepository;
    @Autowired
    private GoogleBucketService googleBucketService;

    @NotNull
    public MembroMinisterio getMembroMinisterioById(
            String idMembroMinisterio) {
        Optional<MembroMinisterio> optional = membroMinisterioRepository.findById(idMembroMinisterio);

        if (optional.isEmpty()) {
            throw new RuntimeException("Can't find membroMinisterio by this id");
        }
        return optional.get();
    }

    @Override
    public Slice<MembroResponse> listMembrosToAddIntoThisMinisterio(
            String idMinisterio, int page, int size) {
        return membroRepository.findMembrosWithNoParticipationInThisMinisterio(idMinisterio, PageRequest.of(page, size));
    }

    @Override
    public Slice<MembroMinisterioWithInfosResponse> listMembrosFromMinisterio(
            String idMinisterio, int page, int size) {
        return membroMinisterioRepository.findWithInfosByMinisterioId(idMinisterio, PageRequest.of(page, size));
    }

    @Override
    public MembroMinisterioWithInfosResponse listMembroMinisterioById(
            String idMembroMinisterio) {
        Optional<MembroMinisterioWithInfosResponse> membroMinisterioWithInfosResponse = membroMinisterioRepository.findWithInfosId(idMembroMinisterio);
        if (membroMinisterioWithInfosResponse.isEmpty()) {
            throw new RuntimeException("Can't find user ministerio by this id");
        }
        return membroMinisterioWithInfosResponse.get();
    }

    @Override
    public MembroMinisterio listMembroMinisterioByMembroIdAndMinisterioId(
            String idMembro, String idMinisterio) {
        Optional<MembroMinisterio> membroMinisterioOptinal = membroMinisterioRepository.findByMinisterioIdAndMembroId(idMinisterio, idMembro);

        if (membroMinisterioOptinal.isEmpty()) {
            throw new RuntimeException("Can' find user ministerio by membroId and ministerioId");
        }

        return membroMinisterioOptinal.get();
    }

    @Override
    public MembroMinisterio addMembroMinisterio(
            AddMembroMinisterioDTO addMembroMinisterioDTO) {
        log.info("Adding member into ministerio...");
        if (verifyIfMembroIsOnMinisterioAlready(addMembroMinisterioDTO.getMinisterioId(), addMembroMinisterioDTO.getMembroId())) {
            throw new RuntimeException("Can't add member into ministerio: he's already there");
        }

        MembroMinisterio membroMinisterio = MinisterioMapper.toMembroMinisterio(addMembroMinisterioDTO);
        MembroMinisterio membroMinisterioInserted = membroMinisterioRepository.insert(membroMinisterio);
        log.info("Member id " + membroMinisterioInserted.getMembroId() + " added into " + membroMinisterio.getMinisterioId());
        return membroMinisterioInserted;
    }

    @Override
    public void removerMembroMinisterio(String idMembroMinisterio) {
        Optional<MembroMinisterio> optional = membroMinisterioRepository.findById(idMembroMinisterio);
        if (optional.isEmpty()) {
            throw new RuntimeException("Can't find membroMinisterio by this id");
        }
        MembroMinisterio membroMinisterio = optional.get();
        membroMinisterioRepository.delete(membroMinisterio);
    }

    public Boolean verifyIfMembroIsOnMinisterioAlready(
            String idMinisterio, String idMembro) {
        return membroMinisterioRepository.findByMinisterioIdAndMembroId(idMinisterio, idMembro)
                                         .isPresent();
    }

    @Override
    public void removerMembroMinisterio(
            MembroMinisterio membroMinisterio) {
        log.info("Removing membro from ministerio...");
        this.membroMinisterioRepository.delete(membroMinisterio);
        log.info("Member removed!");
    }

    @Override
    public StatusParticipacaoEscalaMinisterio getStatusParticipacaoEscala(
            String idMembroMinisterio, String idEscalaMinisterio) {
        log.info("Get status of membro " + idMembroMinisterio + " about the scale " + idEscalaMinisterio);
        EscalaMinisterio escalaMinisterio = escalaService.getEscalaMinisterioById(idEscalaMinisterio);
        if (escalaMinisterio.getMembrosMinisterioConfimadoIds() != null && escalaMinisterio.getMembrosMinisterioConfimadoIds()
                                                                                           .contains(idMembroMinisterio)) {
            return StatusParticipacaoEscalaMinisterio.CONFIRMADO;
        }
        if (escalaMinisterio.getMembrosMinisterioConvidadosIds() != null && escalaMinisterio.getMembrosMinisterioConvidadosIds()
                                                                                            .contains(idMembroMinisterio)) {
            return StatusParticipacaoEscalaMinisterio.CONVIDADO;
        }
        if (escalaMinisterio.getMembrosMinisterioNaoConfirmadoIds() != null) {
            for (String membrosMinisterioNaoConfirmadoId : escalaMinisterio.getMembrosMinisterioNaoConfirmadoIds()) {
                Optional<NaoConfirmadoEscalaMinisterio> optional = naoConfirmadoEscalaMinisterioService.naoConfirmadoEscalaMinisterioRepository.findById(membrosMinisterioNaoConfirmadoId);
                if (optional.isEmpty()) {
                    throw new RuntimeException("Can't find NaoConfirmadoEscalMinisterio by Id");
                }
                NaoConfirmadoEscalaMinisterio naoConfirmadoEscalaMinisterio = optional.get();
                if (idMembroMinisterio.equals(naoConfirmadoEscalaMinisterio.getIdMembroMinisterio())) {
                    return StatusParticipacaoEscalaMinisterio.RECUSADO;
                }
            }
        }
        throw new RuntimeException("Can't found this membro by id");
    }

    @Override
    public Boolean confirmarParticipacaoEscala(
            String idMembro, String idEscalaMinisterio) {
        log.info("Confirming participantion of membro " + idMembro + " into " + idEscalaMinisterio);
        EscalaMinisterio escalaMinisterio = escalaService.getEscalaMinisterioById(idEscalaMinisterio);

        if (escalaMinisterio.getMembrosMinisterioConvidadosIds()
                            .contains(idMembro)) {
            escalaMinisterio.getMembrosMinisterioConvidadosIds()
                            .removeIf(item -> item.equals(idMembro));
        } else {
            throw new RuntimeException("Member hasn't been invited!");
        }
        if (escalaMinisterio.getMembrosMinisterioConfimadoIds() == null) {
            escalaMinisterio.setMembrosMinisterioConfimadoIds(new ArrayList<>());
        }
        escalaMinisterio.getMembrosMinisterioConfimadoIds()
                        .add(idMembro);
        this.escalaMinisterioRepository.save(escalaMinisterio);
        return true;
    }

    @Override
    public Boolean confirmarParticipacaoEnsaio(String idMembro,
                                               String idEnsaioMinisterio) {
        log.info("Confirming participantion of membro " + idMembro + " into " + idEnsaioMinisterio);
        Optional<EscalaMinisterio> optional = this.escalaMinisterioRepository.findByEnsaioMinisterioId(idEnsaioMinisterio);

        if (optional.isEmpty()) {
            throw new ConfirmDeclineParticipationMembroInEnsaioException("Can't find ensaio by id %s".formatted(idEnsaioMinisterio));
        }

        EscalaMinisterio escalaMinisterio = optional.get();

        if (escalaMinisterio.getMembrosMinisterioConvidadosIds()
                            .contains(idMembro)) {
            escalaMinisterio.getMembrosMinisterioConvidadosIds()
                            .removeIf(item -> item.equals(idMembro));
        } else {
            throw new RuntimeException("Member hasn't been invited!");
        }
        if (escalaMinisterio.getMembrosMinisterioConfimadoIds() == null) {
            escalaMinisterio.setMembrosMinisterioConfimadoIds(new ArrayList<>());
        }
        escalaMinisterio.getMembrosMinisterioConfimadoIds()
                        .add(idMembro);
        this.escalaMinisterioRepository.save(escalaMinisterio);
        return true;

    }

    @Override
    public Boolean recusarParticipacaoEscala(
            NaoConfirmarEscalaDTO naoConfirmarEscalaDTO) {
        log.info("Recusing participantion of membro " + naoConfirmarEscalaDTO.getIdMembroMinisterio() + " into " + naoConfirmarEscalaDTO.getIdEscalaMinisterio());
        EscalaMinisterio escalaMinisterio = escalaService.getEscalaMinisterioById(naoConfirmarEscalaDTO.getIdEscalaMinisterio());

        if (escalaMinisterio.getMembrosMinisterioConvidadosIds()
                            .contains(naoConfirmarEscalaDTO.getIdMembroMinisterio())) {
            escalaMinisterio.getMembrosMinisterioConvidadosIds()
                            .removeIf(item -> item.equals(naoConfirmarEscalaDTO.getIdMembroMinisterio()));
        } else {
            throw new RuntimeException("Member hasn't been invited!");
        }


        NaoConfirmadoEscalaMinisterio naoConfirmadoEscalaMinisterio = naoConfirmadoEscalaMinisterioService.createNaoConfirmadoEscalaMinisterio(naoConfirmarEscalaDTO);
        log.info("NaoConfirmadoEscalaMinisterio inserted with id " + naoConfirmadoEscalaMinisterio.getIdEscalaMinisterio());
        if (escalaMinisterio.getMembrosMinisterioNaoConfirmadoIds() == null) {
            escalaMinisterio.setMembrosMinisterioNaoConfirmadoIds(new ArrayList<>());
        }

        escalaMinisterio.getMembrosMinisterioNaoConfirmadoIds()
                        .add(naoConfirmadoEscalaMinisterio.getId());
        this.escalaMinisterioRepository.save(escalaMinisterio);
        return true;
    }

    @Override
    public Boolean recusarParticipacaoEnsaio(
            NaoConfirmarEnsaioDTO naoConfirmarEnsaioDTO) {
        log.info("Recusing participantion of membro {} in ensaio {}", naoConfirmarEnsaioDTO.getIdMembro(), naoConfirmarEnsaioDTO.getIdEnsaioMinisterio());
        Optional<EscalaMinisterio> optional = this.escalaMinisterioRepository.findByEnsaioMinisterioId(naoConfirmarEnsaioDTO.getIdEnsaioMinisterio());

        if (optional.isEmpty()) {
            throw new ConfirmDeclineParticipationMembroInEnsaioException("Can't find ensaio by id %s".formatted(naoConfirmarEnsaioDTO.getIdEnsaioMinisterio()));
        }

        EscalaMinisterio escalaMinisterio = optional.get();

        if (escalaMinisterio.getMembrosMinisterioConvidadosIds()
                            .contains(naoConfirmarEnsaioDTO.getIdMembro())) {
            escalaMinisterio.getMembrosMinisterioConvidadosIds()
                            .removeIf(item -> item.equals(naoConfirmarEnsaioDTO.getIdMembro()));
        } else {
            throw new ConfirmDeclineParticipationMembroInEnsaioException("Member hasn't been invited!");
        }

        NaoConfirmadoEscalaMinisterio naoConfirmadoEscalaMinisterio = naoConfirmadoEscalaMinisterioService.createNaoConfirmadoEscalaMinisterioEnsaio(escalaMinisterio.getId(), naoConfirmarEnsaioDTO);
        log.info("NaoConfirmadoEscalaMinisterio inserted with id {}", naoConfirmadoEscalaMinisterio.getIdEscalaMinisterio());
        if (escalaMinisterio.getMembrosMinisterioNaoConfirmadoIds() == null) {
            escalaMinisterio.setMembrosMinisterioNaoConfirmadoIds(new ArrayList<>());
        }

        escalaMinisterio.getMembrosMinisterioNaoConfirmadoIds()
                        .add(naoConfirmadoEscalaMinisterio.getId());
        this.escalaMinisterioRepository.save(escalaMinisterio);
        return true;
    }

    @Override
    public List<MinisterioResponse> listMinisterioThatMembroParticipateByMembroId(
            String idMembro) {
        log.info("List ministerios that membro " + idMembro + " participate");
        return this.membroMinisterioRepository.findMinisterioByMembroId(idMembro);
    }

    @Override
    public List<String> listMinisterioV2ThatMembroParticipateByMembroId(
            String idMembro) {
        log.info("List ministerios v2 that membro " + idMembro + " participate");
        return this.membroMinisterioRepository.findMinisterioV2ByMembroId(idMembro);
    }

    @Override
    public StatusMembroMinisterio listStatusMembroMinisterioByMinisterioIdAndMembroId(
            String idMinisterio, String membroId) {
        Optional<Ministerio> optional = this.ministerioRepository.findById(idMinisterio);
        if (optional.isEmpty()) {
            throw new RuntimeException("Ministerio " + idMinisterio + " not found");
        }
        Ministerio ministerio = optional.get();
        if (ministerio.getCoordenadorId().equals(membroId)) {
            return StatusMembroMinisterio.COORDENADOR;
        } else if (ministerio.getViceCoordenadorId()
                             .equals(membroId)) {
            return StatusMembroMinisterio.VICE_COORDENADOR;
        } else {
            return StatusMembroMinisterio.MEMBRO;
        }
    }

    @Override
    public List<RepertorioResponseWithFullInfos> listRepertorioAsMembroMinisterioByMinisterioId(
            String idMinisterio) {
        log.info("Listing repertorios of ministerio {} as membro ministerio...", idMinisterio);
        return this.repertorioRepository.findAllByMinisterioIdWithDanceAndMusicInfos(idMinisterio);
    }

    @Override
    public List<MusicaMinisterioResponse> listMusicaMinisterioAsMembroMinisterioByMinisterioId(
            String idMinisterio) {
        log.info("Listing musicas ministerios of ministerio {} as membro ministerio...", idMinisterio);
        return MinisterioMapper.toListResponseMusicaMinisterio(this.musicaMinisterioRepository.findAllByMinisterioId(idMinisterio));
    }

    @Override
    public List<DancaMinisterioResponse> listDancaMinisterioAsMembroMinisterioByMinisterioId(
            String idMinisterio) {
        log.info("Listing dancas ministerios of ministerio {} as membro ministerio...", idMinisterio);
        return MinisterioMapper.toListResponseDancaMinisterio(this.dancaMinisterioRepository.findAllByMinisterioId(idMinisterio));
    }
}
