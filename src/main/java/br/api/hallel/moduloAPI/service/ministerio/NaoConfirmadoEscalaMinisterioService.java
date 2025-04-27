package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.NaoConfirmarEnsaioDTO;
import br.api.hallel.moduloAPI.dto.v1.ministerio.NaoConfirmarEscalaDTO;
import br.api.hallel.moduloAPI.mapper.ministerio.MinisterioMapper;
import br.api.hallel.moduloAPI.model.NaoConfirmadoEscalaMinisterio;
import br.api.hallel.moduloAPI.repository.NaoConfirmadoEscalaMinisterioRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class NaoConfirmadoEscalaMinisterioService
        implements NaoConfirmadoEscalaMinisterioInterface {
    @Autowired
    NaoConfirmadoEscalaMinisterioRepository naoConfirmadoEscalaMinisterioRepository;

    @Override
    public NaoConfirmadoEscalaMinisterio createNaoConfirmadoEscalaMinisterio(
            NaoConfirmarEscalaDTO naoConfirmarEscalaDTO) {
        log.info("Creating nao confirmado escala... ");

        NaoConfirmadoEscalaMinisterio naoConfirmadoEscalaMinisterio =
                new NaoConfirmadoEscalaMinisterio(naoConfirmarEscalaDTO.getIdMembroMinisterio(), naoConfirmarEscalaDTO.getIdEscalaMinisterio(), naoConfirmarEscalaDTO.getMotivo());
        return naoConfirmadoEscalaMinisterioRepository
                .insert(naoConfirmadoEscalaMinisterio);
    }

    @Override
    public NaoConfirmadoEscalaMinisterio createNaoConfirmadoEscalaMinisterioEnsaio(
            String idEscala,
            NaoConfirmarEnsaioDTO naoConfirmarEnsaioDTO) {
        log.info("Creating nao confirmado escala... ");

        NaoConfirmadoEscalaMinisterio naoConfirmadoEscalaMinisterio =
                new NaoConfirmadoEscalaMinisterio(naoConfirmarEnsaioDTO.getIdMembro(), idEscala, naoConfirmarEnsaioDTO.getMotivo());
        return naoConfirmadoEscalaMinisterioRepository
                .insert(naoConfirmadoEscalaMinisterio);
    }

    @Override
    public NaoConfirmadoEscalaMinisterio editNaoConfirmadoEscalaMinisterio(
            String idNaoConfirmadoEscala,
            NaoConfirmarEscalaDTO naoConfirmarEscalaDTO) {
        NaoConfirmadoEscalaMinisterio naoConfirmadoEscalaMinisterio =
                listNaoConfirmadoEscalaMinisterioById(idNaoConfirmadoEscala);
        NaoConfirmadoEscalaMinisterio naoConfirmadoEscalaMinisterioNew = MinisterioMapper.toNaoConfirmadoEscalaMinisterio(naoConfirmarEscalaDTO);
        naoConfirmadoEscalaMinisterio.setMotivo(naoConfirmadoEscalaMinisterioNew.getMotivo());
        return this.naoConfirmadoEscalaMinisterioRepository.save(naoConfirmadoEscalaMinisterio);
    }

    @Override
    public List<NaoConfirmadoEscalaMinisterio> listNaoConfirmadoEscalaMinisterioByIdMembroMinisterio(
            String idMemmbroMinisterio) {
        log.info("Listing nao confirmado escala ministerio by membro ministerio id: " + idMemmbroMinisterio);
        return this.naoConfirmadoEscalaMinisterioRepository.findAllByIdMembroMinisterio(idMemmbroMinisterio);
    }

    @Override
    public NaoConfirmadoEscalaMinisterio listNaoConfirmadoEscalaMinisterioById(
            String idNaoConfirmadoEscalaMinisterio) {
        log.info("Listing nao confirmado escala ministerio by id " + idNaoConfirmadoEscalaMinisterio + "...");
        Optional<NaoConfirmadoEscalaMinisterio> optional = this.naoConfirmadoEscalaMinisterioRepository.findById(idNaoConfirmadoEscalaMinisterio);
        if (optional.isEmpty()) {
            throw new RuntimeException("Can't find nao confirmado escala ministerio by this id");
        }
        return optional.get();
    }

    @Override
    public void deleteNaoConfirmadoEscalaMinisterio(
            String idNaoConfirmadoEscalaMinisterio) {
        log.info("Deleting nao confirmado escala ministerio...");
        NaoConfirmadoEscalaMinisterio naoConfirmadoEscalaMinisterio =
                listNaoConfirmadoEscalaMinisterioById(idNaoConfirmadoEscalaMinisterio);
        this.naoConfirmadoEscalaMinisterioRepository.delete(naoConfirmadoEscalaMinisterio);
        log.info("Nao confirmado id {} deleted", naoConfirmadoEscalaMinisterio.getId());
    }

    @Override
    public Boolean verifyIfMembroMinisterioIsAlreadyNotConfirmed(
            String idEscala, String idMembroMinisterio) {
        Optional<NaoConfirmadoEscalaMinisterio> optional = this
                .naoConfirmadoEscalaMinisterioRepository
                .findByIdMembroMinisterioAndIdEscalaMinisterio(idMembroMinisterio, idEscala);

        return optional.isPresent();
    }
}
