package br.api.hallel.moduloAPI.controller.membro;

import br.api.hallel.moduloAPI.exceptions.ExceptionResponse;
import br.api.hallel.moduloAPI.exceptions.associado.AssociadoNotFoundException;
import br.api.hallel.moduloAPI.exceptions.main.InvalidFileTypeException;
import br.api.hallel.moduloAPI.model.Associado;
import br.api.hallel.moduloAPI.payload.requerimento.BuscarIdAssociadoReq;
import br.api.hallel.moduloAPI.payload.requerimento.EditarPerfilMembroReq;
import br.api.hallel.moduloAPI.payload.requerimento.VirarAssociadoRequest;
import br.api.hallel.moduloAPI.payload.resposta.AssociadoResponse;
import br.api.hallel.moduloAPI.payload.resposta.PerfilResponse;
import br.api.hallel.moduloAPI.service.financeiro.AssociadoService;
import br.api.hallel.moduloAPI.service.main.MembroService;
import br.api.hallel.moduloAPI.utils.ImageTypeValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/membros")
@CrossOrigin("*")
public class MembroController {

    @Autowired
    private MembroService service;

    @Autowired
    private AssociadoService associadoService;

    @GetMapping("/perfil/{id}")
    public ResponseEntity<PerfilResponse> visualizarPerfil(
            @PathVariable String id) throws IllegalAccessException {
        PerfilResponse perfil = this.service.visualizarPerfil(id);
        return ResponseEntity.status(200).body(perfil);
    }

    @GetMapping("/perfil/token/{token}")
    public ResponseEntity<PerfilResponse> visualizarPerfilPeloToken(
            @PathVariable String token) throws
            IllegalAccessException {
        PerfilResponse perfil = this.service.visualizarPerfilPeloToken(token);
        return ResponseEntity.status(200).body(perfil);
    }

    @PatchMapping("/perfil")
    public ResponseEntity<PerfilResponse> editarPerfilUsuario(
            @RequestBody
            EditarPerfilMembroReq dto) throws
            IllegalAccessException {
        return ResponseEntity.status(200)
                             .body(this.service.editarPerfilMembro(dto));
    }

    @Operation(summary = "Editar foto de perfil do membro",
               description = "Passe um arquivo de um tipo imagem para alterar a foto de perfil de um membro",
               responses = {@ApiResponse(
                       description = "Editado com sucesso",
                       content = @Content(schema = @Schema(
                               implementation = PerfilResponse.class)),
                       responseCode = "200"), @ApiResponse(
                       description = "Tipo de imagem invalido",
                       content = @Content(schema = @Schema(
                               implementation = ExceptionResponse.class)),
                       responseCode = "415"),})
    @PatchMapping(value = "/perfil/image/{membroId}",
                  consumes = {"multipart/form-data"})
    public ResponseEntity<PerfilResponse> editarImagePerfilUsuario(
            @PathVariable("membroId") String membroId,
            @RequestPart("file")
            MultipartFile imageFile) throws
            IllegalAccessException {
        if (!ImageTypeValidator.isTypeValid(imageFile.getContentType()))
            throw new InvalidFileTypeException("Tipo de imagem inválido, tente novamente!");
        return ResponseEntity.ok(this.service.editarImagePerfilMembro(membroId, imageFile));
    }

    @PostMapping("/virarAssociado")
    public ResponseEntity<Boolean> createAssociado(@RequestBody
                                                   VirarAssociadoRequest virarAssociadoRequest) {
        Boolean booleanResposta = null;

        try {
            booleanResposta = this.associadoService.criarAssociado(virarAssociadoRequest);

            if (booleanResposta) {
                return ResponseEntity.status(200).body(true);
            } else {
                return ResponseEntity.status(402).body(false);
            }
        } catch (AssociadoNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/buscarAssociadoEmail")
    public ResponseEntity<String> buscarAssociado(
            @RequestBody BuscarIdAssociadoReq buscarIdAssociadoReq) {

        System.out.println(buscarIdAssociadoReq.getEmail()); // Verifique se o email está sendo corretamente capturado
        return ResponseEntity.ok()
                             .body(this.associadoService.IdAssociadofindByEmail(buscarIdAssociadoReq.getEmail()));
    }

}