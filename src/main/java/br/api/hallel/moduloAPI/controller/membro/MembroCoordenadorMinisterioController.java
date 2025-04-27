package br.api.hallel.moduloAPI.controller.membro;

import br.api.hallel.moduloAPI.dto.v1.ministerio.*;
import br.api.hallel.moduloAPI.dto.v2.ministerio.EscalaMinisterioResponseWithInfosV2;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MusicaMinisterioDTOV2;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MusicaMinisterioResponseV2;
import br.api.hallel.moduloAPI.exceptions.ExceptionResponse;
import br.api.hallel.moduloAPI.exceptions.ministerio.MessageTelegramNotSendToConvidadoException;
import br.api.hallel.moduloAPI.model.*;
import br.api.hallel.moduloAPI.payload.resposta.MembroResponse;
import br.api.hallel.moduloAPI.service.ministerio.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api/membros/ministerio/coordenador")
@RestController
@Tag(name = "Coordenador ministerio",
     description = "Endpoints para os coordenadores de um ministério")
public class MembroCoordenadorMinisterioController {

    @Autowired
    private MinisterioService ministerioService;
    @Autowired
    EscalaService escalaService;
    @Autowired
    NaoConfirmadoEscalaMinisterioService naoConfirmadoEscalaService;
    @Autowired
    MembroMinisterioService membroMinisterioService;
    @Autowired
    FuncaoMinisterioService funcaoMinisterioService;
    @Autowired
    RepertorioService repertorioService;
    @Autowired
    MusicaMinisterioService musicaMinisterioService;
    @Autowired
    DancaMinisterioService dancaMinisterioService;
    @Autowired
    EnsaioMinisterioService ensaioMinisterioService;
    @Autowired
    ConvidadoEscalaMinisterioService convidadoEscalaMinisterioService;
    @Autowired
    ConviteEscalaMinisterioService conviteEscalaMinisterioService;


    /**
     * Parte de escala ministério (COORDENADOR)
     */

    @PostMapping("/escala/resucarParticipacao")
    @Operation(
            summary = "Criar uma recusa de participação por um membro do ministerio",
            description = "Criará uma recusa de participação de um membro em uma escala de um ministerio, isto feito pelo coordenador")
    public ResponseEntity<NaoConfirmadoEscalaMinisterio> criarRecusaParticipacao(
            @RequestBody
            NaoConfirmarEscalaDTO naoConfirmarEscalaDTO) {
        return ResponseEntity.ok()
                             .body(this.naoConfirmadoEscalaService.createNaoConfirmadoEscalaMinisterio(naoConfirmarEscalaDTO));
    }

    @PutMapping(
            "/escala/recusarParticipacao/{idRecusaParticipacao}/edit")
    @Operation(
            summary = "Editar uma recusa de participação por um membro de um ministerio")
    public ResponseEntity<NaoConfirmadoEscalaMinisterio> editarRecusaParticipacao(
            @PathVariable("idRecusaParticipacao")
            String idRecusaParticipacao, @RequestBody
            NaoConfirmarEscalaDTO naoConfirmarEscalaDTO) {
        return ResponseEntity.ok()
                             .body(this.naoConfirmadoEscalaService.editNaoConfirmadoEscalaMinisterio(idRecusaParticipacao, naoConfirmarEscalaDTO));
    }

    @GetMapping("/escala/members/can-invite/{idEscala}")
    @Operation(
            summary = "Listar os membros que podem ser convidados em uma escala")
    public ResponseEntity<List<String>> listMembrosCanInviteInEscala(
            @PathVariable("idEscala") String idEscala,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10")
            int size) {
        return ResponseEntity.ok()
                             .body(this.escalaService.listMembroMinisterioCanInviteToEscala(idEscala, page, size));
    }

    @PatchMapping("/escala/convidarMembro/{idEscala}")
    @Operation(
            summary = "Convidar membros em uma escala a partir de seus ids")
    public ResponseEntity<EscalaMinisterioResponse> convidarMembrosInEscala(
            @PathVariable("idEscala") String idEscala,
            @RequestBody List<String> idsMembrosMinisterio) {
        return ResponseEntity.ok()
                             .body(this.escalaService.alterarEscalaConvidarMembroMinisterio(idEscala, idsMembrosMinisterio));
    }

