package br.api.hallel.moduloAPI.controller.administrador;

import br.api.hallel.moduloAPI.dto.v1.evento.EventoDTO;
import br.api.hallel.moduloAPI.dto.v1.evento.EventoResponse;
import br.api.hallel.moduloAPI.exceptions.ApiError;
import br.api.hallel.moduloAPI.financeiroNovo.service.PagamentoEntradaEventoService;
import br.api.hallel.moduloAPI.model.DespesaEvento;
import br.api.hallel.moduloAPI.model.Eventos;
import br.api.hallel.moduloAPI.model.Membro;
import br.api.hallel.moduloAPI.payload.requerimento.DespesaEventoRequest;
import br.api.hallel.moduloAPI.payload.resposta.EventosResponse;
import br.api.hallel.moduloAPI.payload.resposta.EventosVisualizacaoResponse;
import br.api.hallel.moduloAPI.payload.resposta.SeVoluntariarEventoResponse;
import br.api.hallel.moduloAPI.service.eventos.EventoArquivadoService;
import br.api.hallel.moduloAPI.service.eventos.EventosService;
import br.api.hallel.moduloAPI.service.eventos.VoluntarioService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/administrador/eventos")
@Slf4j
public class AdmEventosController {

    @Autowired
    private EventosService eventosService;
    @Autowired
    private EventoArquivadoService eventoArquivadoService;
    @Autowired
    private PagamentoEntradaEventoService pagamentoEntradaService;

    @Autowired
    private VoluntarioService voluntarioService;


    @PostMapping(value = "/create",
                 consumes = {"multipart/form-data"})
    public ResponseEntity<?> createEventos(
            @RequestPart("request") EventoDTO request,
            @RequestPart("fileBanner") MultipartFile fileBanner,
            @RequestPart("fileImage") MultipartFile fileImage) {

        return ResponseEntity.status(200)
                             .body(eventosService.createEvento(fileBanner, fileImage, request));
    }

//    @PostMapping("{data}/create")
//    public ResponseEntity<?> createEventosMobile(
//            @PathVariable(value = "data") String data,
//            @RequestBody EventoDTO request) throws ParseException {
//
//        data = data.replace("-", "/");
//
//        SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy"); // Formato da string recebida
//
//
//        Date data2 = sdfInput.parse(data);
//
//
//        request.setDate(data2);
//
//        log.info("Create eventos acessado infos:\n{\n titulo:" + request.getTitulo());
//
//        log.info(request.toString());
//
//
//        return ResponseEntity.status(200)
//                .body(eventosService.createEvento(request));
//
//    }


    @PatchMapping(value = "/{id}/edit",
                  consumes = {"multipart/form-data"})
    public EventoResponse updateEventos(
            @PathVariable(value = "id") String id,
            @RequestPart("request") EventoDTO request,
            @RequestPart(value = "fileBanner", required = false)
            MultipartFile fileBanner,
            @RequestPart(value = "fileImage", required = false)
            MultipartFile fileImage) {

        return this.eventosService.updateEventoById(id, fileBanner, fileImage, request);
    }

    @PatchMapping(value = "/{id}/edit/image-banner",
                  consumes = {"multipart/form-data"})
    public EventosResponse updateEventoImageBanner(
            @PathVariable(value = "id") String id,
            @RequestPart(value = "fileBanner", required = false)
            MultipartFile fileBanner,
            @RequestPart(value = "fileImage", required = false)
            MultipartFile fileImage) {

        return this.eventosService.editImageAndBannerEventos(id, fileBanner, fileImage);
    }

    @PatchMapping(value = "/{id}/edit/image",
                  consumes = {"multipart/form-data"})
    public EventosResponse updateEventoImage(
            @PathVariable(value = "id") String id,
            @RequestPart(value = "fileImage", required = false)
            MultipartFile fileImage) {

        return this.eventosService.editImageEventos(id, fileImage);
    }

