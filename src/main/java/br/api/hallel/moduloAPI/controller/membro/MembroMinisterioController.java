package br.api.hallel.moduloAPI.controller.membro;

import br.api.hallel.moduloAPI.dto.v1.ministerio.*;
import br.api.hallel.moduloAPI.dto.v2.ministerio.EscalaMinisterioResponseWithInfosV2;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MinisterioResponseV2;
import br.api.hallel.moduloAPI.dto.v2.ministerio.MusicaMinisterioResponseV2;
import br.api.hallel.moduloAPI.exceptions.ExceptionResponse;
import br.api.hallel.moduloAPI.model.ConvidadoEscalaMinisterio;
import br.api.hallel.moduloAPI.model.FuncaoMinisterio;
import br.api.hallel.moduloAPI.model.MembroMinisterio;
import br.api.hallel.moduloAPI.model.NaoConfirmadoEscalaMinisterio;
import br.api.hallel.moduloAPI.security.ministerio.TokenCoordenadorMinisterio;
import br.api.hallel.moduloAPI.service.eventos.EventosService;
import br.api.hallel.moduloAPI.service.ministerio.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
import java.util.Date;
import java.util.List;

@RequestMapping("/api/membros/ministerio")
@RestController
@Tag(name = "Membro ministerio",
     description = "Endpoints para os membros do ministério")
public class MembroMinisterioController {

    @Autowired
    private MinisterioService ministerioService;

    @Autowired
    private MembroMinisterioService membroMinisterioService;

    @Autowired
    private NaoConfirmadoEscalaMinisterioService naoConfirmadoEscalaMinisterioService;

    @Autowired
    private EscalaService escalaService;

    @Autowired
    private FuncaoMinisterioService funcaoMinisterioService;

    @Autowired
    private TokenCoordenadorMinisterio tokenCoordenadorMinisterio;

    @Autowired
    RepertorioService repertorioService;
    @Autowired
    MusicaMinisterioService musicaMinisterioService;
    @Autowired
    DancaMinisterioService dancaMinisterioService;

    @Autowired
    EventosService eventosService;

    @Autowired
    EnsaioMinisterioService ensaioMinisterioService;

    @Autowired
    ConvidadoEscalaMinisterioService convidadoEscalaMinisterioService;
    @Autowired
    ConviteEscalaMinisterioService conviteEscalaMinisterioService;


    @GetMapping("/token")
    @Operation(summary = "Valida e gera um token para o coordenador")
    public String generateTokenCoordenador(
            @RequestParam(name = "ministerioId") String ministerioId,
            @RequestParam(name = "membroId") String membroId) {
        if (!ministerioService.validateCoordenadorInMinisterio(ministerioId, membroId)) {
            throw new RuntimeException("Can't generate the token for coordenador, invalid ministerio or membro id");
        }

        return tokenCoordenadorMinisterio.generateToken(ministerioId, membroId);
    }

    @GetMapping(
            "/escalas/status/{idMembroMinisterio}/{idEscalaMinisterio}")
    @Operation(summary = "Pegar o status do membro em uma escala")
    public ResponseEntity<StatusParticipacaoEscalaMinisterio>
    statusParticipacaoEscalaMinisterio
            (@PathVariable("idMembroMinisterio")
             String idMembroMinisterio,
             @PathVariable("idEscalaMinisterio")
             String idEscalaMinisterio) {
        return ResponseEntity.ok()
                             .body(this.membroMinisterioService.getStatusParticipacaoEscala(idMembroMinisterio, idEscalaMinisterio));
    }

    @PatchMapping(
            "/confirmarParticipacao/{idMembro}/{idEscalaMinisterio}")
    @Operation(
            summary = "Confirmar participação em uma escala de um ministerio")
    public ResponseEntity<Boolean> confirmParticipacaoEscalaMinisterio(
            @PathVariable("idMembro")
            String idMembroMinisterio,
            @PathVariable("idEscalaMinisterio")
            String idEscalaMinisterio) {
        return ResponseEntity.ok()
                             .body(this.membroMinisterioService.confirmarParticipacaoEscala(idMembroMinisterio, idEscalaMinisterio));

    }

