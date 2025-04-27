package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.DancaMinisterioDTO;
import br.api.hallel.moduloAPI.dto.v1.ministerio.DancaMinisterioDTOEdit;
import br.api.hallel.moduloAPI.dto.v1.ministerio.DancaMinisterioResponse;
import br.api.hallel.moduloAPI.mapper.ministerio.MinisterioMapper;
import br.api.hallel.moduloAPI.model.DancaMinisterio;
import br.api.hallel.moduloAPI.model.Repertorio;
import br.api.hallel.moduloAPI.repository.DancaMinisterioRepository;
import br.api.hallel.moduloAPI.repository.RepertorioRepository;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.internal.constraintvalidators.bv.time.futureorpresent.FutureOrPresentValidatorForReadableInstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class DancaMinisterioService
        implements DancaMinisterioInterface {

    @Autowired
    DancaMinisterioRepository dancaMinisterioRepository;

    @Autowired
    RepertorioRepository repertorioRepository;
    private FutureOrPresentValidatorForReadableInstant futureOrPresentValidatorForReadableInstant;

    @Override
    public DancaMinisterioResponse createDancaMinisterio(
            DancaMinisterioDTO dancaMinisterioDTO) {
        log.info("Creating dança ministerio...");
        DancaMinisterio dancaMinisterio = dancaMinisterioRepository.insert(MinisterioMapper.toModelDancaMinisterio(dancaMinisterioDTO));
        return MinisterioMapper.toResponseDancaMinisterioResponse(dancaMinisterio);
    }

    @Override
    public List<DancaMinisterioResponse> getAllDancaMinisterio(
            String ministerioId) {
        log.info("Listing all dança ministerios of ministerio " + ministerioId + "...");
        return MinisterioMapper.toListResponseDancaMinisterio(dancaMinisterioRepository.findAllByMinisterioId(ministerioId));
    }

    private DancaMinisterio getDancaMinisterio(String idDanca) {
        Optional<DancaMinisterio> optional = dancaMinisterioRepository.findById(idDanca);
        if (optional.isEmpty()) {
            throw new RuntimeException("Can't find danca ministerio by this id");
        }
        return optional.get();
    }

    @Override
    public DancaMinisterioResponse getDancaMinisterioById(
            String idDancaMinisterio) {
        log.info("Listing dança ministerio by id " + idDancaMinisterio + "...");
        return MinisterioMapper.toResponseDancaMinisterioResponse(getDancaMinisterio(idDancaMinisterio));
    }

    @Override
    public DancaMinisterioResponse updateDancaMinisterio(
            String idDancaMinisterio,
            DancaMinisterioDTOEdit dancaMinisterioDTOEdit) {
        log.info("Editing danca ministerio id" + idDancaMinisterio + "...");
        DancaMinisterio dancaMinisterioOld = getDancaMinisterio(idDancaMinisterio);
        dancaMinisterioOld.setDescricao(dancaMinisterioDTOEdit.getDescricao());
        dancaMinisterioOld.setLinkVideo(dancaMinisterioDTOEdit.getLinkVideo());
        dancaMinisterioOld.setNome(dancaMinisterioDTOEdit.getNome());
        DancaMinisterio dancaMinisterioEdited = this.dancaMinisterioRepository.save(dancaMinisterioOld);
        return MinisterioMapper.toResponseDancaMinisterioResponse(dancaMinisterioEdited);
    }

    @Override
    public void deleteDancaMinisterio(String idDancaMinisterio) {
        log.info("Deleting danca ministerio id" + idDancaMinisterio + "...");
        DancaMinisterio dancaMinisterio = getDancaMinisterio(idDancaMinisterio);
        List<Repertorio> repertorios = repertorioRepository.findByDancaMinisterioIdsContains(idDancaMinisterio);
        if (!repertorios.isEmpty()){
            for (Repertorio repertorio : repertorios){
                List<String> idsDancas = repertorio.getDancaMinisterioIds();
                idsDancas.remove(idDancaMinisterio);
                repertorio.setDancaMinisterioIds(idsDancas);
                repertorioRepository.save(repertorio);
            }
        }
        dancaMinisterioRepository.delete(dancaMinisterio);
    }
}