    @PatchMapping("/escala/desconvidarMembro/{idEscala}")
    @Operation(
            summary = "Desconvidar membros em uma escala a partir de seus ids")
    public ResponseEntity<EscalaMinisterioResponse> desconvidarMembroInEscala(
            @PathVariable("idEscala") String idEscala,
            @RequestBody List<String> idsMembroMinisterio) {
        return ResponseEntity.ok()
                             .body(this.escalaService.alterarEscalaDesconvidarMembroMinisterio(idEscala, idsMembroMinisterio));
    }

    @PatchMapping("/escala/confirmarMembros/{idEscala}")
    @Operation(
            summary = "Confirmar membros em uma escala a partir de seus ids")
    public ResponseEntity<EscalaMinisterioResponse> confirmarMembrosInEscala(
            @PathVariable("idEscala") String idEscala,
            @RequestBody List<String> idsMembrosMinisterio) {
        return ResponseEntity.ok()
                             .body(this.escalaService
                                     .alterarEscalaConfirmandoMembroMinisterio(idEscala, idsMembrosMinisterio));
    }

    @PatchMapping("/escala/recusarMembros/{idEscala}")
    @Operation(
            summary = "Ausentar membros em uma escala")
    public ResponseEntity<EscalaMinisterioResponse> ausentarMembrosInEscala(
            @PathVariable("idEscala") String idEscala,
            @RequestBody
            List<NaoConfirmadoEscalaDTOAdm> naoConfirmadoEscalaDTO) {
        return ResponseEntity.ok()
                             .body(this.escalaService
                                     .alterarEscalaNaoConfirmandoMembroMinisterio(idEscala, naoConfirmadoEscalaDTO));
    }


    @GetMapping("/escala/{idEscalaMinisterio}/ausencias")
    @Operation(
            summary = "Listar as ausencias e seus motivos a partir do id de um escala")
    public ResponseEntity<List<NaoConfirmadoEscalaMinisterioWithInfos>> listAusenciaEscalaMinisterioById(
            @PathVariable("idEscalaMinisterio")
            String idEscalaMinisterio) {
        return ResponseEntity.ok(this.escalaService
                .listMotivosAusenciaMembroEventoByIdEscalasMinisterio(idEscalaMinisterio));
    }

    @GetMapping("/escala/{idMinisterio}/date")
    @Operation(
            summary = "Listar as escalas de um ministerio em um intervalo de tempo")
    public ResponseEntity<List<EscalaMinisterioWithEventoInfoResponse>> listEscalaMinisterioByIdMinisterio(
            @PathVariable("idMinisterio") String idMinisterio,
            @RequestParam("dateStart")
            LocalDateTime dateStart,
            @RequestParam("dateEnd") LocalDateTime dateEnd) {
        return ResponseEntity.ok(this.escalaService
                .listEscalaMinisterioRangeDateByMinisterioId(idMinisterio, dateStart, dateEnd));
    }

    @GetMapping("/escala/simple/{idMinisterio}/date")
    @Operation(
            summary = "Listar os ids das escalas de um ministério em um intervalo de tempo")
    public ResponseEntity<List<SimpleEscalaMinisterioResponse>> listEscalaMinisterioIdsByIdMinisterio(
            @PathVariable("idMinisterio") String idMinisterio,
            @RequestParam("dateStart") LocalDateTime start,
            @RequestParam("dateEnd") LocalDateTime end) {
        return ResponseEntity.ok()
                             .body(this.escalaService.listEscalaMinisterioIdsByMinisterioIdAndRangeDate(idMinisterio, start, end));
    }

    @PatchMapping("/escala/repertorio/{idEscala}")
    @Operation(summary = "Adicionar ou remover repertorio")
    public ResponseEntity<EscalaMinisterioResponseWithInfosV2> adicionarRemoverRepertorioInEscala(
            @PathVariable("idEscala") String idEscalaMinisterio,
            @RequestBody EscalaRepertorioDTO escalaRepertorioDTO) {
        return ResponseEntity.ok(this.escalaService.adicionarRemoverRepertorioInEscala(idEscalaMinisterio, escalaRepertorioDTO));
    }