    @PatchMapping(
            "/recusarParticipacao")
    @Operation(
            summary = "Recusar participação em uma escala de um ministerio")
    public ResponseEntity<Boolean> recusarParticipacaoEscalaMinisterio(
            @RequestBody
            NaoConfirmarEscalaDTO naoConfirmarEscalaDTO) {
        return ResponseEntity.ok()
                             .body(this.membroMinisterioService.recusarParticipacaoEscala(naoConfirmarEscalaDTO));
    }

    @GetMapping("/recusarParticipacao/{idMembroMinisterio}")
    @Operation(
            summary = "Listar recusa participação de um membro do ministerio")
    public ResponseEntity<List<NaoConfirmadoEscalaMinisterio>> listarRecusarParticipacaoEscalaMinisterio(
            @PathVariable("idMembroMinisterio")
            String idMembroMinisterio) {
        return ResponseEntity.ok()
                             .body(this.naoConfirmadoEscalaMinisterioService
                                     .listNaoConfirmadoEscalaMinisterioByIdMembroMinisterio(idMembroMinisterio));
    }

    @GetMapping(
            "/recusarParticipacao/{idNaoConfirmadoEscalaMinisterio}/byId")
    @Operation(summary = "Listar recusa participação pelo id")
    public ResponseEntity<NaoConfirmadoEscalaMinisterio> listarRecusarParticipacaoPeloId(
            @PathVariable("idNaoConfirmadoEscalaMinisterio")
            String idNaoConfirmadoEscalaMinisterio) {
        return ResponseEntity.ok()
                             .body(this.naoConfirmadoEscalaMinisterioService.listNaoConfirmadoEscalaMinisterioById(idNaoConfirmadoEscalaMinisterio));
    }

    @GetMapping("/escalas/canParticipate/{idMembro}")
    @Operation(
            summary = "Listar todos as escalas que pode participar em um intervalo de tempo")
    public ResponseEntity<List<EscalaMinisterioWithEventoInfoResponse>> listarEscalasMinisterioMembroCanParticipate(
            @PathVariable("idMembro")
            String idMembroMinisterio,
            @RequestParam("dateStart")
            LocalDateTime dateStart,
            @RequestParam("dateEnd") LocalDateTime dateEnd) {
        return ResponseEntity.ok(this.escalaService.listEscalaMinisterioMembroIdCanParticipate(idMembroMinisterio, dateStart, dateEnd));
    }

    @GetMapping("/escalas/canParticipate/simple/{idMembro}")
    @Operation(
            summary = "Listar todos os ids das escalas que pode participar em um intervalo de tempo")
    public ResponseEntity<List<SimpleEscalaMinisterioResponse>> listarEscalasMinisterioIdsMembroCanParticipate(
            @PathVariable("idMembro")
            String idMembroMinisterio,
            @RequestParam("dateStart")
            LocalDateTime dateStart,
            @RequestParam("dateEnd") LocalDateTime dateEnd) {
        return ResponseEntity.ok(this.escalaService.listEscalaMinisterioIdsByMembroIdThatCanParticipate(idMembroMinisterio, dateStart, dateEnd));
    }

    @GetMapping("/escalas/confirmed/{idMembro}")
    @Operation(
            summary = "Listar todos as escalas que participa em um intervalo de tempo")
    public ResponseEntity<List<EscalaMinisterioWithEventoInfoResponse>> listarEscalasMinisterioMembroConfirmed(
            @PathVariable("idMembro")
            String idMembroMinisterio,
            @RequestParam("dateStart")
            LocalDateTime dateStart,
            @RequestParam("dateEnd") LocalDateTime dateEnd) {
        return ResponseEntity.ok(this.escalaService.listEscalaMinisterioConfirmedMembro(idMembroMinisterio, dateStart, dateEnd));
    }

