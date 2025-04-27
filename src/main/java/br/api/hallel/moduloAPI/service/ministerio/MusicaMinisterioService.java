package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.MusicaMinisterioDTO;
import br.api.hallel.moduloAPI.dto.v1.ministerio.MusicaMinisterioDTOEdit;
import br.api.hallel.moduloAPI.dto.v1.ministerio.MusicaMinisterioResponse;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MusicaMinisterioDTOV2;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MusicaMinisterioResponseV2;
import br.api.hallel.moduloAPI.mapper.ministerio.MinisterioMapper;
import br.api.hallel.moduloAPI.model.MusicaMinisterio;
import br.api.hallel.moduloAPI.model.Repertorio;
import br.api.hallel.moduloAPI.repository.MusicaMinisterioRepository;
import br.api.hallel.moduloAPI.repository.RepertorioRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class MusicaMinisterioService
        implements MusicaMinisterioInterface {

    @Autowired
    MusicaMinisterioRepository musicaMinisterioRepository;

    @Autowired
    RepertorioRepository repertorioRepository;

    @Override
    public MusicaMinisterioResponse createMusicaMinisterio(
            MusicaMinisterioDTO musicaMinisterioDTO) {
        log.info("Creating musica ministerio...");
        MusicaMinisterio modelToInsert = new MusicaMinisterio();
        modelToInsert.setMinisterioId(musicaMinisterioDTO.getMinisterioId());
        modelToInsert.setEscala(musicaMinisterioDTO.getEscala());
        modelToInsert.setTitulo(musicaMinisterioDTO.getTitulo());
        modelToInsert.setTom(musicaMinisterioDTO.getTom());
        modelToInsert.setDuracao(musicaMinisterioDTO.getDuracao());
        modelToInsert.setCompasso(musicaMinisterioDTO.getCompasso());
        modelToInsert.setChaveHarmonica(musicaMinisterioDTO.getChaveHarmonica());
        MusicaMinisterio musicaMinisterio = musicaMinisterioRepository.insert(modelToInsert);
        return MinisterioMapper.toResponseMusicaMinisterioResponse(musicaMinisterio);
    }

    @Override
    public List<MusicaMinisterioResponseV2> getAllMusicaMinisterios(
            String ministerioId) {
        log.info("Listing musicas ministerio of ministerio " + ministerioId + "...");
        return MinisterioMapper.toListResponseV2MusicaMinisterio(musicaMinisterioRepository.findAllByMinisterioId(ministerioId));
    }

    private MusicaMinisterio getMusicaMinisterio(
            String idMusicaMinisterio) {
        Optional<MusicaMinisterio> optional = musicaMinisterioRepository.findById(idMusicaMinisterio);
        if (optional.isEmpty()) {
            throw new RuntimeException("Can't find musica ministerio by this id");
        }
        return optional.get();
    }

    @Override
    public MusicaMinisterioResponse getMusicaMinisterioById(
            String idMusicaMinisterio) {
        log.info("Listing musica ministerio by id " + idMusicaMinisterio + "...");
        MusicaMinisterio musicaMinisterio = getMusicaMinisterio(idMusicaMinisterio);
        return MinisterioMapper.toResponseMusicaMinisterioResponse(musicaMinisterio);
    }


    @Override
    public MusicaMinisterioResponse updateMusicaMinisterio(
            String idMusicaMinisterio,
            MusicaMinisterioDTOEdit musicaMinisterioDTO) {
        log.info("Editing musica ministerio id" + idMusicaMinisterio + "...");
        MusicaMinisterio musicaMinisterioOld = getMusicaMinisterio(idMusicaMinisterio);
        musicaMinisterioOld.setDuracao(musicaMinisterioDTO.getDuracao());
        musicaMinisterioOld.setCompasso(musicaMinisterioDTO.getCompasso());
        musicaMinisterioOld.setEscala(musicaMinisterioDTO.getEscala());
        musicaMinisterioOld.setTom(musicaMinisterioDTO.getTom());
        musicaMinisterioOld.setTitulo(musicaMinisterioDTO.getTitulo());
        musicaMinisterioOld.setChaveHarmonica(musicaMinisterioDTO.getChaveHarmonica());
        MusicaMinisterio musicaMinisterioEdited = this.musicaMinisterioRepository.save(musicaMinisterioOld);

        return MinisterioMapper.toResponseMusicaMinisterioResponse(musicaMinisterioEdited);
    }

    @Override
    public void deleteMusicaMinisterio(String idMusicaMinisterio) {
        log.info("Deleting musica ministerio id" + idMusicaMinisterio + "...");
        MusicaMinisterio musicaMinisterio = getMusicaMinisterio(idMusicaMinisterio);
        List<Repertorio> repertorios = repertorioRepository.findByMusicasIdsContains(idMusicaMinisterio);
        if (!repertorios.isEmpty()) {
            for (Repertorio repertorio : repertorios) {
                List<String> idsMusicas = repertorio.getMusicasIds();
                idsMusicas.remove(idMusicaMinisterio);
                repertorio.setMusicasIds(idsMusicas);
                repertorioRepository.save(repertorio);
            }
        }

        musicaMinisterioRepository.delete(musicaMinisterio);
    }

    @Override
    public MusicaMinisterioResponseV2 createMusicaMinisterioV2(
            MusicaMinisterioDTOV2 dtoV2) {
        log.info("V2: Creating música ministério...");
        MusicaMinisterio musicaMinisterioToAdd = new MusicaMinisterio();
        musicaMinisterioToAdd.setChaveHarmonica(dtoV2.getChaveHarmonica());
        musicaMinisterioToAdd.setMinisterioId(dtoV2.getMinisterioId());
        musicaMinisterioToAdd.setEscala(dtoV2.getEscala());
        musicaMinisterioToAdd.setLetra(dtoV2.getLetra());
        musicaMinisterioToAdd.setDuracao(dtoV2.getDuracao());
        musicaMinisterioToAdd.setCompasso(dtoV2.getCompasso());
        musicaMinisterioToAdd.setTitulo(dtoV2.getTitulo());
        musicaMinisterioToAdd.setTom(dtoV2.getTom());
        MusicaMinisterio musicaMinisterioAdded = musicaMinisterioRepository.save(musicaMinisterioToAdd);
        return MinisterioMapper.toResponseMusicaMinisterioV2(musicaMinisterioAdded);
    }

    @Override
    public MusicaMinisterioResponseV2 updateMusicaMinisterioV2(
            String idMusicaMinisterio, MusicaMinisterioDTOV2 dtoV2) {
        log.info("V2: Editing música ministério {}...", idMusicaMinisterio);
        MusicaMinisterio musicaMinisterioOld = getMusicaMinisterio(idMusicaMinisterio);
        musicaMinisterioOld.setChaveHarmonica(dtoV2.getChaveHarmonica());
        musicaMinisterioOld.setEscala(dtoV2.getEscala());
        musicaMinisterioOld.setLetra(dtoV2.getLetra());
        musicaMinisterioOld.setDuracao(dtoV2.getDuracao());
        musicaMinisterioOld.setCompasso(dtoV2.getCompasso());
        musicaMinisterioOld.setTitulo(dtoV2.getTitulo());
        musicaMinisterioOld.setTom(dtoV2.getTom());
        MusicaMinisterio musicaMinisterioEdited = this.musicaMinisterioRepository.save(musicaMinisterioOld);
        return MinisterioMapper.toResponseMusicaMinisterioV2(musicaMinisterioEdited);
    }

    @Override
    public MusicaMinisterioResponseV2 getMusicaMinisterioWithLetra(
            String idMusicaMinisterio) {
        log.info("Getting música ministério {} with letra...", idMusicaMinisterio);
        return MinisterioMapper.toResponseMusicaMinisterioV2(getMusicaMinisterio(idMusicaMinisterio));
    }

    @Override
    public String getLetraOfMusicaMinisterio(
            String idMusicaMinisterio) {
        log.info("Getting letra from música ministerio {}", idMusicaMinisterio);
        return getMusicaMinisterio(idMusicaMinisterio).getLetra();
    }
}