    @PatchMapping(value = "/{id}/edit/banner",
                  consumes = {"multipart/form-data"})
    public EventosResponse updateBannerEvento(
            @PathVariable(value = "id") String id,
            @RequestPart(value = "fileBanner", required = false)
            MultipartFile fileBanner) {

        return this.eventosService.editBannerEventos(id, fileBanner);
    }

    @DeleteMapping("/{id}/delete")
    public void deleteEvento(@PathVariable(value = "id") String id) {


        System.out.println("evento deletado");
        this.eventosService.deleteEventoById(id);
    }

    @GetMapping("/{idEvento}/list")
    public ResponseEntity<Eventos> listarEventoByIdEvento(
            @PathVariable(value = "idEvento") String idEvento) {
        return ResponseEntity.status(200)
                             .body(this.eventosService.listarEventoById(idEvento));
    }


    @GetMapping("/{idEvento}/arquivar")
    public void arquivarEvento(
            @PathVariable(value = "idEvento") String id) {
        this.eventoArquivadoService.addEventoArquivado(id);
    }

    @GetMapping("/{idEvento}/desarquivar")
    public void desarquivarEvento(
            @PathVariable(value = "idEvento") String idEvento) {
        System.out.println("evento desarquivados");

        this.eventoArquivadoService.retirarEventoArquivado(idEvento);
    }

    @GetMapping("/arquivados")
    public ResponseEntity<?> eventosArquivados() {
        System.out.println("eventos arquivados");

        if (this.eventoArquivadoService.listarEventosArquivados() != null) {
            return ResponseEntity.ok()
                                 .body(this.eventoArquivadoService.listarEventosArquivados());

        }
        return new ResponseEntity<>(new ApiError(400, "Nenhum evento arquivo para listar", new Date()),
                HttpStatus.NO_CONTENT);
    }