    @GetMapping("/escalas/confirmed/simple/{idMembro}")
    @Operation(
            summary = "Listar todos os ids das escalas que participa em um intervalo de tempo")
    public ResponseEntity<List<SimpleEscalaMinisterioResponse>> listarEscalasMinisterioIdsMembroConfirmed(
            @PathVariable("idMembro")
            String idMembroMinisterio,
            @RequestParam("dateStart")
            LocalDateTime dateStart,
            @RequestParam("dateEnd") LocalDateTime dateEnd) {
        return ResponseEntity.ok(this.escalaService.listEscalaMinisterioIdsByMembroIdThatConfirmed(idMembroMinisterio, dateStart, dateEnd));
    }

    @GetMapping("/escala/{idEscalaMinisterio}")
    @Operation(
            summary = "Listar as informações de uma escala de um ministerio pelo seu id")
    public ResponseEntity<EscalaMinisterioResponseWithInfosV2> listEscalaMinisterioById(
            @PathVariable("idEscalaMinisterio")
            String idEscalaMinisterio) {
        return ResponseEntity.ok(this.escalaService.listEscalaMinisterioByIdWithInfosV2(idEscalaMinisterio));
    }

    @GetMapping("/escala/evento/{idEvento}")
    @Operation(
            summary = "Listar as informações de um evento de uma escala pelo id do evento")
    public ResponseEntity<EventosShortResponse> listEventoEscalaMinisterioById(
            @PathVariable("idEvento") String idEvento) {
        return ResponseEntity.ok()
                             .body(this.eventosService.listEventoInEscalaInfo(idEvento));
    }

    @GetMapping("/membroParticipate/{idMembro}")
    @Operation(
            summary = "Listar os ids dos ministerios um membro participa pelo seu id"
    )
    public ResponseEntity<List<String>> listarMinisterioV2PeloMembroId(
            @PathVariable("idMembro") String idMembro) {
        return ResponseEntity.ok(this.membroMinisterioService.listMinisterioV2ThatMembroParticipateByMembroId(idMembro));
    }

    @GetMapping("/ministerio-infos/{id_ministerio}")
    public ResponseEntity<MinisterioResponseV2> listMinisterioInfos(
            @PathVariable("id_ministerio") String idMinisterio) {
        return ResponseEntity.ok(this.ministerioService.listMinisterioV2ById(idMinisterio));
    }

