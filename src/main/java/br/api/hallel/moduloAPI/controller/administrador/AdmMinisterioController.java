package br.api.hallel.moduloAPI.controller.administrador;

import br.api.hallel.moduloAPI.dto.v1.ministerio.*;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MinisterioDTOV2;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MinisterioResponseV2;
import br.api.hallel.moduloAPI.service.ministerio.EscalaService;
import br.api.hallel.moduloAPI.service.ministerio.MinisterioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/administrador/ministerio")
@Tag(name = "Administrador ministerio",
     description = "Endpoints para o administrador em relação a ministério")
public class AdmMinisterioController {

    @Autowired
    public MinisterioService ministerioService;

    @Autowired
    private EscalaService escalaService;

    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(summary = "Criar ministerio para a comunidade")
    public ResponseEntity<MinisterioResponse> createMinisterio(
            @RequestPart("request") MinisterioDTO ministerioDTO,
            @RequestPart("fileImage") MultipartFile fileImage
                                                              ) {
        return ResponseEntity.ok()
                             .body(ministerioService.createMinisterio(ministerioDTO, fileImage));
    }

    @PostMapping(value = "/v2/create",
                 consumes = {"multipart/form-data"})
    @Operation(
            summary = "Criar ministerio para a comunidade - atributos de repertorio")
    public ResponseEntity<MinisterioResponseV2> createMinisterioV2(
            @RequestPart("request") MinisterioDTOV2 ministerioDTOV2,
            @RequestPart("fileImage") MultipartFile fileImage) {
        return ResponseEntity.ok()
                             .body(ministerioService.createMinisterioV2(ministerioDTOV2, fileImage));
    }

    @GetMapping
    @Operation(summary = "Listar os ministerios da comunidade")
    public ResponseEntity<Slice<MinisterioWithCoordsResponse>> listMinisterio(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5")
            int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(ministerioService.listMinisteriosWithCoords(pageRequest));
    }

    @GetMapping("/{idMinisterio}")
    @Operation(summary = "Listar um ministerio pelo seu id")
    public ResponseEntity<MinisterioResponse> listMinisterioById(
            @PathVariable("idMinisterio") String idMinisterio) {
        return ResponseEntity.ok(ministerioService.listMinisterioById(idMinisterio));
    }

    @GetMapping("/v2/{idMinisterio}")
    @Operation(summary = "Listar um ministerio pelo seu id")
    public ResponseEntity<MinisterioResponseV2> listMinisterioV2ById(
            @PathVariable("idMinisterio") String idMinisterio) {
        return ResponseEntity.ok(ministerioService.listMinisterioV2ById(idMinisterio));
    }

    @GetMapping("/search")
    @Operation(
            summary = "Pesquisar um ministério a partir do nome dele",
            description = "O endpoint fará uma busca dos ministérios que possuem o nome passado como parâmetro")
    public ResponseEntity<List<MinisterioWithCoordsResponse>> searchMinisterioByName(
            @RequestParam(name = "name") String name) {
        return ResponseEntity.ok(ministerioService.listMInisteriosWithCoordsByName(name));
    }

    @PutMapping("/{idMinisterio}/edit")
    @Operation(summary = "Editar ministerio")
    public ResponseEntity<MinisterioResponse> updateMinisterio(
            @PathVariable("idMinisterio") String idMinisterio,
            @RequestBody MinisterioDTO ministerioDTO) {
        return ResponseEntity.ok()
                             .body(ministerioService.editMinisterio(idMinisterio, ministerioDTO));
    }

    @PutMapping(value = "/{idMinisterio}/v2/edit",
                consumes = {"multipart/form-data"})
    @Operation(
            summary = "Editar ministerio - Suportando os atributos de repertorio")
    public ResponseEntity<MinisterioResponseV2> updateMinisterioV2(
            @PathVariable("idMinisterio") String idMinisterio,
            @RequestPart("request") MinisterioDTOV2 ministerioDTOV2,
            @RequestPart("fileImage") MultipartFile fileImage) {
        return ResponseEntity.ok()
                             .body(ministerioService.editMinisterioV2(idMinisterio, ministerioDTOV2, fileImage));
    }

    @PatchMapping(value = "/{id}/edit/image",
                  consumes = {"multipart/form-data"})
    public ResponseEntity<MinisterioResponseV2> editMinisterioImage
            (@PathVariable(value = "id") String id,
             @RequestPart("fileImage") MultipartFile fileImage) {
        return ResponseEntity.ok()
                             .body(ministerioService.editImageMinisterio(id, fileImage));
    }

    @DeleteMapping("/{idMinisterio}")
    @Operation(summary = "Deletar ministerio")
    public ResponseEntity<?> deleteMinisterio(
            @PathVariable("idMinisterio") String idMinisterio) {
        ministerioService.deleteMinisterio(idMinisterio);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{idMinisterio}/edit/coordenadores")
    @Operation(summary = "Alterar os coordenadores do ministerio")
    public ResponseEntity<MinisterioResponse> alterarCoordenadoresDoMinisterios(
            @PathVariable("idMinisterio") String idMinisterio,
            @RequestBody
            EditCoordMinisterioDTO editCoordMinisterioDTO) {
        return ResponseEntity.ok()
                             .body(this.ministerioService.alterarCoordenadoresInMinisterio(idMinisterio, editCoordMinisterioDTO));
    }

    @GetMapping("/escala")
    @Operation(summary = "Listar as escalas dos ministerios")
    public ResponseEntity<List<EscalaMinisterioWithEventoInfoResponse>> listarEscalasDosMinisterios() {
        return ResponseEntity.ok(this.escalaService.listEscalaMinisterio());
    }

    @GetMapping("/escala/date")
    @Operation(
            summary = "Listar as escalas dos ministerios em um intervalo de tempo")
    public ResponseEntity<List<EscalaMinisterioWithEventoInfoResponse>> listarEscalasDosMinisteriosRangeDate(
            @RequestParam(name = "dateStart")
            LocalDateTime dateStart,
            @RequestParam(name = "dateEnd") LocalDateTime dateEnd) {
        return ResponseEntity.ok(this.escalaService.listEscalaMinisterioRangeDate(dateStart, dateEnd));
    }

}