    @GetMapping("/escala/v2/{idEscalaMinisterio}")
    @Operation(
            summary = "V2 - Listar as informações de uma escala de um ministerio pelo seu id")
    public ResponseEntity<EscalaMinisterioResponseWithInfosV2> listEscalaMinisterioV2ById(
            @PathVariable("idEscalaMinisterio")
            String idEscalaMinisterio) {
        return ResponseEntity.ok(this.escalaService.listEscalaMinisterioByIdWithInfosV2(idEscalaMinisterio));
    }

    /**
     * @param idMinisterio
     * @return List {@link EventosShortResponse}
     * @apiNote {@summary Listar os eventos que o ministerio participa}
     */
    @Operation(summary = "Listar eventos que o ministerio participa")
    @GetMapping("/eventos/{idMinisterio}")
    public List<EventosShortResponse> listarEventosMinisterioParticipa(
            @PathVariable String idMinisterio) {
        return this.ministerioService.listEventosThatMinisterioIsIn(idMinisterio);
    }

    /**
     * Parte de membros de um ministerio (COORDENADOR)
     *
     * @return {@link MembroResponse}, {@link MembroMinisterioWithInfosResponse}
     */

    @Operation(
            summary = "Listar membros adicionaveis em um ministerio")
    @GetMapping("/membroMinisterio/disponivel/{idMinisterio}")
    public ResponseEntity<Slice<MembroResponse>> listMembroToAddIntoMinisterio(
            @PathVariable("idMinisterio") String idMinisterio, @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok()
                             .body(this.membroMinisterioService.listMembrosToAddIntoThisMinisterio(idMinisterio, page, size));
    }

    @Operation(
            summary = "Listar um membro de um ministerio pelo seu id")
    @GetMapping("/membroMinisterio/{idMembroMinisterio}")
    public ResponseEntity<MembroMinisterioWithInfosResponse> listMembrosOfMinisterioById(
            @PathVariable("idMembroMinisterio")
            String idMembroMinisterio) {
        return ResponseEntity.ok()
                             .body(this.membroMinisterioService.listMembroMinisterioById(idMembroMinisterio));
    }

    @Operation(
            summary = "Adicionar um membro em um ministerio"
    )
    @PostMapping("/membroMinisterio")
    public ResponseEntity<MembroMinisterio> adicionarMembroMinisterio(
            @RequestBody
            AddMembroMinisterioDTO addMembroMinisterioDTO) {
        return ResponseEntity.ok()
                             .body(this.membroMinisterioService.addMembroMinisterio(addMembroMinisterioDTO));
    }

    @Operation(
            summary = "Remover um membro de um ministerio"
    )
    @DeleteMapping("/membroMinisterio/{idMembroMinisterio}")
    public ResponseEntity<?> removerMembroMinisterio(
            @PathVariable("idMembroMinisterio")
            String idMembroMinisterio) {
        this.membroMinisterioService.removerMembroMinisterio(idMembroMinisterio);
        return ResponseEntity.noContent().build();
    }

    /**
     * Parte de funções de um ministerio
     *
     * @return {@link FuncaoMinisterio}, {@link MembroMinisterioWithInfosResponse}
     */

    @Operation(summary = "Adicionar função ministerio")
    @PostMapping("/funcao")
    public ResponseEntity<FuncaoMinisterio> createFuncaoMinisterio(
            @RequestBody
            FuncaoMinisterioDTO funcaoMinisterioDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(this.funcaoMinisterioService.createFuncaoMinisterio(funcaoMinisterioDTO));
    }

