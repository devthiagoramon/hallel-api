package br.api.hallel.moduloAPI.service.ministerio;

import br.api.hallel.moduloAPI.dto.v1.ministerio.*;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MinisterioDTOV2;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MinisterioResponseV2;
import br.api.hallel.moduloAPI.mapper.ministerio.*;
import br.api.hallel.moduloAPI.model.*;
import br.api.hallel.moduloAPI.repository.*;
;
import br.api.hallel.moduloAPI.service.google.GoogleBucketService;
import br.api.hallel.moduloAPI.utils.GoogleBucketUtils;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class MinisterioService implements MinisterioInterface {

    @Autowired
    MinisterioRepository ministerioRepository;
    @Autowired
    MembroMinisterioService membroMinisterioService;
    @Autowired
    EventosRepository eventosRepository;
    @Autowired
    EscalaMinisterioRepository escalaMinisterioRepository;
    @Autowired
    private GoogleBucketService bucketService;


    @NotNull
    private Ministerio getMinisterioById(String idMinisterio) throws
            RuntimeException {
        Optional<Ministerio> optional = ministerioRepository.findById(idMinisterio);
        if (optional.isEmpty()) {
            log.info("Error: getting ministerio " + idMinisterio + "!");
            throw new RuntimeException("Can't find ministerio by this id!");
        }
        return optional.get();
    }

    @Override
    public MinisterioResponse createMinisterio(
            MinisterioDTO coordMinisterioDTO,
            MultipartFile fileImage) {
        log.info("Creating ministerio...");


        Ministerio ministerio = this.ministerioRepository.insert(MinisterioMapper.toModelMinisterio(coordMinisterioDTO));

        try {
            String fileImageUrl = bucketService.sendImageToBucket(fileImage, GoogleBucketUtils.getImageName(ministerio.getId(), Ministerio.class.getSimpleName(), Objects.requireNonNull(fileImage.getContentType())));
            ministerio.setFileImageUrl(fileImageUrl);
            ministerio = this.ministerioRepository.save(ministerio);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (coordMinisterioDTO.getCoordenadorId() != null) {
            membroMinisterioService.addMembroMinisterio(new AddMembroMinisterioDTO(coordMinisterioDTO.getCoordenadorId(), ministerio.getId(), null));
        }
        if (coordMinisterioDTO.getViceCoordenadorId() != null) {
            membroMinisterioService.addMembroMinisterio(new AddMembroMinisterioDTO(coordMinisterioDTO.getViceCoordenadorId(), ministerio.getId(), null));
        }
        log.info("Ministerio id " + ministerio.getId() + " created!");
        return MinisterioMapper.toMinisterioResponse(ministerio);
    }

    @Override
    public MinisterioResponseV2 createMinisterioV2(
            MinisterioDTOV2 ministerioDTOV2,
            MultipartFile fileImage) {
        log.info("Creating ministerio v2...");

        Ministerio ministerio = this.ministerioRepository.insert(MinisterioMapper.toModelMinisterioV2(ministerioDTOV2));

        try {
            String fileImageUrl = bucketService.sendImageToBucket(fileImage,
                    GoogleBucketUtils
                            .getImageName(ministerio.getId(),
                                    Ministerio.class.getSimpleName()));
            ministerio.setFileImageUrl(fileImageUrl);
            ministerio = this.ministerioRepository.save(ministerio);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (ministerioDTOV2.getCoordenadorId() != null) {
            membroMinisterioService.addMembroMinisterio(new AddMembroMinisterioDTO(ministerioDTOV2.getCoordenadorId(), ministerio.getId(), null));
        }
        if (ministerioDTOV2.getViceCoordenadorId() != null) {
            membroMinisterioService.addMembroMinisterio(new AddMembroMinisterioDTO(ministerioDTOV2.getViceCoordenadorId(), ministerio.getId(), null));
        }
        log.info("Ministerio id " + ministerio.getId() + " created!");
        return MinisterioMapper.toMinisterioResponseV2(ministerio);
    }

    @Override
    public MinisterioResponse editMinisterio(String idMinisterio,
                                             MinisterioDTO editCoordMinisterioDTO) {
        log.info("Editing ministerio...");
        Ministerio ministerio = getMinisterioById(idMinisterio);
        ministerio.setNome(editCoordMinisterioDTO.getNome());
        ministerio.setDescricao(editCoordMinisterioDTO.getDescricao());
        ministerio.setFileImageUrl(editCoordMinisterioDTO.getFileImageUrl());
        ministerio.setObjetivos(editCoordMinisterioDTO.getObjetivos());
        Ministerio ministerioCoordsEdited = editCoordsMembroOfMembroMinisterio(ministerio, editCoordMinisterioDTO);

        Ministerio ministerioUpdated = this.ministerioRepository.save(ministerioCoordsEdited);
        log.info("Ministerio id " + ministerio.getId() + " edited!");
        return MinisterioMapper.toMinisterioResponse(ministerioUpdated);
    }

    @Override
    public MinisterioResponseV2 editMinisterioV2(String idMinisterio,
                                                 MinisterioDTOV2 ministerioDTOV2,
                                                 MultipartFile fileImage) {
        log.info("Editing ministerio v2...");
        Ministerio ministerio = getMinisterioById(idMinisterio);
        ministerio.setNome(ministerioDTOV2.getNome());
        ministerio.setDescricao(ministerioDTOV2.getDescricao());
        ministerio.setFileImageUrl(ministerioDTOV2.getFileImageUrl());
        ministerio.setObjetivos(ministerioDTOV2.getObjetivos());
        ministerio.setHasRepertorio(ministerioDTOV2.isHasRepertorio());
        ministerio.setHasDance(ministerioDTOV2.isHasDance());
        ministerio.setHasMusic(ministerioDTOV2.isHasMusic());

        if (fileImage != null && !fileImage.isEmpty()) {
            try {
                String fileImageUrl = bucketService.updateImageOfBucket(fileImage, GoogleBucketUtils
                        .getImageName(ministerio.getId(),
                                Ministerio.class.getSimpleName()));
                ministerio.setFileImageUrl(fileImageUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Ministerio ministerioCoordsEdited = editCoordsMembroOfMembroMinisterio(ministerio, ministerioDTOV2);
        Ministerio ministerioUpdated = this.ministerioRepository.save(ministerioCoordsEdited);
        log.info("Ministerio id " + ministerio.getId() + " edited!");
        return MinisterioMapper.toMinisterioResponseV2(ministerioUpdated);
    }

    @Override
    public MinisterioResponseV2 editImageMinisterio(
            String idMinisterio, MultipartFile fileImage) {
        Optional<Ministerio> ministerioOptional = ministerioRepository.findById(idMinisterio);
        if (ministerioOptional.isEmpty()) {
            throw new RuntimeException("Evento não encontrado!");
        }

        Ministerio ministerio = ministerioOptional.get();

        try {
            String imageFileUrl = bucketService.updateImageOfBucket(fileImage, ministerio.getNome() + "_image");
            ministerio.setFileImageUrl(imageFileUrl);

            log.info("Imagem do ministério editado com sucesso!");

            Ministerio ministerioResponse = ministerioRepository.save(ministerio);
            return MinisterioMapper.toMinisterioResponseV2(ministerioResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Ministerio editCoordsMembroOfMembroMinisterio(
            Ministerio ministerio,
            MinisterioDTOV2 editCoordMinisterioDTO) {

        // Verificação se o coordenador e vicecoordenador são membros ministerio, caso não forem adicionar eles no ministerio
        List<MembroMinisterio> listByCoordId = membroMinisterioService.membroMinisterioRepository.findByMembroId(ministerio.getCoordenadorId());
        List<MembroMinisterio> listByViceCoordId = membroMinisterioService.membroMinisterioRepository.findByMembroId(ministerio.getViceCoordenadorId());

        MembroMinisterio membroMinisterioCoord = null;
        MembroMinisterio membroMinisterioVice = null;

        for (MembroMinisterio coord : listByCoordId) {
            if (coord.getMinisterioId().equals(ministerio.getId())) {
                membroMinisterioCoord = coord;
            }
        }
        for (MembroMinisterio viceCoord : listByViceCoordId) {
            if (viceCoord.getMinisterioId()
                         .equals(ministerio.getId())) {
                membroMinisterioVice = viceCoord;
            }
        }

        // Se houve alteração de coordenador, alterar o id do ministerio e remover o antigo do ministerio
        if (editCoordMinisterioDTO.getCoordenadorId() != null && !(editCoordMinisterioDTO.getCoordenadorId()
                                                                                         .equals(ministerio.getCoordenadorId()))) {
            ministerio.setCoordenadorId(editCoordMinisterioDTO.getCoordenadorId());
            membroMinisterioService.removerMembroMinisterio(membroMinisterioCoord);
            if (!membroMinisterioService.verifyIfMembroIsOnMinisterioAlready(ministerio.getId(), editCoordMinisterioDTO.getCoordenadorId())) {
                membroMinisterioService.addMembroMinisterio(new AddMembroMinisterioDTO(editCoordMinisterioDTO.getCoordenadorId(), ministerio.getId(), null));
            }
        }
        // Se houve alteração de vice, alterar o id do ministerio e remover o antigo do ministerio
        if (editCoordMinisterioDTO.getViceCoordenadorId() != null && !(editCoordMinisterioDTO.getViceCoordenadorId()
                                                                                             .equals(ministerio.getViceCoordenadorId()))) {
            ministerio.setViceCoordenadorId(editCoordMinisterioDTO.getViceCoordenadorId());
            assert membroMinisterioVice != null;
            membroMinisterioService.removerMembroMinisterio(membroMinisterioVice);
            if (!membroMinisterioService.verifyIfMembroIsOnMinisterioAlready(ministerio.getId(), editCoordMinisterioDTO.getViceCoordenadorId())) {
                membroMinisterioService.addMembroMinisterio(new AddMembroMinisterioDTO(editCoordMinisterioDTO.getViceCoordenadorId(), ministerio.getId(), null));
            }
        }

        return ministerio;
    }

    private Ministerio editCoordsMembroOfMembroMinisterio(
            Ministerio ministerio,
            MinisterioDTO editCoordMinisterioDTO) {

        // Verificação se o coordenador e vicecoordenador são membros ministerio, caso não forem adicionar eles no ministerio
        List<MembroMinisterio> listByCoordId = membroMinisterioService.membroMinisterioRepository.findByMembroId(ministerio.getCoordenadorId());
        List<MembroMinisterio> listByViceCoordId = membroMinisterioService.membroMinisterioRepository.findByMembroId(ministerio.getViceCoordenadorId());

        MembroMinisterio membroMinisterioCoord = null;
        MembroMinisterio membroMinisterioVice = null;

        for (MembroMinisterio coord : listByCoordId) {
            if (coord.getMinisterioId().equals(ministerio.getId())) {
                membroMinisterioCoord = coord;
            }
        }
        for (MembroMinisterio viceCoord : listByViceCoordId) {
            if (viceCoord.getMinisterioId()
                         .equals(ministerio.getId())) {
                membroMinisterioVice = viceCoord;
            }
        }

        // Se houve alteração de coordenador, alterar o id do ministerio e remover o antigo do ministerio
        if (editCoordMinisterioDTO.getCoordenadorId() != null && !editCoordMinisterioDTO.getCoordenadorId()
                                                                                        .equals(ministerio.getCoordenadorId())) {
            ministerio.setCoordenadorId(editCoordMinisterioDTO.getCoordenadorId());
            membroMinisterioService.removerMembroMinisterio(membroMinisterioCoord);
            if (!membroMinisterioService.verifyIfMembroIsOnMinisterioAlready(ministerio.getId(), editCoordMinisterioDTO.getCoordenadorId())) {
                membroMinisterioService.addMembroMinisterio(new AddMembroMinisterioDTO(editCoordMinisterioDTO.getCoordenadorId(), ministerio.getId(), null));
            }
        }
        // Se houve alteração de vice, alterar o id do ministerio e remover o antigo do ministerio
        if (editCoordMinisterioDTO.getViceCoordenadorId() != null && !editCoordMinisterioDTO.getViceCoordenadorId()
                                                                                            .equals(ministerio.getViceCoordenadorId())) {
            ministerio.setViceCoordenadorId(editCoordMinisterioDTO.getViceCoordenadorId());
            assert membroMinisterioVice != null;
            membroMinisterioService.removerMembroMinisterio(membroMinisterioVice);
            if (!membroMinisterioService.verifyIfMembroIsOnMinisterioAlready(ministerio.getId(), editCoordMinisterioDTO.getCoordenadorId())) {
                membroMinisterioService.addMembroMinisterio(new AddMembroMinisterioDTO(editCoordMinisterioDTO.getViceCoordenadorId(), ministerio.getId(), null));
            }
        }

        return ministerio;
    }

    private Ministerio editCoordsMembroOfMembroMinisterio(
            Ministerio ministerio,
            EditCoordMinisterioDTO editCoordMinisterioDTO) {

        // Verificação se o coordenador e vicecoordenador são membros ministerio, caso não forem adicionar eles no ministerio
        List<MembroMinisterio> listByCoordId = membroMinisterioService.membroMinisterioRepository.findByMembroId(ministerio.getCoordenadorId());
        List<MembroMinisterio> listByViceCoordId = membroMinisterioService.membroMinisterioRepository.findByMembroId(ministerio.getViceCoordenadorId());

        MembroMinisterio membroMinisterioCoord = null;
        MembroMinisterio membroMinisterioVice = null;

        for (MembroMinisterio coord : listByCoordId) {
            if (coord.getMinisterioId().equals(ministerio.getId())) {
                membroMinisterioCoord = coord;
            }
        }
        for (MembroMinisterio viceCoord : listByViceCoordId) {
            if (viceCoord.getMinisterioId()
                         .equals(ministerio.getId())) {
                membroMinisterioVice = viceCoord;
            }
        }

        // Se houve alteração de coordenador, alterar o id do ministerio e remover o antigo do ministerio
        if (editCoordMinisterioDTO.getCoordenadorId() != null && !editCoordMinisterioDTO.getCoordenadorId()
                                                                                        .equals(ministerio.getCoordenadorId())) {
            ministerio.setCoordenadorId(editCoordMinisterioDTO.getCoordenadorId());
            membroMinisterioService.removerMembroMinisterio(membroMinisterioCoord);
            if (!membroMinisterioService.verifyIfMembroIsOnMinisterioAlready(ministerio.getId(), editCoordMinisterioDTO.getCoordenadorId())) {
                membroMinisterioService.addMembroMinisterio(new AddMembroMinisterioDTO(editCoordMinisterioDTO.getCoordenadorId(), ministerio.getId(), null));
            }
        }
        // Se houve alteração de vice, alterar o id do ministerio e remover o antigo do ministerio
        if (editCoordMinisterioDTO.getViceCoordenadorId() != null && !editCoordMinisterioDTO.getViceCoordenadorId()
                                                                                            .equals(ministerio.getViceCoordenadorId())) {
            ministerio.setViceCoordenadorId(editCoordMinisterioDTO.getViceCoordenadorId());
            membroMinisterioService.removerMembroMinisterio(membroMinisterioVice);
            if (!membroMinisterioService.verifyIfMembroIsOnMinisterioAlready(ministerio.getId(), editCoordMinisterioDTO.getCoordenadorId())) {
                membroMinisterioService.addMembroMinisterio(new AddMembroMinisterioDTO(editCoordMinisterioDTO.getViceCoordenadorId(), ministerio.getId(), null));
            }
        }

        return ministerio;
    }

    @Override
    public void deleteMinisterio(String idMinisterio) {
        log.info("Deleting ministerio...");
        Ministerio ministerio = getMinisterioById(idMinisterio);
        String fileName = ministerio.getFileImageUrl()
                                    .substring(ministerio.getFileImageUrl()
                                                         .lastIndexOf("/") + 1, ministerio.getFileImageUrl()
                                                                                          .indexOf("?"));
        try {
            if (bucketService.deleteImageOfBucket(fileName)) {
                log.info("Imagem '{}' foi removido do Bucket com sucesso!", fileName);
            } else {
                log.warn("Imagem '{}' não foi encontrada no bucket!", fileName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.ministerioRepository.delete(ministerio);
        log.info("Ministerio " + ministerio.getId() + "_" + ministerio.getNome() + " deleted!");
    }

    @Override
    public List<MinisterioResponse> listMinisterios() {
        log.info("Listing ministerios...");
        return MinisterioMapper.toMinisterioResponseList(this.ministerioRepository.findAll());
    }

    @Override
    public List<MinisterioPublicResponse> listMinisterioPublic(
            int page, int size) {
        System.out.println("page:" + page);
        Pageable pageable = PageRequest.of(page, size);
        Page<Ministerio> ministeriosPaginados = this.ministerioRepository.findAll(pageable);

        List<MinisterioPublicResponse> listaResponse = ministeriosPaginados.getContent()
                                                                           .stream()
                                                                           .map(MinisterioPublicResponse::toListMinisterioResponse)
                                                                           .collect(Collectors.toList());

        System.out.println("Dados do banco: " + listaResponse);

        return listaResponse;
    }

    @Override
    public MinisterioWithCoordsResponse listMinisterioWithCoordById(
            String idMinisterio) {
        return this.ministerioRepository.findAllWithCoordsById(idMinisterio);
    }

    @Override
    public Slice<MinisterioWithCoordsResponse> listMinisteriosWithCoords(Pageable page) {
        return this.ministerioRepository.findAllWithCoords(page);
    }

    @Override
    public List<MinisterioWithCoordsResponse> listMInisteriosWithCoordsByName(
            String name) {
        return this.ministerioRepository.findAllWithCoordsByName(name);
    }

    @Override
    public MinisterioResponse listMinisterioById(
            String idMinisterio) {
        log.info("Listing ministerio " + idMinisterio + "...");
        return MinisterioMapper.toMinisterioResponse(getMinisterioById(idMinisterio));
    }

    @Override
    public MinisterioResponse alterarCoordenadoresInMinisterio(
            String idMinisterio,
            EditCoordMinisterioDTO editCoordMinisterioDTO) {
        log.info("Changing coord and vice-coord in ministerio " + idMinisterio + "...");
        Ministerio ministerio = getMinisterioById(idMinisterio);
        Ministerio ministerioCoordEdited = editCoordsMembroOfMembroMinisterio(ministerio, editCoordMinisterioDTO);
        Ministerio ministerioUpdated = this.ministerioRepository.save(ministerioCoordEdited);
        log.info("Coord and vice-coord changed in ministerio " + ministerioUpdated.getNome());
        return MinisterioMapper.toMinisterioResponse(ministerioUpdated);
    }

    @Override
    public Boolean validateCoordenadorInMinisterio(
            String idMinisterio, String idUser) {
        log.info("Validating coordenator of ministerios " + idMinisterio + " user_id: " + idUser);
        Ministerio ministerio = getMinisterioById(idMinisterio);
        return ministerio.getCoordenadorId()
                         .equals(idUser) || ministerio.getViceCoordenadorId()
                                                      .equals(idUser);
    }

    @Override
    public List<EventosShortResponse> listEventosThatMinisterioIsIn(
            String ministerioId) {
        log.info("List eventos that ministerio {} is participating! ", ministerioId);
        return this.eventosRepository.findAllByMinisteriosAssociadosContains(ministerioId);
    }

    @Override
    public MinisterioResponseV2 listMinisterioV2ById(
            String idMinisterio) {
        log.info("Listing ministerio {} V2...", idMinisterio);
        Optional<Ministerio> optional = this.ministerioRepository.findById(idMinisterio);
        if (optional.isEmpty()) {
            throw new RuntimeException("Can't find ministerio by this id");
        }
        return MinisterioMapper.toMinisterioResponseV2(optional.get());
    }

    private EscalaMinisterio getEscalaMinisterioById(
            String idEscalaMinisterio) {
        Optional<EscalaMinisterio> optional = escalaMinisterioRepository.findById(idEscalaMinisterio);
        if (optional.isEmpty()) {
            throw new RuntimeException("Can't get the ministerio by this id");
        }
        return optional.get();
    }


}