    @Operation(summary = "Listar funções de um ministerio")
    @GetMapping("/funcao/ministerio/{idMinisterio}")
    public ResponseEntity<List<FuncaoMinisterio>> listFuncoesMinisterioByIdMinisterio(
            @PathVariable("idMinisterio") String ministerioId) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(this.funcaoMinisterioService.listFuncaoOfMinisterio(ministerioId));
    }

    @Operation(summary = "Listar membros de um ministerio")
    @GetMapping("/membroMinisterio/list/{idMinisterio}")
    public ResponseEntity<Slice<MembroMinisterioWithInfosResponse>> listMembrosOfMinisterio(
            @PathVariable("idMinisterio") String idMinisterio,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5")
            int size) {
        return ResponseEntity.ok()
                             .body(this.membroMinisterioService.listMembrosFromMinisterio(idMinisterio, page, size));
    }


    @Operation(
            summary = "Listar membro ministerio pelo id do membro e id do ministerio")
    @GetMapping("/membroMinisterio/{idMembro}/{idMinisterio}")
    public ResponseEntity<MembroMinisterio> listMembroMinisterioByMembroidAndMinisterioId(
            @PathVariable("idMembro") String idMembro,
            @PathVariable("idMinisterio") String idMinisterio) {
        return ResponseEntity.ok()
                             .body(this.membroMinisterioService.listMembroMinisterioByMembroIdAndMinisterioId(idMembro, idMinisterio));
    }


    @GetMapping("/status/{idMinisterio}/{idMembro}")
    @Operation(
            summary = "Pegar o status do membro em um ministerio",
            responses = {
                    @ApiResponse(responseCode = "200",
                                 description = "Status do membro de um ministerio retornado com sucesso",
                                 content = @Content(schema = @Schema(
                                         implementation = StatusMembroMinisterio.class),
                                                    examples = {
                                                            @ExampleObject(
                                                                    "COORDENADOR"),
                                                            @ExampleObject(
                                                                    "VICE_COORDENADOR"),
                                                            @ExampleObject(
                                                                    "MEMBRO")})),
            }
    )
    public ResponseEntity<StatusMembroMinisterio> getStatusMembroMinisterio(
            @PathVariable("idMinisterio") String idMinisterio,
            @PathVariable("idMembro") String idMembro) {
        return ResponseEntity.ok(this.membroMinisterioService.listStatusMembroMinisterioByMinisterioIdAndMembroId(idMinisterio, idMembro));
    }

    @GetMapping("/repertorio/{idMinisterio}")
    @Operation(
            summary = "Listar os repertorio de um mininsterio pelo id do ministerio")
    public ResponseEntity<List<RepertorioResponseWithFullInfos>> listarRepertorioAsMembroMinisterio(
            @PathVariable("idMinisterio") String idMinisterio) {
        return ResponseEntity.ok(this.membroMinisterioService.listRepertorioAsMembroMinisterioByMinisterioId(idMinisterio));
    }

    @GetMapping("/musica/{idMinisterio}")
    @Operation(
            summary = "Listar as musicas de um ministerio pelo id do ministerio")
    public ResponseEntity<List<MusicaMinisterioResponse>> listarMusicaMinisterioAsMembroMinisterio(
            @PathVariable("idMinisterio") String idMinisterio) {
        return ResponseEntity.ok(this.membroMinisterioService.listMusicaMinisterioAsMembroMinisterioByMinisterioId(idMinisterio));
    }

    @GetMapping("/danca/{idMinisterio}")
    @Operation(
            summary = "Listar as danças de um ministerio pelo id do ministerio")
    public ResponseEntity<List<DancaMinisterioResponse>> listarDancaMinisterioAsMembroMinisterio(
            @PathVariable("idMinisterio") String idMinisterio) {
        return ResponseEntity.ok(this.membroMinisterioService.listDancaMinisterioAsMembroMinisterioByMinisterioId(idMinisterio));
    }


    // Escalas
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

    // Repertorio
    @GetMapping("/repertorio/list/{idMinisterio}")
    @Operation(summary = "Listar os repertorios de um ministerio")
    public ResponseEntity<List<RepertorioResponse>> listRepertoriosByMinisterioId(
            @PathVariable("idMinisterio") String idMinisterio) {
        return ResponseEntity.ok()
                             .body(this.repertorioService.listRepertoriosByMinisterioId(idMinisterio));
    }

    @GetMapping("/repertorio/list/id/{idRepertorio}/infos")
    @Operation(
            summary = "Listar um repertorio ministerio pelo seu id com danças e musicas")
    public ResponseEntity<RepertorioResponseWithFullInfos> listRepertorioByIdWithFullInfos(
            @PathVariable("idRepertorio") String idRepertorio) {
        return ResponseEntity.ok()
                             .body(this.repertorioService.listRepertorioWithDancesAndMusic(idRepertorio));
    }

    // Musica
    @GetMapping("/musica/list/{idMinisterio}")
    @Operation(summary = "Listar as musicas de um ministerio")
    public ResponseEntity<List<MusicaMinisterioResponseV2>> listMusicasMinisterioByMinisterioId(
            @PathVariable("idMinisterio") String idMinisterio) {
        return ResponseEntity.ok()
                             .body(this.musicaMinisterioService.getAllMusicaMinisterios(idMinisterio));
    }

    @GetMapping("/musica/v2/infos/{idMusicaMinisterio}")
    @Operation(summary = "Listar música ministério com as letras")
    public ResponseEntity<MusicaMinisterioResponseV2> listMusicaMinisterioWithLetters(
            @PathVariable("idMusicaMinisterio")
            String idMusicaMinisterio) {
        return ResponseEntity.ok()
                             .body(this.musicaMinisterioService.getMusicaMinisterioWithLetra(idMusicaMinisterio));
    }

    @GetMapping("/musica/letra/{idMusicaMinisterio}")
    @Operation(summary = "Listar a letra de uma música pelo seu id")
    public ResponseEntity<String> listLetraMusicaMinisterio(
            @PathVariable("idMusicaMinisterio")
            String idMusicaMinisterio) {
        return ResponseEntity.ok()
                             .body(this.musicaMinisterioService.getLetraOfMusicaMinisterio(idMusicaMinisterio));
    }

    // Dança
    @GetMapping("/danca/list/{idMinisterio}")
    @Operation(summary = "Listar as danças de um ministerio")
    public ResponseEntity<List<DancaMinisterioResponse>> listDancaMinisterioByMinisterioId(
            @PathVariable("idMinisterio") String idMinisterio) {
        return ResponseEntity.ok()
                             .body(this.dancaMinisterioService.getAllDancaMinisterio(idMinisterio));
    }

    /**
     * Ensaios do ministério
     */

    @GetMapping("/ensaio/list/{idMinisterio}")
    @Operation(
            summary = "Listar os ensaios de um determinado ministerio")
    public ResponseEntity<List<EnsaioMinisterioResponse>> listEnsaioMinisterioResponse(
            @PathVariable("idMinisterio") String idMinisterio) {
        return ResponseEntity.ok()
                             .body(this.ensaioMinisterioService.listEnsaioMinisterioByMinisterioId(idMinisterio));

    }

    @GetMapping("/ensaio/list/id/{idEnsaioMinisterio}")
    @Operation(
            summary = "Listar o ensaio de um ministério pelo seu id")
    public ResponseEntity<EnsaioMinisterioResponse> listEnsaioMinisterioById(
            @PathVariable("idEnsaioMinisterio")
            String idEnsaioMinisterio) {
        return ResponseEntity.ok()
                             .body(this.ensaioMinisterioService.listEnsaioMinisterioById(idEnsaioMinisterio));
    }

    @GetMapping("/ensaio/statusMembro")
    @Operation(
            summary = "Listar o status de participação do membro do ministério no ensaio")
    public ResponseEntity<StatusMembroMinisterioInEscala> statusMembroMinisterioInEnsaio(
            @RequestParam(name = "idEnsaio") String idEnsaio,
            @RequestParam(name = "idMembro") String idMembro) {
        return ResponseEntity.ok()
                             .body(this.ensaioMinisterioService.getStatusMembroInEnsaioMinisterio(idEnsaio, idMembro));
    }

    @PatchMapping("/ensaio/confirmParticipation")
    @Operation(
            summary = "Confirmar participação em um ensaio de um ministério")
    public ResponseEntity<Boolean> confirmParticipacaoInEnsaio(
            @RequestParam(name = "idEnsaio") String idEnsaio,
            @RequestParam(name = "idMembro") String idMembro) {
        return ResponseEntity.ok()
                             .body(this.membroMinisterioService.confirmarParticipacaoEnsaio(idMembro, idEnsaio));
    }

    @PutMapping("/ensaio/declineParticipation")
    @Operation(
            summary = "Recusar participação em um ensaio de um ministério")
    public ResponseEntity<Boolean> declineParticipationInEnsaio(
            @RequestBody
            NaoConfirmarEnsaioDTO naoConfirmarEnsaioDTO) {
        return ResponseEntity.ok()
                             .body(this.membroMinisterioService.recusarParticipacaoEnsaio(naoConfirmarEnsaioDTO));
    }


}