    @Operation(summary = "Listar uma função ministerio pelo id")
    @GetMapping("/funcao/{idFuncaoMinisterio}")
    public ResponseEntity<FuncaoMinisterio> lsitFuncaoById(
            @PathVariable("idFuncaoMinisterio")
            String idFuncaoMinisterio) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(this.funcaoMinisterioService.listFuncaoMinisterioById(idFuncaoMinisterio));
    }

    @Operation(summary = "Editar uma função ministerio")
    @PutMapping("/funcao/{idFuncaoMinisterio}")
    public ResponseEntity<FuncaoMinisterio> editFuncaoMinisterio(
            @PathVariable("idFuncaoMinisterio")
            String idFuncaoMinisterio,
            @RequestBody FuncaoMinisterioDTO funcaoMinisterioDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(this.funcaoMinisterioService.editFuncaoMinisterio(idFuncaoMinisterio, funcaoMinisterioDTO));
    }

    @Operation(summary = "Deletar uma função ministerio")
    @DeleteMapping("/funcao/{idFuncaoMinisterio}")
    public ResponseEntity<?> deleteFuncaoMinisterio(
            @PathVariable("idFuncaoMinisterio")
            String idFuncaoMinisterio) {
        this.funcaoMinisterioService.deleteFuncaoMinisterio(idFuncaoMinisterio);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Definir uma função a um membro do ministerio",
            description = "Rota para definir um função do ministerio a um membro ministerio")
    @PatchMapping("/funcao/membroMinisterio")
    public ResponseEntity<MembroMinisterioWithInfosResponse> defineFuncaoMinisterioToMembroMinisterio(
            @RequestBody
            DefineFunctionsDTO defineFunctionsDTO) {
        return ResponseEntity.ok()
                             .body(this.funcaoMinisterioService.defineFunctionsToMembroMinisterio(defineFunctionsDTO));
    }

    /**
     * Parte de repertorio do ministerio
     */

    @PostMapping("/repertorio/create")
    @Operation(summary = "Criar repertorio para o ministerio")
    public ResponseEntity<RepertorioResponse> createRepertorio(
            @RequestBody RepertorioDTO repertorioDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(this.repertorioService.createRepertorio(repertorioDTO));
    }

    @GetMapping("/repertorio/list/{idMinisterio}")
    @Operation(summary = "Listar os repertorios de um ministerio")
    public ResponseEntity<List<RepertorioResponse>> listRepertoriosByMinisterioId(
            @PathVariable("idMinisterio") String idMinisterio) {
        return ResponseEntity.ok()
                             .body(this.repertorioService.listRepertoriosByMinisterioId(idMinisterio));
    }

    @GetMapping("/repertorio/list/id/{idRepertorio}")
    @Operation(
            summary = "Listar um repertorio ministerio pelo seu id")
    public ResponseEntity<RepertorioResponse> listRepertoriosByRepertorioId(
            @PathVariable("idRepertorio") String idRepertorio) {
        return ResponseEntity.ok()
                             .body(this.repertorioService.listRepertorioById(idRepertorio));
    }

    @GetMapping("/repertorio/list/id/{idRepertorio}/infos")
    @Operation(
            summary = "Listar um repertorio ministerio pelo seu id com danças e musicas")
    public ResponseEntity<RepertorioResponseWithFullInfos> listRepertorioByIdWithFullInfos(
            @PathVariable("idRepertorio") String idRepertorio) {
        return ResponseEntity.ok()
                             .body(this.repertorioService.listRepertorioWithDancesAndMusic(idRepertorio));
    }

    @PutMapping("/repertorio/{idRepertorio}/edit")
    @Operation(summary = "Editar um repertorio de um ministerio")
    public ResponseEntity<RepertorioResponse> editRepertorio(
            @PathVariable("idRepertorio") String idRepertorio,
            @RequestBody RepertorioDTOEdit repertorioDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(this.repertorioService.editRepertorio(idRepertorio, repertorioDTO));
    }

    @DeleteMapping("/repertorio/{idRepertorio}/delete")
    @Operation(summary = "Deletar um repertorio de um ministerio")
    public ResponseEntity<?> deleteRepertorio(
            @PathVariable("idRepertorio") String idRepertorio) {
        this.repertorioService.deleteRepertorio(idRepertorio);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/repertorio/music/{idRepertorio}")
    @Operation(summary = "Adicionar ou remover musicas do repertorio")
    public ResponseEntity<RepertorioResponseWithInfos> repertorioMusicUpdated(
            @PathVariable("idRepertorio") String idRepertorio,
            @RequestBody RepertorioMusicDTO repertorioMusicDTO) {
        return ResponseEntity.ok()
                             .body(this.repertorioService.adicionarRemoverMusicasRepertorio(idRepertorio, repertorioMusicDTO));
    }

    @PatchMapping("/repertorio/dance/{idRepertorio}")
    @Operation(summary = "Adicionar ou remover danças do repertorio")
    public ResponseEntity<RepertorioResponseWithInfos> repertorioDanceUpdated(
            @PathVariable("idRepertorio") String idRepertorio,
            @RequestBody RepertorioDancaDTO repertorioDancaDTO) {
        return ResponseEntity.ok()
                             .body(this.repertorioService.adicionarRemoverDancaRepertorio(idRepertorio, repertorioDancaDTO));
    }

    /**
     * Parte de musicas do ministerio
     */

    @PostMapping("/musica/create")
    @Operation(
            summary = "Criar musica de um ministerio para um ministério")
    public ResponseEntity<MusicaMinisterioResponse> createMusicaMinisterio(
            @RequestBody MusicaMinisterioDTO musicaMinisterioDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(this.musicaMinisterioService.createMusicaMinisterio(musicaMinisterioDTO));
    }

    @PostMapping("/musica/v2/create")
    @Operation(
            summary = "Criar música de um ministério para um ministério com letras")
    public ResponseEntity<MusicaMinisterioResponseV2> createMusicaMinisterioV2(
            @RequestBody
            MusicaMinisterioDTOV2 musicaMinisterioDTOV2) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(this.musicaMinisterioService.createMusicaMinisterioV2(musicaMinisterioDTOV2));
    }

    @GetMapping("/musica/list/{idMinisterio}")
    @Operation(summary = "Listar as musicas de um ministerio")
    public ResponseEntity<List<MusicaMinisterioResponseV2>> listMusicasMinisterioByMinisterioId(
            @PathVariable("idMinisterio") String idMinisterio) {
        return ResponseEntity.ok()
                             .body(this.musicaMinisterioService.getAllMusicaMinisterios(idMinisterio));
    }

    @GetMapping("/musica/list/id/{idMusicaMinisterio}")
    @Operation(
            summary = "Listar uma musica ministerio pelo seu id")
    public ResponseEntity<MusicaMinisterioResponse> listMusicaMinisterioById(
            @PathVariable("idMusicaMinisterio")
            String idMusicaMinisterio) {
        return ResponseEntity.ok()
                             .body(this.musicaMinisterioService.getMusicaMinisterioById(idMusicaMinisterio));
    }

    @PutMapping("/musica/{idMusicaMinisterio}/edit")
    @Operation(summary = "Editar uma musica de um ministerio")
    public ResponseEntity<MusicaMinisterioResponse> editMusicaMinisterio(
            @PathVariable("idMusicaMinisterio")
            String idMusicaMinisterio,
            @RequestBody
            MusicaMinisterioDTOEdit musicaMinisterioDTOEdit) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(this.musicaMinisterioService.updateMusicaMinisterio(idMusicaMinisterio, musicaMinisterioDTOEdit));
    }

    @PutMapping("/musica/v2/{idMusicaMinisterio}/edit")
    @Operation(
            summary = "Editar uma música de um ministério com letras")
    public ResponseEntity<MusicaMinisterioResponseV2> editMusicaMinisterioWithLetra(
            @PathVariable("idMusicaMinisterio")
            String idMusicaMinisterio, @RequestBody
            MusicaMinisterioDTOV2 musicaMinisterioDTOV2) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(this.musicaMinisterioService.updateMusicaMinisterioV2(idMusicaMinisterio, musicaMinisterioDTOV2));
    }

    @DeleteMapping("/musica/{idMusicaMinisterio}/delete")
    @Operation(summary = "Deletar uma musica de um ministerio")
    public ResponseEntity<?> deleteMusicaMinisterio(
            @PathVariable("idMusicaMinisterio")
            String idMusicaMinisterio) {
        this.musicaMinisterioService.deleteMusicaMinisterio(idMusicaMinisterio);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Parte de danças do ministerio
     */

    @PostMapping("/danca/create")
    @Operation(
            summary = "Criar dança de um ministerio para o repertorio")
    public ResponseEntity<DancaMinisterioResponse> createDancaMinisterio(
            @RequestBody DancaMinisterioDTO dancaMinisterioDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(this.dancaMinisterioService.createDancaMinisterio(dancaMinisterioDTO));
    }

    @GetMapping("/danca/list/{idMinisterio}")
    @Operation(summary = "Listar as danças de um ministerio")
    public ResponseEntity<List<DancaMinisterioResponse>> listDancaMinisterioByMinisterioId(
            @PathVariable("idMinisterio") String idMinisterio) {
        return ResponseEntity.ok()
                             .body(this.dancaMinisterioService.getAllDancaMinisterio(idMinisterio));
    }

    @GetMapping("/danca/list/id/{idDancaMinisterio}")
    @Operation(
            summary = "Listar uma dança do ministerio pelo seu id")
    public ResponseEntity<DancaMinisterioResponse> listDancaMinisterioById(
            @PathVariable("idDancaMinisterio")
            String idDancaMinisterio) {
        return ResponseEntity.ok()
                             .body(this.dancaMinisterioService.getDancaMinisterioById(idDancaMinisterio));
    }

    @PutMapping("/danca/{idDancaMinisterio}/edit")
    @Operation(summary = "Editar uma dança de um ministerio")
    public ResponseEntity<DancaMinisterioResponse> editDancaMinisterio(
            @PathVariable("idDancaMinisterio")
            String idDancaMinisterio,
            @RequestBody
            DancaMinisterioDTOEdit dancaMinisterioDTOEdit) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(this.dancaMinisterioService.updateDancaMinisterio(idDancaMinisterio, dancaMinisterioDTOEdit));
    }

    @DeleteMapping("/danca/{idDancaMinisterio}/delete")
    @Operation(summary = "Deletar uma dança de um ministerio")
    public ResponseEntity<?> deleteDancaMinisterio(
            @PathVariable("idDancaMinisterio")
            String idDancaMinisterio) {
        this.dancaMinisterioService.deleteDancaMinisterio(idDancaMinisterio);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Parte de ensaio dos ministério
     */

    @PostMapping("/ensaio/create")
    @Operation(
            summary = "Criar um ensaio para o ministério, criará também uma escala")
    public ResponseEntity<EnsaioMinisterioResponse> createEnsaioMinisterio(
            @RequestBody EnsaioMinisterioDTO ensaioMinisterioDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(this
                                     .ensaioMinisterioService
                                     .createEnsaioMinisterio(ensaioMinisterioDTO));
    }

    @PutMapping("/ensaio/edit/{idEnsaioMinisterio}")
    @Operation(summary = "Editar um ensaio de um ministério")
    public ResponseEntity<EnsaioMinisterioResponse> editEnsaioMinisterio(
            @PathVariable("idEnsaioMinisterio") String idEnsaio,
            @RequestBody EnsaioMinisterioDTO dto) {
        return ResponseEntity.ok()
                             .body(this
                                     .ensaioMinisterioService
                                     .editEnsaioMinisterio(idEnsaio, dto));
    }

    @DeleteMapping("/ensaio/delete/{idEnsaioMinisterio}")
    @Operation(summary = "Deletar um ensaio de um ministerio")
    public ResponseEntity<Boolean> deleteEnsaioMinisterio(
            @PathVariable("idEnsaioMinisterio") String idEnsaio) {
        this.ensaioMinisterioService.deleteEnsaioMinisterio(idEnsaio);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/ensaio/can-associate-escala")
    @Operation(
            summary = "Listar as escalas que podem ser associadas ao ensaios de um ministério a partir de uma data")
    public ResponseEntity<List<SimpleEscalaMinisterioResponse>> listEscalasAddableIntoEnsaioFromDate(
            @RequestParam(name = "ministerioId") String ministerioId,
            @RequestParam(name = "from") LocalDateTime from) {
        return ResponseEntity.ok()
                             .body(this.ensaioMinisterioService.listEscalasThatCanAssociateIntoEnsaioMinisterio(ministerioId, from));
    }

    @PatchMapping("/ensaio/associate-escala")
    @Operation(summary = "Associar escala em um ensaio pelo seus ids")
    public ResponseEntity<EnsaioMinisterioResponse> associateEscalaMinisterioInEnsaio(
            @RequestParam(name = "idEnsaio") String idEnsaio,
            @RequestParam(name = "idEscala") String idEscala) {
        return ResponseEntity.ok()
                             .body(this
                                     .ensaioMinisterioService
                                     .associateEscalaIntoEnsaioMinisterio(idEscala, idEnsaio));
    }


    /**
     * Parte de convidado de uma escala do ministério
     */

    @PostMapping("/convidado-escala/create-and-send-message")
    @Operation(
            summary = "Criar um convidado e enviar mensagem para ele",
            description = "Criar um convidado de uma escala de um ministério e enviar mensagem para o número inserido no telegram",
            responses = {@ApiResponse(responseCode = "201",
                                      description = "Convidado criado e mensagem enviada",
                                      content = @Content(
                                              schema = @Schema(
                                                      implementation = ConvidadoEscalaMinisterioWithConviteResponse.class))),
                         @ApiResponse(responseCode = "500",
                                      description = "Erro ao enviar mensagem ao telegram",
                                      content = @Content(
                                              schema = @Schema(
                                                      implementation = ExceptionResponse.class)))})
    public ResponseEntity<ConvidadoEscalaMinisterioWithConviteResponse> createConvidadoAndSendMessage(
            @RequestBody ConvidadoEscalaMinisterioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(this.convidadoEscalaMinisterioService.createConvidadoAndSendMessage(dto));

    }

    @DeleteMapping(
            "/convidado-escala/delete-guest/{idConvidadoEscala}")
    @Operation(summary = "Excluir um convidado de uma escala",
               description = "Excluir um convidado de um escala atráves de seu id e enviar mensagem para o número cadastrado",
               responses = {@ApiResponse(responseCode = "200",
                                         description = "Convidado excluido com sucesso e mensagem enviada",
                                         content = @Content(
                                                 schema = @Schema(
                                                         implementation = Boolean.class))),
                            @ApiResponse(responseCode = "404",
                                         description = "Convidado não encontrado",
                                         content = @Content(
                                                 schema = @Schema(
                                                         implementation = ExceptionResponse.class)))})

    public ResponseEntity<Boolean> deleteConvidadoAndSendMessage(
            @PathVariable("idConvidadoEscala")
            String idConvidadoEscala) {
        return ResponseEntity.ok()
                             .body(this.convidadoEscalaMinisterioService.deleteConvidadoAndSendMessage(idConvidadoEscala));
    }

    @DeleteMapping(
            "/convidado-escala/delete-invite/{idConvidadoEscala}")
    @Operation(
            summary = "Excluir um convite de um convidado de uma escala",
            description = "Excluir um convite de um convidado cadastrado em um convidado",
            responses = {@ApiResponse(responseCode = "200",
                                      description = "Convite excluido com sucesso",
                                      content = @Content(
                                              schema = @Schema(
                                                      implementation = Boolean.class))),
                         @ApiResponse(responseCode = "404",
                                      description = "Convidado não encontrado",
                                      content = @Content(
                                              schema = @Schema(
                                                      implementation = ExceptionResponse.class)))})
    public ResponseEntity<Boolean> deleteConviteOfConvidado(
            @PathVariable("idConvidadoEscala")
            String idConvidadoEscala) {
        return ResponseEntity.ok()
                             .body(this.convidadoEscalaMinisterioService.deleteConviteOfConvidado(idConvidadoEscala));
    }


    @PutMapping("/convidado-escala/edit-message-and-resend")
    @Operation(
            summary = "Editar a mensagem do convite de um convidado de uma escala",
            description = "Edita um convite de um convidado da escala e envia a mensagem para o telegram para o número cadastrado",
            responses = {@ApiResponse(responseCode = "200",
                                      description = "Convite editado com sucesso e mensagem enviado para o número",
                                      content = @Content(
                                              schema = @Schema(
                                                      implementation = ConviteEscalaResponse.class))),
                         @ApiResponse(responseCode = "404",
                                      description = "Convidado não encontrado",
                                      content = @Content(
                                              schema = @Schema(
                                                      implementation = ExceptionResponse.class)))})
    public ResponseEntity<ConviteEscalaResponse> editConviteAndResendMessage(
            @RequestParam("convidadoEscalaId")
            String convidadoEscalaId,
            @RequestParam("conviteEscalaId") String conviteEscalaId,
            @RequestBody
            ConviteEscalaMinisterioDTO dto) {
        return ResponseEntity.ok()
                             .body(this.conviteEscalaMinisterioService.editConviteEscalaAndResend(convidadoEscalaId, conviteEscalaId, dto));
    }

    @PutMapping("/convidado-escala/infos/edit/{idConvidadoEscala}")
    @Operation(
            summary = "Editar as informações do convidado de uma escala",
            description = "Edita somente as informações como nome, email e telefone de um convidado de uma escala do ministério")
    public ResponseEntity<ConvidadoEscalaMinisterioWithConviteResponse> editInfosConvidadoEscala(
            @PathVariable("idConvidadoEscala")
            String idConvidadoEscala,
            @RequestBody EditConvidadoEscalaMinisterioDTO dto) {
        return ResponseEntity.ok()
                             .body(this.convidadoEscalaMinisterioService.editConvidado(idConvidadoEscala, dto));
    }


    @GetMapping("/convidado-escala/resend-message")
    @Operation(summary = "Reenviar convite para o número",
               description = "Reenviar o convite para um determinado número para o telegram",
               responses = {@ApiResponse(responseCode = "200",
                                         description = "Convite reenviado com sucesso",
                                         content = @Content(
                                                 schema = @Schema(
                                                         implementation = Boolean.class))),
                            @ApiResponse(responseCode = "404",
                                         description = "Convite não encontrado",
                                         content = @Content(
                                                 schema = @Schema(
                                                         implementation = ExceptionResponse.class)))})
    public ResponseEntity<Boolean> resendConviteEscala(
            @RequestParam("phoneGuest") String phoneGuest,
            @RequestParam("conviteEscalaId") String conviteEscalaId,
            @RequestParam("eventoId") String eventoId) {
        return ResponseEntity.ok()
                             .body(this.conviteEscalaMinisterioService.resendConviteEscala(phoneGuest, conviteEscalaId, eventoId));

    }

    @GetMapping("/convidado-escala/convidado/{idConvidadoEscala}")
    @Operation(summary = "Informações do convidado",
               description = "Listar as informações do convidado de uma escala pelo seu id",
               responses = {@ApiResponse(responseCode = "200",
                                         description = "Informações do convidado de uma escala carregada com sucesso",
                                         content = @Content(
                                                 schema = @Schema(
                                                         implementation = ConvidadoEscalaMinisterioWithInfos.class))),
                            @ApiResponse(responseCode = "404",
                                         description = "Convidado não encontrado",
                                         content = @Content(
                                                 schema = @Schema(
                                                         implementation = ExceptionResponse.class)))})
    public ResponseEntity<ConvidadoEscalaMinisterioWithInfos> listConvidadoWithInfos(
            @PathVariable("idConvidadoEscala")
            String idConvidadoEscala) {
        return ResponseEntity.ok()
                             .body(this.convidadoEscalaMinisterioService.listConvidadoInfosById(idConvidadoEscala));
    }

    @GetMapping("/convidado-escala/convite/{idConvite}")
    @Operation(summary = "Convite de uma escala",
               description = "Listar as informações de um convite de uma escala pelo seu id",
               responses = {@ApiResponse(responseCode = "200",
                                         description = "Informações do convite de uma escala carregada com sucesso",
                                         content = @Content(
                                                 schema = @Schema(
                                                         implementation = ConviteEscalaResponse.class))),
                            @ApiResponse(responseCode = "404",
                                         description = "Convite não encontrado",
                                         content = @Content(
                                                 schema = @Schema(
                                                         implementation = ExceptionResponse.class)))})
    public ResponseEntity<ConviteEscalaResponse> listConviteEscalaById(
            @PathVariable("idConvite") String idConvite) {
        return ResponseEntity.ok()
                             .body(this.conviteEscalaMinisterioService.listConviteEscala(idConvite));
    }

    @GetMapping("/convidado-escala/with-invite/{idEscala}")
    @Operation(
            summary = "Listar os convidados de uma escala com convite",
            description = "Listar os convidados de uma escala com convite pelo id da escala")
    public ResponseEntity<List<ConvidadoEscalaMinisterioWithConviteResponse>> listConvidadosWithInvitesByEscalaId(
            @PathVariable("idEscala") String idEscala) {
        return ResponseEntity.ok()
                             .body(this.convidadoEscalaMinisterioService.listConvidadosWithConvites(idEscala));
    }


}
