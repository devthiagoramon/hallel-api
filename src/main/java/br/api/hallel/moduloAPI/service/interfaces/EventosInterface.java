package br.api.hallel.moduloAPI.service.interfaces;

import br.api.hallel.moduloAPI.dto.v1.evento.EventoDTO;
import br.api.hallel.moduloAPI.dto.v1.evento.EventoResponse;
import br.api.hallel.moduloAPI.dto.v1.evento.EventosPublicResponse;
import br.api.hallel.moduloAPI.dto.v1.ministerio.EventosShortResponse;
import br.api.hallel.moduloAPI.financeiroNovo.model.StatusEntradaEvento;
import br.api.hallel.moduloAPI.financeiroNovo.payload.request.PagamentoEntradaEventoReq;
import br.api.hallel.moduloAPI.model.*;
import br.api.hallel.moduloAPI.payload.requerimento.DespesaEventoRequest;
import br.api.hallel.moduloAPI.payload.requerimento.InscreverEventoRequest;
import br.api.hallel.moduloAPI.payload.resposta.EventosResponse;
import br.api.hallel.moduloAPI.payload.resposta.EventosVisualizacaoResponse;
import br.api.hallel.moduloAPI.payload.resposta.LocalEventoResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventosInterface {

    List<EventosVisualizacaoResponse> listarAllEventos(int page, int size   );

    Eventos listarEventoById(String id);

    EventoResponse listarEventosByTitulo(String nome);

    Eventos createEvento(MultipartFile fileBanner, MultipartFile fileImage, EventoDTO evento);

    EventoResponse updateEventoById(String id, MultipartFile fileBanner, MultipartFile fileImage, EventoDTO request);

    void deleteEventoById(String id);

    EventosResponse editImageAndBannerEventos(String idEvento, MultipartFile fileBanner, MultipartFile fileImage);

    EventosResponse editImageEventos(String idEvento, MultipartFile fileImage);

    EventosResponse editBannerEventos(String idEvento, MultipartFile fileBanner);

    Boolean inscreverEvento(InscreverEventoRequest inscreverEventoRequest);

    List<EventosVisualizacaoResponse> listEventoOrdemAlfabetica(
            PageRequest pageRequest);

    List<Membro> listParticipantesEventos(String id);

    EventosVisualizacaoResponse addDestaqueToEvento(String idEvento);

    EventosVisualizacaoResponse removeDestaqueToEvento(String idEvento);

    List<EventosVisualizacaoResponse> listEventosDestacados();

    EventoResponse adicionarDespesaInEvento(String idEvento, DespesaEventoRequest despesaEvento);

    String editarDespesaInEvento(String idEvento, Integer idDespesaEvento, DespesaEventoRequest despesaEvento);

    void excluirDespesaInEvento(String idEvento, Integer idDespesaEvento);

    List<DespesaEvento> listarDespesasInEvento(String idEvento);

    List<EventosVisualizacaoResponse> listEventosSemDestaqueToVisualizar();

    List<EventosVisualizacaoResponse> listEventosDestacadosToVisualizar();

    List<EventosVisualizacaoResponse> listByPage(int page);
    List<EventosVisualizacaoResponse> listarEventosByName(String titulo);

    Boolean solicitarPagamentoEntrada(PagamentoEntradaEventoReq request, Membro membro, Eventos eventos);

    Boolean aceitarSolicitacaoPagamento(String idSolicitacaoPagamento, String idEvento);

    Boolean recusarSolicitacaoPagamento(String idSolicitacaoPagamento, String idEvento);

    Boolean verificarIsInscrito(String idEvento, String idUser);

    StatusEntradaEvento verificarSituacaoMembroEmEvento(String idEvento, String emailMembro);

    List<EventoResponse> listarEventosInscritos(String iduser);


    EventosPublicResponse listarEventoPublicById(String idEvento);

    EventosShortResponse listEventoInEscalaInfo(String idEvento);
    List<LocalEventoResponse> listLocalEvento();

}
