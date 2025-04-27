package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.*;
import br.api.hallel.moduloAPI.mapper.ministerio.MinisterioMapper;
import br.api.hallel.moduloAPI.model.EscalaMinisterio;
import br.api.hallel.moduloAPI.model.Repertorio;
import br.api.hallel.moduloAPI.repository.EscalaMinisterioRepository;
import br.api.hallel.moduloAPI.repository.RepertorioRepository;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class RepertorioService implements RepertorioInterface {

    @Autowired
    private RepertorioRepository repertorioRepository;

    @Autowired
    private EscalaMinisterioRepository escalaMinisterioRepository;

    @Override
    public RepertorioResponse createRepertorio(
            RepertorioDTO repertorioDTO) {
        log.info("Creating repertorio in ministerio...");
        Repertorio repertorioModel = new Repertorio();
        repertorioModel.setMinisterioId(repertorioDTO.getMinisterioId());
        repertorioModel.setNome(repertorioDTO.getNome());
        repertorioModel.setDescricao(repertorioDTO.getDescricao());
        repertorioModel.setDancaMinisterioIds(repertorioDTO.getDancaMinisterioIds());
        repertorioModel.setMusicasIds(repertorioDTO.getMusicasIds());
        Repertorio repertorio = repertorioRepository.insert(repertorioModel);
        log.info("Repertorio " + repertorio.getId() + " created");
        return MinisterioMapper.toResponseRepertorio(repertorio);
    }

    @Override
    public List<RepertorioResponse> listRepertoriosByMinisterioId(
            String ministerioId) {
        log.info("Listing repertorios ministerio of ministerio: " + ministerioId);
        return MinisterioMapper.toListResponseRepertorio(repertorioRepository.findAllByMinisterioId(ministerioId));
    }

    @Override
    public RepertorioResponse listRepertorioById(
            String idRepertorio) {
        log.info("List repertorio ministerio by id: " + idRepertorio);
        Repertorio repertorio = getRepertorioById(idRepertorio);
        return MinisterioMapper.toResponseRepertorio(repertorio);
    }


    @Override
    public RepertorioResponse editRepertorio(String idRepertorio,
                                             RepertorioDTOEdit repertorioDTOEdit) {
        log.info("Editing repertorio ministerio " + idRepertorio + "...");
        Repertorio oldRepertorio = getRepertorioById(idRepertorio);
        oldRepertorio.setNome(repertorioDTOEdit.getNome());
        oldRepertorio.setDescricao(repertorioDTOEdit.getDescricao());
        oldRepertorio.setDancaMinisterioIds(repertorioDTOEdit.getDancaMinisterioIds());
        oldRepertorio.setMusicasIds(repertorioDTOEdit.getMusicasIds());
        Repertorio repertorioEdited = this.repertorioRepository.save(oldRepertorio);
        log.info("Repertorio " + repertorioEdited.getId() + " edited");
        return MinisterioMapper.toResponseRepertorio(repertorioEdited);
    }

    @Override
    public void deleteRepertorio(String idRepertorio) {
        log.info("Deleting repertorio ministerio " + idRepertorio + "...");
        Repertorio repertorio = getRepertorioById(idRepertorio);
        repertorioRepository.delete(repertorio);
        List<EscalaMinisterio> escalasMinisterio = escalaMinisterioRepository.findByRepertorioIdsContaining(idRepertorio);
        if (!escalasMinisterio.isEmpty()) {
            for (EscalaMinisterio escalaMinisterio : escalasMinisterio) {
                List<String> idsEscalas = escalaMinisterio.getRepertorioIds();
                idsEscalas.remove(idRepertorio);
                escalaMinisterio.setRepertorioIds(idsEscalas);
                escalaMinisterioRepository.save(escalaMinisterio);
            }
        }
        log.info("Repertorio " + repertorio.getId() + " deleted");
    }

    @Override
    public RepertorioResponseWithInfos adicionarRemoverMusicasRepertorio(
            String idRepertorio,
            RepertorioMusicDTO repertorioMusicDTO) {
        log.info("Adicionar ou removendo musicas do repertorio " + idRepertorio + "...");
        Repertorio repertorio = getRepertorioById(idRepertorio);
        List<String> musicIdsList = new ArrayList<>();
        if (repertorio.getMusicasIds() != null) {
            musicIdsList = repertorio.getMusicasIds();
        }
        if (repertorioMusicDTO.getMusicIdsAdd() != null) {
            musicIdsList.addAll(repertorioMusicDTO.getMusicIdsAdd());
        }
        if (repertorioMusicDTO.getMusicIdsRemove() != null) {
            musicIdsList = musicIdsList.stream()
                                       .filter(music -> !(repertorioMusicDTO.getMusicIdsRemove()
                                                                            .contains(music)))
                                       .toList();
        }
        repertorio.setMusicasIds(musicIdsList);
        Repertorio repertorioUpdated = this.repertorioRepository.save(repertorio);
        return MinisterioMapper.toRepertorioWithInfos(repertorioUpdated);
    }

    @Override
    public RepertorioResponseWithInfos adicionarRemoverDancaRepertorio(
            String idRepertorio,
            RepertorioDancaDTO repertorioDancaDTO) {
        log.info("Adicionar ou removendo danças do repertorio " + idRepertorio + "...");
        Repertorio repertorio = getRepertorioById(idRepertorio);
        List<String> dancaIdsList = new ArrayList<>();
        if (repertorio.getDancaMinisterioIds() != null) {
            dancaIdsList = repertorio.getDancaMinisterioIds();
        }
        if (repertorioDancaDTO.getDanceIdsAdd() != null) {
            dancaIdsList.addAll(repertorioDancaDTO.getDanceIdsAdd());
        }
        if (repertorioDancaDTO.getDanceIdsRemove() != null) {
            dancaIdsList = dancaIdsList.stream()
                                       .filter(dance -> !(repertorioDancaDTO.getDanceIdsRemove()
                                                                            .contains(dance)))
                                       .toList();
        }
        repertorio.setDancaMinisterioIds(dancaIdsList);
        Repertorio repertorioUpdated = this.repertorioRepository.save(repertorio);
        return MinisterioMapper.toRepertorioWithInfos(repertorioUpdated);
    }

    @Override
    public RepertorioResponseWithFullInfos listRepertorioWithDancesAndMusic(
            String idRepertorio) {
        log.info("Listing repertorio with dances and music");
        return this.repertorioRepository.findByIdWithDanceAndMusicInfos(idRepertorio);
    }

    private @NotNull Repertorio getRepertorioById(
            String idRepertorio) {
        Optional<Repertorio> optional = repertorioRepository.findById(idRepertorio);
        if (optional.isEmpty()) {
            throw new RuntimeException("Can't find repertorio by this id");
        }
        return optional.get();
    }
}