    @GetMapping("/asc")
    @Operation(summary = "Listar os eventos como administrador",
               description = "Listar os eventos da comunidade em ordem alfabetica")
    public ResponseEntity<List<EventosVisualizacaoResponse>> getEventsByOrderAsc(
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "page", defaultValue = "0")
            int page) {
        return ResponseEntity.status(200)
                             .body(this.eventosService.listEventoOrdemAlfabetica(PageRequest.of(page, size)));
    }

    @PostMapping("/addDestaque/{id}")
    public ResponseEntity<EventosVisualizacaoResponse> addDestaqueToEvent(
            @PathVariable(value = "id") String id) {
        return ResponseEntity.status(200)
                             .body(this.eventosService.addDestaqueToEvento(id));
    }

    @PostMapping("/removeDestaque/{id}")
    public ResponseEntity<EventosVisualizacaoResponse> removeDestaqueToEvent(
            @PathVariable(value = "id") String id) {
        return ResponseEntity.status(200)
                             .body(this.eventosService.removeDestaqueToEvento(id));
    }

    @GetMapping("/destaques")
    public ResponseEntity<?> listParticipantesEventos() {
        List<EventosVisualizacaoResponse> eventosVisualizacaoResponses = this.eventosService.listEventosDestacados();
        if (!eventosVisualizacaoResponses.isEmpty()) {
            return ResponseEntity.status(200)
                                 .body(eventosVisualizacaoResponses);
        }
        return new ResponseEntity<>(new ApiError(400, "Nenhum evento em destaque", new Date()),
                HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/get/participantes")
    public ResponseEntity<List<Membro>> listParticipantesEventos(
            @PathVariable(value = "id") String id) {
        return ResponseEntity.status(200)
                             .body(this.eventosService.listParticipantesEventos(id));
    }

    @PostMapping("/{id}/despesa/add")
    public ResponseEntity<EventoResponse> adicionarDespesaNoEvento(
            @PathVariable(value = "id") String idEvento,
            @RequestBody DespesaEventoRequest despesaEventoRequest) {

        return ResponseEntity.status(200)
                             .body(this.eventosService.adicionarDespesaInEvento(idEvento, despesaEventoRequest));
    }

    @PutMapping("/{idEvento}/despesa/{idDespesa}/edit")
    public ResponseEntity<String> editarDespesaNoEvento(
            @PathVariable(value = "idEvento") String idEvento,
            @PathVariable(value = "idDespesa") Integer idDespesa,
            @RequestBody
            DespesaEventoRequest despesaEventoRequestNew) {
        return ResponseEntity.status(200)
                             .body(this.eventosService.editarDespesaInEvento(idEvento, idDespesa, despesaEventoRequestNew));
    }

    @DeleteMapping("/{idEvento}/despesa/{idDespesa}/delete")
    public ResponseEntity<String> excluirDespesaNoEvento(
            @PathVariable(value = "idEvento") String idEvento,
            @PathVariable(value = "idDespesa") Integer idDespesa) {
        this.eventosService.excluirDespesaInEvento(idEvento, idDespesa);
        return ResponseEntity.status(200).body("Deleta com sucesso");
    }

    @GetMapping("/{idEvento}/despesa/listAll")
    public ResponseEntity<?> listarTodasDespesasNoEvento(
            @PathVariable(value = "idEvento") String idEvento) {
        List<DespesaEvento> despesas = this.eventosService.listarDespesasInEvento(idEvento);
        if (!despesas.isEmpty()) {
            return ResponseEntity.status(200).body(despesas);
        }
        return new ResponseEntity<>(new ApiError(400, "Nenhuma despesa cadastrada", new Date()), HttpStatus.NO_CONTENT);
    }

    @PostMapping("/confirmar/{idPagamento}/entrada/{idEvento}")
    public ResponseEntity<?> confirmarPagamentoEntrada(
            @PathVariable(value = "idPagamento") String idPagamento,
            @PathVariable(value = "idEvento") String idEvento) {

        if (eventosService.aceitarSolicitacaoPagamento(idPagamento, idEvento)) {
            return ResponseEntity.accepted().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/recusar/{idPagamento}/entrada/{idEvento}")
    public ResponseEntity<?> recusarPagamentoEntrada(
            @PathVariable(value = "idPagamento") String idPagamento,
            @PathVariable(value = "idEvento") String idEvento) {

        if (eventosService.recusarSolicitacaoPagamento(idPagamento, idEvento)) {
            return ResponseEntity.accepted().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/AdicionarDescontoParaMembro/{id}")
    public ResponseEntity<Boolean> AdicionaDescontoMembro(
            @PathVariable(value = "id") String idEvento,
            Double valorDesconto) {
        System.out.println(valorDesconto + idEvento);
        return ResponseEntity.ok()
                             .body(eventosService.adicionarDescontoParaMembro(idEvento, valorDesconto));
    }

    @PostMapping("/AdicionarDescontoAssociado/{id}")
    public ResponseEntity<Boolean> AdicionaDescontoAssociado(
            @PathVariable(value = "id") String idEvento,
            Double valorDesconto) {
        return ResponseEntity.ok()
                             .body(eventosService.adicionarDescontoParaAssociado(idEvento, valorDesconto));
    }

    @PostMapping("/AlterarValorEvento/{id}")
    public ResponseEntity<Boolean> AlteraValorEvento(
            @PathVariable(value = "id") String idEvento,
            Double valorEvento) {
        return ResponseEntity.ok()
                             .body(eventosService.AlterarValorEvento(idEvento, valorEvento));
    }

    @GetMapping("{id}/listVoluntarios")
    public ResponseEntity<List<SeVoluntariarEventoResponse>> listAllVoluntarios(
            @PathVariable(value = "id") String idEvento) {
        return ResponseEntity.ok()
                             .body(voluntarioService.listAllVoluntarios(idEvento));
    }


}
