package br.api.hallel.moduloAPI.controller.administrador;

import br.api.hallel.moduloAPI.model.Administrador;
import br.api.hallel.moduloAPI.model.Membro;
import br.api.hallel.moduloAPI.payload.requerimento.AdministradorLoginRequest;
import br.api.hallel.moduloAPI.payload.requerimento.CadAdministradorRequerimento;
import br.api.hallel.moduloAPI.payload.requerimento.ConfirmCodeRequest;
import br.api.hallel.moduloAPI.payload.resposta.AdministradorResponse;
import br.api.hallel.moduloAPI.payload.resposta.AuthenticationResponse;
import br.api.hallel.moduloAPI.payload.resposta.LoginAdmResponse;
import br.api.hallel.moduloAPI.payload.resposta.MembroResponse;
import br.api.hallel.moduloAPI.service.main.AdministradorService;
import br.api.hallel.moduloAPI.service.main.MembroService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/administrador")
@Slf4j
public class AdministradorController {

    @Autowired
    private AdministradorService service;
    @Autowired
    private MembroService membroService;

    @PostMapping("/create")
    public ResponseEntity<?> inserirAdministrador(@Valid @RequestBody
                                                  CadAdministradorRequerimento administradorReq) {
        return this.service.inserirAdministrador(administradorReq);
    }

    @GetMapping("")
    public List<Administrador> listarAdministradores() {
        return this.service.listarTodosAdministradores();
    }

    @GetMapping("/{id}")
    public Administrador listarAdministradorPorId(
            @PathVariable String id) {
        return this.service.findAdministrador(id);
    }

    @PostMapping("/login")
    public ResponseEntity<AdministradorResponse> loginAdministrador(
            @RequestBody AdministradorLoginRequest admRequest) {
        return ResponseEntity.ok()
                .body(this.service.logarAdministrador(admRequest));
    }

    @PostMapping("/login/confirmar")
    public ResponseEntity<AuthenticationResponse> confirmarCodigo(@RequestBody ConfirmCodeRequest request) {
        return ResponseEntity.ok(this.service.validateLoginAdm(request));
    }

    @PostMapping("/{id}/update")
    public String alterarAdministrador(
            @PathVariable(value = "id") String id,
            @RequestBody Administrador administradorNovo) {
        return this.service.alterarAdministrador(id, administradorNovo);
    }

    @PostMapping("/{id}/delete")
    public String deletarAdministrador(@PathVariable String id) {
        return this.service.deletarAdministrador(id);
    }

    @GetMapping("/membros")
    public ResponseEntity<List<MembroResponse>> listAllMembros(
            @ParameterObject
            Pageable pageable) {
        return ResponseEntity.status(200)
                .body(membroService.listAllMembros(pageable));
    }

    @GetMapping("/membros/name")
    @Operation(summary = "Listar os membros a partir do nome", description = "Listar todos os membros com o determinado nome passado como parâmetro")
    public ResponseEntity<List<MembroResponse>> listAllMembrosByName(@RequestParam(name = "name") String name){
        return ResponseEntity.status(200).body(membroService.listAllMembrosByName(name));
    }


    @GetMapping("/membros/{id}")
    public ResponseEntity<Membro> listMembroId(
            @PathVariable String id) {
        return ResponseEntity.status(201)
                .body(membroService.listMembroId(id));
    }


}