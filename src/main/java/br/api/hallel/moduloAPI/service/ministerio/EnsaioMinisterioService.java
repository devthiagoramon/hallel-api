package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.EnsaioMinisterioDTO;
import br.api.hallel.moduloAPI.dto.v1.ministerio.EnsaioMinisterioResponse;
import br.api.hallel.moduloAPI.dto.v1.ministerio.SimpleEscalaMinisterioResponse;
import br.api.hallel.moduloAPI.dto.v1.ministerio.StatusMembroMinisterioInEscala;
import br.api.hallel.moduloAPI.exceptions.ministerio.*;
import br.api.hallel.moduloAPI.mapper.ministerio.MinisterioMapper;
import br.api.hallel.moduloAPI.model.EnsaioMinisterio;
import br.api.hallel.moduloAPI.model.EscalaMinisterio;
import br.api.hallel.moduloAPI.model.Ministerio;
import br.api.hallel.moduloAPI.repository.EnsaioMinisterioRepository;
import br.api.hallel.moduloAPI.repository.EscalaMinisterioRepository;
import br.api.hallel.moduloAPI.repository.MinisterioRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class EnsaioMinisterioService
        implements EnsaioMinisterioInterface {

    @Autowired
    private EnsaioMinisterioRepository repository;

    @Autowired
    private EscalaMinisterioRepository escalaRepository;

    @Autowired
    private MinisterioRepository ministerioRepository;

    @Autowired
    private NaoConfirmadoEscalaMinisterioService naoConfirmadoEscalaMinisterioService;

    @Override
    public EnsaioMinisterioResponse createEnsaioMinisterio(
            EnsaioMinisterioDTO dto) {

        log.info("Creating ensaio for ministerio {}...", dto.getIdMinisterio());
        EnsaioMinisterio ensaioMinisterio = new EnsaioMinisterio();

        ensaioMinisterio.setTitulo(dto.getTitulo());
        ensaioMinisterio.setDescricao(dto.getDescricao());
        ensaioMinisterio.setIdEscalaMinisterioAssociated(dto.getIdEscalaMinisterioAssociated());
        ensaioMinisterio.setIdMinisterio(dto.getIdMinisterio());
        ensaioMinisterio.setDate(Date.from(dto.getDate()
                                              .atZone(ZoneId.systemDefault())
                                              .toInstant()));

        EnsaioMinisterio ensaioMinisterioCreated = repository.save(ensaioMinisterio);

        Optional<Ministerio> optionalMinisterio = ministerioRepository.findById(dto.getIdMinisterio());
        //noinspection ExtractMethodRecommender
        if (optionalMinisterio.isEmpty()) {
            throw new EnsaioCreateEditException("Can't find ministério by id");
        }

        Ministerio ministerio = optionalMinisterio.get();
        EscalaMinisterio escalaMinisterio = new EscalaMinisterio();
        escalaMinisterio.setMinisterioId(ensaioMinisterioCreated.getIdMinisterio());
        escalaMinisterio.setDate(ensaioMinisterioCreated.getDate());
        escalaMinisterio.setMembrosMinisterioConvidadosIds(
                List.of(ministerio.getCoordenadorId(),
                        ministerio.getViceCoordenadorId()));
        escalaMinisterio.setEnsaioMinisterioId(ensaioMinisterioCreated.getId());
        escalaMinisterio.setEnsaio(true);
        escalaMinisterio.setMembrosMinisterioConfimadoIds(new ArrayList<>());
        escalaMinisterio.setMembrosMinisterioNaoConfirmadoIds(new ArrayList<>());
        escalaRepository.save(escalaMinisterio);

        return MinisterioMapper.toEnsaioMinisterioResponse(ensaioMinisterioCreated);
    }

    @Override
    public EnsaioMinisterioResponse editEnsaioMinisterio(
            String idEnsaio, EnsaioMinisterioDTO dto) {
        log.info("Editing ensaio ministerio {}...", idEnsaio);
        Optional<EnsaioMinisterio> optional = repository.findById(idEnsaio);

        if (optional.isEmpty()) {
            throw new EnsaioCreateEditException("Can't find ensaio ministério by id");
        }

        EnsaioMinisterio ensaioMinisterio = optional.get();
        ensaioMinisterio.setTitulo(dto.getTitulo());
        ensaioMinisterio.setDescricao(dto.getDescricao());
        ensaioMinisterio.setIdEscalaMinisterioAssociated(dto.getIdEscalaMinisterioAssociated());

        Date dateDTO = Date.from(dto.getDate()
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant());
        if (!ensaioMinisterio.getDate().equals(dateDTO)) {
            ensaioMinisterio.setDate(dateDTO);
            Optional<EscalaMinisterio> optionalEscalaMinisterio = escalaRepository.findByEnsaioMinisterioId(idEnsaio);
            if (optionalEscalaMinisterio.isEmpty()) {
                throw new EnsaioCreateEditException("Can't find escala by id");
            }
            EscalaMinisterio escalaMinisterio = optionalEscalaMinisterio.get();
            escalaMinisterio.setDate(dateDTO);
            escalaRepository.save(escalaMinisterio);
        }
        EnsaioMinisterio editedEnsaio = repository.save(ensaioMinisterio);
        return MinisterioMapper.toEnsaioMinisterioResponse(editedEnsaio);
    }

    @Override
    public List<EnsaioMinisterioResponse> listEnsaioMinisterioByMinisterioId(
            String idMinisterio) {
        log.info("Listing ensaios ministerio of ministerio {}...", idMinisterio);
        return MinisterioMapper
                .toListEnsaioMinisterioResponse(this
                        .repository
                        .findAllByIdMinisterio(idMinisterio));
    }

    @Override
    public EnsaioMinisterioResponse listEnsaioMinisterioById(
            String idEnsaioMinisterio) {
        log.info("Listing ensaio ministério by id {}", idEnsaioMinisterio);
        Optional<EnsaioMinisterio> optional = this.repository.findById(idEnsaioMinisterio);
        if (optional.isEmpty()) {
            throw new EnsaioListException("Can't find ensaio by id %s".formatted(idEnsaioMinisterio));
        }
        return MinisterioMapper.toEnsaioMinisterioResponse(optional.get());
    }

    @Override
    public List<SimpleEscalaMinisterioResponse> listEscalasThatCanAssociateIntoEnsaioMinisterio(
            String idMinisterio,
            LocalDateTime from) {
        log.info("Listing escalas that can associate into ensaio of ministerio {}...", idMinisterio);

        return this.escalaRepository.findAllEscalasMinisterioCanAddIntoEnsaioOfMinisterioFromDate(idMinisterio, from);
    }

    @Override
    public EnsaioMinisterioResponse associateEscalaIntoEnsaioMinisterio(
            String idEscala, String idEnsaio) {
        log.info("Associating escala {} into ensaio {}...", idEscala, idEnsaio);
        Optional<EnsaioMinisterio> optional = repository.findById(idEnsaio);
        if (optional.isEmpty()) {
            throw new AssociateEscalaInEnsaioExcpetion("Can't find escala by id");
        }

        EnsaioMinisterio ensaioMinisterio = optional.get();
        ensaioMinisterio.setIdEscalaMinisterioAssociated(idEscala);
        EnsaioMinisterio updatedEnsaio = repository.save(ensaioMinisterio);
        return MinisterioMapper.toEnsaioMinisterioResponse(updatedEnsaio);
    }

    @Override
    public StatusMembroMinisterioInEscala getStatusMembroInEnsaioMinisterio(
            String idEnsaio, String idMembro) {
        log.info("Getting status membro {} in ensaio {}...", idMembro, idEnsaio);
        Optional<EscalaMinisterio> optionalEscala = this.escalaRepository.findByEnsaioMinisterioId(idEnsaio);
        if (optionalEscala.isEmpty()) {
            throw new StatusMembroInEnsaioException("Can't find escala by ensaio id %s".formatted(idEnsaio));
        }
        EscalaMinisterio escalaMinisterio = optionalEscala.get();
        if (escalaMinisterio.getMembrosMinisterioConvidadosIds()
                            .contains(idMembro)) {
            return StatusMembroMinisterioInEscala.CONVIDADO;
        }
        if (escalaMinisterio.getMembrosMinisterioConfimadoIds() != null && escalaMinisterio.getMembrosMinisterioConfimadoIds()
                                                                                           .contains(idMembro)) {
            return StatusMembroMinisterioInEscala.CONFIRMADO;
        }
        if (naoConfirmadoEscalaMinisterioService.verifyIfMembroMinisterioIsAlreadyNotConfirmed(escalaMinisterio.getId(), idMembro)) {
            return StatusMembroMinisterioInEscala.NAO_CONFIRMADO;
        }
        throw new StatusMembroInEnsaioException("Can't find membro in ensaio id %s".formatted(idEnsaio));
    }

    @Override
    public void deleteEnsaioMinisterio(String idEnsaio) {
        log.info("Deleting ensaio ministerio {}...", idEnsaio);
        Optional<EnsaioMinisterio> optional = repository.findById(idEnsaio);

        if (optional.isEmpty()) {
            throw new EnsaioDeleteException("Can't find ensaio ministério by id");
        }

        EnsaioMinisterio ensaioMinisterio = optional.get();
        Optional<EscalaMinisterio> optionalEscalaMinisterio = escalaRepository.findByEnsaioMinisterioId(idEnsaio);
        if (optionalEscalaMinisterio.isEmpty()) {
            throw new EnsaioDeleteException("Can't find escala by id");
        }
        EscalaMinisterio escalaMinisterio = optionalEscalaMinisterio.get();
        escalaRepository.delete(escalaMinisterio);
        repository.delete(ensaioMinisterio);

    }
}
