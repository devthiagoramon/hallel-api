package br.api.hallel.moduloAPI.service.eventos;

import br.api.hallel.moduloAPI.dto.v1.evento.EventoDTO;
import br.api.hallel.moduloAPI.dto.v1.evento.EventoResponse;
import br.api.hallel.moduloAPI.dto.v1.evento.EventosPublicResponse;
import br.api.hallel.moduloAPI.dto.v1.ministerio.EscalaMinisterioResponse;
import br.api.hallel.moduloAPI.dto.v1.ministerio.EventosShortResponse;
import br.api.hallel.moduloAPI.exceptions.eventos.EventoIllegalArumentException;
import br.api.hallel.moduloAPI.exceptions.eventos.EventoNotFoundException;
import br.api.hallel.moduloAPI.exceptions.eventos.EventosEditImageException;
import br.api.hallel.moduloAPI.financeiroNovo.model.EntradasFinanceiro;
import br.api.hallel.moduloAPI.financeiroNovo.model.PagamentoEntradaEvento;
import br.api.hallel.moduloAPI.financeiroNovo.model.StatusEntradaEvento;
import br.api.hallel.moduloAPI.financeiroNovo.payload.request.PagamentoEntradaEventoReq;
import br.api.hallel.moduloAPI.financeiroNovo.repository.PagamentoEntradaEventoRepository;
import br.api.hallel.moduloAPI.financeiroNovo.service.PagamentoEntradaEventoService;
import br.api.hallel.moduloAPI.mapper.evento.EventoMapper;
import br.api.hallel.moduloAPI.model.*;
import br.api.hallel.moduloAPI.payload.requerimento.DespesaEventoRequest;
import br.api.hallel.moduloAPI.payload.requerimento.InscreverEventoRequest;
import br.api.hallel.moduloAPI.payload.requerimento.SeVoluntariarEventoReq;
import br.api.hallel.moduloAPI.payload.resposta.*;
import br.api.hallel.moduloAPI.repository.AssociadoRepository;
import br.api.hallel.moduloAPI.repository.EventosRepository;
import br.api.hallel.moduloAPI.repository.LocalEventoRepository;
import br.api.hallel.moduloAPI.repository.MembroRepository;
import br.api.hallel.moduloAPI.service.google.GoogleBucketService;
import br.api.hallel.moduloAPI.service.interfaces.EventosInterface;
import br.api.hallel.moduloAPI.service.main.MembroService;
import br.api.hallel.moduloAPI.service.ministerio.EscalaService;
import br.api.hallel.moduloAPI.service.ministerio.MinisterioService;
import br.api.hallel.moduloAPI.utils.GoogleBucketUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class EventosService implements EventosInterface {

    @Autowired
    private EventosRepository repository;
    @Autowired
    private PagamentoEntradaEventoRepository pagamentoEntradaEventoRepository;
    @Autowired
    private MembroService membroService;
    @Autowired
    private MembroRepository membroRepository;

    @Autowired
    private AssociadoRepository associadoRepository;
    @Autowired
    private LocalEventoRepository localEventoRepository;
    @Autowired
    private PagamentoEntradaEventoService pagamentoEntradaService;
    @Autowired
    private MinisterioService ministerioService;
    @Autowired
    private EscalaService escalaService;
    @Autowired
    private GoogleBucketService bucketService;

    //CRIA EVENTOS
    @Override
    public Eventos createEvento(MultipartFile fileBanner,
                                MultipartFile fileImage,
                                EventoDTO dto) {

        if (dto.getTitulo() == null || dto.getTitulo()
                                          .isEmpty()) {
            throw new EventoIllegalArumentException("Não foi possível criar o evento.");
        }
        log.info("Creating evento...");


        Eventos eventos = this.repository.insert(EventoMapper.dtoToEvento(dto));

        if ((fileImage != null && !(fileImage.isEmpty())) && (fileBanner != null && !(fileBanner.isEmpty()))) {

            String imageUrl = null;
            String bannerImageUrl = null;
            try {
                imageUrl = bucketService.sendImageToBucket(fileImage, GoogleBucketUtils
                        .getImageName(
                                eventos.getId(),
                                Eventos.class.getSimpleName(),
                                "image"));
                bannerImageUrl = bucketService.sendImageToBucket(fileBanner, GoogleBucketUtils
                        .getImageName(
                                eventos.getId(),
                                Eventos.class.getSimpleName(),
                                "banner"));

                eventos.setFileImageUrl(imageUrl);
                eventos.setBanner(bannerImageUrl);
                log.info(eventos.getFileImageUrl());
                log.info(eventos.getBanner());
                eventos = this.repository.save(eventos);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {

            if (eventos.getMinisteriosAssociados() != null && !eventos.getMinisteriosAssociados()
                                                                      .isEmpty()) {
                for (String ministerioId : eventos.getMinisteriosAssociados()) {
                    EscalaMinisterioResponse escalaMinisterio = escalaService.createEscalaMinisterio(eventos, ministerioId);
                    if (eventos.getEscalasMinisterio() == null) {
                        List<EscalaMinisterioResponse> escalasMinisterios = new ArrayList<>();
                        escalasMinisterios.add(escalaMinisterio);
                        eventos.setEscalasMinisterio(escalasMinisterios);
                    } else {
                        eventos.getEscalasMinisterio()
                               .add(escalaMinisterio);
                    }
                    eventos = this.repository.save(eventos);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return eventos;

    }

    public Eventos createEvento(Eventos evento) {
        log.info("EVENT0 CRIADO!");
        return this.repository.insert(evento);
    }

    //LISTA OS EVENTOS JÁ CRIADOS
    @Override
    public List<EventosVisualizacaoResponse> listarAllEventos(
            int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Eventos> eventosPaginados = this.repository.findAll(pageable);
        List<EventosVisualizacaoResponse> listaResponse = eventosPaginados.stream()
                                                                          .map(evento -> new EventosVisualizacaoResponse().toListEventosResponse(evento))
                                                                          .collect(Collectors.toList());

        log.info("Eventos listados! ");

        return listaResponse;
    }

    public List<String> listAllEventosId() {
        List<String> stringList = this.repository.findAll().stream()
                                                 .map(Eventos::getId)
                                                 .collect(Collectors.toList());
        return stringList;
    }

    //LISTA APENAS UM EVENTO PELO SEU ID
    @Override
    public Eventos listarEventoById(String id) {
        Optional<Eventos> optional = this.repository.findById(id);

        if (optional.isPresent()) {
            Eventos eventos = optional.get();
            log.info("Evento listado");
            //String url = bucketService.updateImageOfBucket();
            return eventos;
        }

        throw new EventoNotFoundException("Evento id (" + id + ")não encontrado!");
    }

    //LISTAR EVENTOS NA HOMEPAGE
    @Override
    public List<EventosVisualizacaoResponse> listEventosSemDestaqueToVisualizar() {
        List<EventosVisualizacaoResponse> listaResponse = new ArrayList<>();
        this.repository.findAll().forEach(eventos -> {
            if (eventos.getDestaque() == null) {
                listaResponse.add(new EventosVisualizacaoResponse().toListEventosResponse(eventos));
            } else {
                if (!eventos.getDestaque()) {
                    listaResponse.add(new EventosVisualizacaoResponse().toListEventosResponse(eventos));
                }
            }
        });
        return listaResponse;
    }

    @Override
    public List<EventosVisualizacaoResponse> listEventosDestacadosToVisualizar() {
        List<EventosVisualizacaoResponse> listaResponse = new ArrayList<>();
        this.repository.findAll().forEach(eventos -> {
            if (eventos.getDestaque() != null) {
                if (eventos.getDestaque()) {
                    listaResponse.add(new EventosVisualizacaoResponse().toListEventosResponse(eventos));
                }
            }
        });
        return listaResponse;
    }

    //Listar eventos por página
    @Override
    public List<EventosVisualizacaoResponse> listByPage(int page) {
        List<EventosVisualizacaoResponse> listaResponse = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, 15);

        for (Eventos eventos : this.repository.findAll(pageable)) {
            listaResponse.add(new EventosVisualizacaoResponse().toListEventosResponse(eventos));
        }

        log.info("Eventos para visualização listados!");

        return listaResponse;
    }

    @Override
    public List<EventosVisualizacaoResponse> listarEventosByName(
            String titulo) {
        log.info("Searching events by titulo that contains: {}", titulo);
        return this.repository.findAllByTituloContainingIgnoreCase(titulo);
    }

//    public void preencherFormularioInscricaoComBanco(Membro membro, String idEvento) {
//
//        InscreverEventoRequest request = new InscreverEventoRequest();
//        request.setIdEvento(idEvento);
//        request.setEmail(membro.getEmail());
//        request.setNome(membro.getNome());
//        request.setIdade(membro.getIdade());
//
//        log.info("Linha 154");
//        if (membro.getCpf() != null) {
//            if (!membro.getCpf().isEmpty()) {
//                log.info(membro.getCpf());
//                request.setCpf(membro.getCpf());
//                log.info("Linha 157");
//            }
//        }
//
//        Date date = new Date();
//        LocalDate localDate = date.toInstant().atZone(ZoneId.of("America/Puerto_Rico")).toLocalDate();
//
//        request.setAno(localDate.getYear());
//        request.setMes(localDate.getMonthValue());
//
//        inscreverEvento(request);
//    }

    //Solicitar a entrada do evento (precisa pagar a entrada)


    //Administrador aceita o pagamento realizado pelo membro que deseja participar do evento
    @Override
    public Boolean aceitarSolicitacaoPagamento(
            String idSolicitacaoPagamento, String idEvento) {
        PagamentoEntradaEvento pagamento = this.pagamentoEntradaEventoRepository.findById(idSolicitacaoPagamento)
                                                                                .isPresent() ?
                this.pagamentoEntradaEventoRepository.findById(idSolicitacaoPagamento)
                                                     .get() : null;

        Eventos eventos = this.repository.findById(idEvento)
                                         .isPresent() ?
                this.repository.findById(idEvento).get() : null;

        EntradasFinanceiro entradaOld = new EntradasFinanceiro();
        entradaOld.setDate(pagamento.getDate());
        entradaOld.setMetodoPagamento(pagamento.getMetodoPagamento());
        entradaOld.setValor(pagamento.getValor());
        entradaOld.setCodigo(pagamento.getCodigo());

        EntradasFinanceiro entradaNew = new EntradasFinanceiro();
        for (PagamentoEntradaEvento pagamentoEntrada : eventos.getPagamentoEntradaEventoList()) {
            entradaNew.setDate(pagamentoEntrada.getDate());
            entradaNew.setMetodoPagamento(pagamentoEntrada.getMetodoPagamento());
            entradaNew.setValor(pagamentoEntrada.getValor());
            entradaNew.setCodigo(pagamentoEntrada.getCodigo());

            if (entradaOld.equals(entradaNew)) {

                int index = eventos.getPagamentoEntradaEventoList()
                                   .indexOf(entradaOld);
                eventos.getPagamentoEntradaEventoList()
                       .set(index, new PagamentoEntradaEventoReq().toPag(pagamentoEntrada));

                break;
            }
        }

        this.pagamentoEntradaEventoRepository.save(pagamento);
        this.repository.save(eventos);
        return true;
    }

    @Override
    public Boolean recusarSolicitacaoPagamento(
            String idSolicitacaoPagamento, String idEvento) {
        PagamentoEntradaEvento pagamento = this.pagamentoEntradaEventoRepository.findById(idSolicitacaoPagamento)
                                                                                .isPresent() ?
                this.pagamentoEntradaEventoRepository.findById(idSolicitacaoPagamento)
                                                     .get() : null;

        Eventos eventos = this.repository.findById(idEvento)
                                         .isPresent() ?
                this.repository.findById(idEvento).get() : null;

        EntradasFinanceiro entradaOld = new EntradasFinanceiro();
        entradaOld.setDate(pagamento.getDate());
        entradaOld.setMetodoPagamento(pagamento.getMetodoPagamento());
        entradaOld.setValor(pagamento.getValor());
        entradaOld.setCodigo(pagamento.getCodigo());

        EntradasFinanceiro entradaNew = new EntradasFinanceiro();
        for (PagamentoEntradaEvento pagamentoEntrada : eventos.getPagamentoEntradaEventoList()) {
            entradaNew.setDate(pagamentoEntrada.getDate());
            entradaNew.setMetodoPagamento(pagamentoEntrada.getMetodoPagamento());
            entradaNew.setValor(pagamentoEntrada.getValor());
            entradaNew.setCodigo(pagamentoEntrada.getCodigo());

            if (entradaOld.equals(entradaNew)) {

                int index = eventos.getPagamentoEntradaEventoList()
                                   .indexOf(entradaOld);
                eventos.getPagamentoEntradaEventoList()
                       .set(index, new PagamentoEntradaEventoReq().toPag(pagamentoEntrada));

                break;
            }
        }

        this.pagamentoEntradaEventoRepository.save(pagamento);
        this.repository.save(eventos);
        return true;
    }

    @Override
    public Boolean verificarIsInscrito(String idEvento,
                                       String idUser) {
        Optional<Eventos> optional = this.repository.findById(idEvento);
        boolean isInscrito = false;
        if (optional.isPresent()) {
            Eventos evento = optional.get();
            if (evento.getIntegrantes() != null) {
                for (Membro integrante : evento.getIntegrantes()) {
                    if (integrante.getId().equals(idUser)) {
                        isInscrito = true;
                    }
                }
            }
        }
        return isInscrito;
    }

    @Override
    public StatusEntradaEvento verificarSituacaoMembroEmEvento(
            String idEvento, String emailMembro) {

        Optional<PagamentoEntradaEvento> optional = pagamentoEntradaEventoRepository.findByIdEventos(idEvento);
        StatusEntradaEvento statusEntradaEvento = null;
        if (optional.isPresent()) {
            PagamentoEntradaEvento pagamentoEntradaEvento = optional.get();
            if (pagamentoEntradaEvento.getEmailMembroPagador()
                                      .equals(emailMembro)) {

            }
        }
        return statusEntradaEvento;
    }

    @Override
    public List<EventoResponse> listarEventosInscritos(
            String iduser) {
//        List<EventosResponse> eventosResponses = new ArrayList<>();
//        for (Eventos eventos : this.repository.findAll()) {
//            if (eventos.getIntegrantes() != null) {
//                for (Membro integrante : eventos.getIntegrantes()) {
//                    if (integrante.getId().equals(iduser)) {
//                        eventosResponses.add(new EventosResponse().toEventosResponse(eventos));
//                    }
//                }
//            }
//        }
//        return eventosResponses;
        return null;
    }

    @Override
    public EventosPublicResponse listarEventoPublicById(
            String idEvento) {
        log.info("List event in public by id " + idEvento);
        Optional<Eventos> optional = this.repository.findById(idEvento);
        if (optional.isEmpty()) {
            throw new EventoIllegalArumentException("Can't find evento by this id");
        }
        return EventoMapper.modelToPublicResponse(optional.get());
    }

    @Override
    public EventosShortResponse listEventoInEscalaInfo(
            String idEvento) {
        log.info("Listing evento in escala info by id {}", idEvento);
        Optional<EventosShortResponse> optional = this.repository.findByIdShort(idEvento);
        if (optional.isEmpty()) {
            throw new EventoIllegalArumentException("Can't find evento by this id");
        }

        return optional.get();
    }

    @Override
    public List<LocalEventoResponse> listLocalEvento() {
        List<LocalEvento> localEventos = localEventoRepository.findAll();
        List<LocalEventoResponse> response = new ArrayList<>();
        for (LocalEvento localEvento : localEventos) {
            response.add(new LocalEventoResponse().toLocalEventoReponse(localEvento));
        }
        log.info("Locais listados! " + response.toString());
        return response;
    }

    //LISTA O EVENTO PELO SEU NOME
    @Override
    public EventoResponse listarEventosByTitulo(String nome) {
        Optional<Eventos> optional = this.repository.findByTitulo(nome);

        if (optional.isPresent()) {
            Eventos eventos = optional.get();

            log.info("Evento listado");
            return EventoMapper.modelToResponse(eventos);
        }

        return null;
    }

    //ATUALIZA AS INFORMAÇÕES DO EVENTO
    @Override
    public EventoResponse updateEventoById(String id,
                                           MultipartFile fileBanner,
                                           MultipartFile fileImage,
                                           EventoDTO request) {


        Eventos eventoOld = this.listarEventoById(id);

        eventoOld.setId(id);
        eventoOld.setTitulo(request.getTitulo());
        eventoOld.setDescricao(request.getDescricao());
        eventoOld.setDate(request.getDate());
        eventoOld.setLocalEvento(request.getLocalEvento());
        eventoOld.setDestaque(request.getDestaque());

        if ((fileImage != null && !fileImage.isEmpty())
                && (fileBanner != null && !fileBanner.isEmpty())) {
            String imageUrl = null;
            String bannerImageUrl = null;
            try {
                imageUrl = bucketService.updateImageOfBucket(fileImage, GoogleBucketUtils
                        .getImageName(
                                eventoOld.getId(),
                                Eventos.class.getSimpleName(),

                                "image"));
                bannerImageUrl = bucketService.updateImageOfBucket(fileBanner, GoogleBucketUtils
                        .getImageName(
                                eventoOld.getId(),
                                Eventos.class.getSimpleName(),
                                "banner"));
                eventoOld.setFileImageUrl(imageUrl);
                eventoOld.setBanner(bannerImageUrl);
                log.info(eventoOld.getFileImageUrl());
                log.info(eventoOld.getBanner());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (request.getMinisteriosAssociados() != null) {
            eventoOld.setMinisteriosAssociados(request.getMinisteriosAssociados());
        } else {
            eventoOld.setMinisteriosAssociados(null);
        }
        if (request.getPalestrantes() != null) {
            eventoOld.setPalestrantes(request.getPalestrantes());
        }

        if (eventoOld.getMinisteriosAssociados() != null) {
            for (EscalaMinisterioResponse escalaMinisterio : eventoOld.getEscalasMinisterio()) {
                if (eventoOld.getMinisteriosAssociados()
                             .contains(escalaMinisterio.getMinisterioId())) {
                    this.escalaService.editarDataEscalaMinisterio(escalaMinisterio.getId(), eventoOld.getDate());
                }
            }
        }


        Eventos eventosResponse = this.repository.save(eventoOld);

        log.info("Evento Atualizado!");

        return EventoMapper.modelToResponse(eventosResponse);
    }

    //REMOVE UM EVENTO
    @Override
    public void deleteEventoById(String id) {

        if (listarEventoById(id) != null) {
            try {
                this.escalaService.deleteEscalasWithDeletingEvento(id);

                Eventos evento = repository.findById(id).get();
                String fileName = evento.getFileImageUrl()
                                        .substring(evento.getFileImageUrl()
                                                         .lastIndexOf("/") + 1, evento.getFileImageUrl()
                                                                                      .indexOf("?"));
                String bannerName = evento.getBanner()
                                          .substring(evento.getBanner()
                                                           .lastIndexOf("/") + 1, evento.getBanner()
                                                                                        .indexOf("?"));

                if (bucketService.deleteImageOfBucket(fileName)) {
                    log.info("Image '{}' deletada com sucesso do Bucket! ", fileName);
                } else {
                    log.warn("Imagem '{}' não encontrada no bucket", fileName);
                }
                if (bucketService.deleteImageOfBucket(bannerName)) {
                    log.info("Image '{}' deletada com sucesso do Bucket! ", bannerName);
                } else {
                    log.warn("Imagem '{}' não encontrada no bucket", bannerName);
                }

            } catch (Exception e) {
                log.error(e.getMessage());
            }
            this.repository.deleteById(id);
            log.info("EVENTO DELETADO!");
        } else {
            throw new EventoNotFoundException("Evento não econtrado");
        }
    }

    @Override
    public EventosResponse editImageAndBannerEventos(String idEvento,
                                                     MultipartFile fileBanner,
                                                     MultipartFile fileImage) {
        Optional<Eventos> eventosOptional = repository.findById(idEvento);
        if (eventosOptional.isEmpty()) {
            throw new EventoNotFoundException("Evento não encontrado!");
        }

        Eventos evento = repository.findById(idEvento).get();
        if ((fileBanner != null && !fileBanner.isEmpty()) && (fileImage != null && !fileImage.isEmpty())) {
            try {
                String imageUrl = bucketService.updateImageOfBucket(fileImage, "evento-%s-image.%s".formatted(
                        evento.getId(), Objects.requireNonNull(fileImage.getContentType())
                                               .substring(fileImage.getContentType()
                                                                   .indexOf("/") + 1)));
                String bannerImageUrl = bucketService.updateImageOfBucket(fileBanner, "evento-%s-banner.%s".formatted(
                        evento.getId(), Objects.requireNonNull(fileBanner.getContentType())
                                               .substring(fileBanner.getContentType()
                                                                    .indexOf("/") + 1)));
                evento.setFileImageUrl(imageUrl);
                evento.setBanner(bannerImageUrl);
                repository.save(evento);
                log.info("Evento Atualizado!");
                return new EventosResponse();
            } catch (IOException e) {
                throw new EventosEditImageException(e + "- Não foi possível atualizar a imagem e o banner do evento!");
            }
        } else {
            throw new EventosEditImageException("Imagem não pode ser nula!");
        }
    }

    @Override
    public EventosResponse editImageEventos(String idEvento,
                                            MultipartFile fileImage) {
        Optional<Eventos> eventosOptional = repository.findById(idEvento);
        if (eventosOptional.isEmpty()) {
            throw new EventoNotFoundException("Evento não encontrado!");
        }

        Eventos evento = repository.findById(idEvento).get();
        if (fileImage != null && !fileImage.isEmpty()) {
            try {
                String fileImageUrl = bucketService.updateImageOfBucket(fileImage, idEvento + "_image");
                evento.setFileImageUrl(fileImageUrl);
                repository.save(evento);
                log.info("Evento Atualizado!");
                return new EventosResponse();
            } catch (IOException e) {
                throw new EventosEditImageException(e + "- Não foi possível  atualizar a imagem do evento!");
            }
        } else {
            throw new EventosEditImageException("Imagem não pode ser nula!");
        }
    }

    @Override
    public EventosResponse editBannerEventos(String idEvento,
                                             MultipartFile fileBanner) {
        Optional<Eventos> eventosOptional = repository.findById(idEvento);
        if (eventosOptional.isEmpty()) {
            throw new EventoNotFoundException("Evento não encontrado!");
        }

        Eventos evento = repository.findById(idEvento).get();
        if (fileBanner != null && !fileBanner.isEmpty()) {
            try {
                String fileBannerUrl = bucketService.updateImageOfBucket(fileBanner, idEvento + "_banner");
                evento.setBanner(fileBannerUrl);
                repository.save(evento);
                log.info("Evento Atualizado!");
                return new EventosResponse();
            } catch (IOException e) {
                throw new EventosEditImageException(e + "- Não foi possível atualizar o banner do evento!");
            }
        } else {
            throw new EventosEditImageException("Bnner não pode ser nulo!");

        }
    }

    //ADICIONA UM MEMBRO AO EVENTO
    @Override
    public Boolean inscreverEvento(
            InscreverEventoRequest inscreverEventoRequest) {
        Date date = new Date();
        inscreverEventoRequest.setDataInscricao(date);
        Optional<Eventos> eventosOptional = this.repository.findById(inscreverEventoRequest.getIdEvento());

        if (eventosOptional.isPresent()) {
            Eventos evento = eventosOptional.get();

            // Adicionar o membro ao evento apenas uma vez
            Membro membroNovo = null;
            if (inscreverEventoRequest.isAssociado()) {
                Optional<Associado> optional = this.associadoRepository.findById(inscreverEventoRequest.getId());
                Associado associado = optional.orElse(null);
                membroNovo = associado;
            } else if (inscreverEventoRequest.isMembro()) {
                membroNovo = this.membroService.listMembroId(inscreverEventoRequest.getId());
            } else {
                membroNovo = inscreverEventoRequest.toMembroEvento();
            }

            if (evento.getIntegrantes() == null) {
                evento.setIntegrantes(new ArrayList<>());
            }

            // Verificar se o membro já está na lista de integrantes do evento
            if (!evento.getIntegrantes().contains(membroNovo)) {
                evento.getIntegrantes().add(membroNovo);


                ValorEventoResponse response = this.InformacoesValorEvento(evento.getId(), membroNovo.getId());
                // Solicitar pagamento de entrada apenas se o membro não estiver participando do evento
                inscreverEventoRequest.setValorPago(response.getValorEventoComDesconto());

                PagamentoEntradaEventoReq pagamentoEntradaEventoReq = new PagamentoEntradaEventoReq().toPagamentoEntradaEventoReq(inscreverEventoRequest);
                this.solicitarPagamentoEntrada(pagamentoEntradaEventoReq, membroNovo, evento);

                return true;
            }

            // Membro já está participando do evento, não é necessário adicionar novamente
            return false;
        }

        throw new EventoNotFoundException("Evento não encontrado.");
    }

    @Override
    public Boolean solicitarPagamentoEntrada(
            PagamentoEntradaEventoReq request, Membro membro,
            Eventos eventos) {
        adicionarPagamentoEntradaEvento(request, eventos);
        adicionarMembro(eventos, membro);
        adicionarEventoParticipando(membro, eventos);
        System.out.println("funcionooooooou!");
        salvarEventos(eventos);
        atualizarMembro(membro);
        cadastrarPagamentoEntrada(request);
        return true;
    }

    private void adicionarPagamentoEntradaEvento(
            PagamentoEntradaEventoReq request, Eventos eventos) {
        if (eventos.getPagamentoEntradaEventoList() == null) {
            eventos.setPagamentoEntradaEventoList(new ArrayList<>());
        }
        eventos.getPagamentoEntradaEventoList()
               .add(request.toPagamentoEntradaEvento());

    }

    private void adicionarMembro(Eventos eventos, Membro membro) {
        if (eventos.getIntegrantes() == null) {
            eventos.setIntegrantes(new ArrayList<>());
        }
        eventos.getIntegrantes().add(membro);
    }

    private void adicionarEventoParticipando(Membro membro,
                                             Eventos eventos) {
        if (membro.getEventosParticipando() == null) {
            membro.setEventosParticipando(new ArrayList<>());
        }
        membro.getEventosParticipando().add(eventos.getId());
    }


    private void salvarEventos(Eventos eventos) {

        repository.save(eventos);


    }

    private void atualizarMembro(Membro membro) {
        membroService.updateMembro(membro.getId(), membro);
    }

    private void cadastrarPagamentoEntrada(
            PagamentoEntradaEventoReq request) {

        pagamentoEntradaService.cadastrar(request);
    }


    public Boolean InscreverVoluntarioEmEvento(
            SeVoluntariarEventoReq seVoluntariar) {
        Optional<Eventos> eventosOptional = this.repository.findById(seVoluntariar.getIdEvento());

        if (eventosOptional.isPresent()) {
            Eventos evento = eventosOptional.get();
            VoluntarioEvento voluntarioNovo = seVoluntariar.toVoluntarioEvento();

            if (evento.getVoluntarios() == null) {
                evento.setVoluntarios(new ArrayList<>());
            }


            System.out.println("voluntario :" + voluntarioNovo.toString());
            evento.getVoluntarios().add(voluntarioNovo);
            this.repository.save(evento); // Salvar as alterações no evento de volta ao repositório

            return true;
        }

        throw new EventoNotFoundException("Evento não encontrado.");
    }


    //Lista eventos por ordem alfabética
    @Override
    public List<EventosVisualizacaoResponse> listEventoOrdemAlfabetica(
            PageRequest pageRequest) {

        List<EventosVisualizacaoResponse> listResponse = new ArrayList<>();

        this.repository.findAllByOrderByTituloAsc(pageRequest)
                       .forEach(eventos -> {
                           listResponse.add(new EventosVisualizacaoResponse().toListEventosResponse(eventos));
                       });

        log.info("Listando eventos em ordem alfabetica");
        return listResponse;
    }

    //Lista membros de um evento
    @Override
    public List<Membro> listParticipantesEventos(String id) {

        Eventos eventosResponse = this.listarEventoById(id);

        log.info("Listando participantes de um evento");

        if (eventosResponse.getIntegrantes() != null) {
            log.info("Listando todos os integrantes");

            return eventosResponse.getIntegrantes();
        } else {
            log.info("Nenhum participante para listar");

            return null;
        }
    }

    //Eventos em Destaque na HomePage
    @Override
    public EventosVisualizacaoResponse addDestaqueToEvento(
            String idEvento) {

        if (!listarEventoById(idEvento).getDestaque()) {
            Eventos request = this.repository.findById(idEvento)
                                             .get();
            request.setId(idEvento);
            request.setDestaque(true);

            Eventos eventosResponse = this.listarEventoById(idEvento) != null ?
                    this.repository.save(request) : null;

            return new EventosVisualizacaoResponse().toListEventosResponse(eventosResponse);
        } else {
            log.warn("Evento já está em destaque");
            return null;
        }

    }

    //Remover esse destaque da HomePage
    @Override
    public EventosVisualizacaoResponse removeDestaqueToEvento(
            String idEvento) {

        if (listarEventoById(idEvento).getDestaque()) {
            Eventos request = this.repository.findById(idEvento)
                                             .get();
            request.setId(idEvento);
            request.setDestaque(false);

            Eventos eventosResponse = this.listarEventoById(idEvento) != null ?
                    this.repository.save(request) : null;

            return new EventosVisualizacaoResponse().toListEventosResponse(eventosResponse);
        } else {

            log.warn("Evento já está sem destaque");
            return null;
        }
    }

    //Listar os eventos em Destaque
    @Override
    public List<EventosVisualizacaoResponse> listEventosDestacados() {

        List<EventosVisualizacaoResponse> responseList = new ArrayList<>();

        this.repository.findAllByDestaqueEquals(true)
                       .forEach(eventos -> {
                           responseList.add(new EventosVisualizacaoResponse().toListEventosResponse(eventos));
                       });

        log.info("Listando eventos em destaque");

        return responseList;
    }

    //Adicionar despesas de um evento
    @Override
    public EventoResponse adicionarDespesaInEvento(String idEvento,
                                                   DespesaEventoRequest despesaEvento) {
        Eventos evento = listarEventoById(idEvento);
        DespesaEvento despesaEventoObj = despesaEvento.toDespesaEvento();
        if (evento.getDespesas() == null) {
            evento.setDespesas(new ArrayList<>());
        }
        despesaEventoObj.setId(evento.getDespesas().size() + 1);
        evento.getDespesas().add(despesaEventoObj);
        this.repository.save(evento);

        return EventoMapper.modelToResponse(evento);
    }

    //Editar a despesa desse evento
    @Override
    public String editarDespesaInEvento(String idEvento,
                                        Integer idDespesaEvento,
                                        DespesaEventoRequest despesaEvento) {
        Eventos evento = listarEventoById(idEvento);
        DespesaEvento despesaEventoObj = despesaEvento.toDespesaEvento();

        /**
         * Pegar o index no array de despesas da despesa que for alterar
         * */
        int indexDespesa = -1;

        for (DespesaEvento despesaEventoInterator : evento.getDespesas()) {
            if (Objects.equals(idDespesaEvento, despesaEventoInterator.getId())) {
                indexDespesa = evento.getDespesas()
                                     .indexOf(despesaEventoInterator);
            }
        }

        DespesaEvento despesaEventoObjOld = evento.getDespesas()
                                                  .get(indexDespesa);

        despesaEventoObjOld.setNome(despesaEventoObj.getNome());
        despesaEventoObjOld.setTipoDespesa(despesaEventoObj.getTipoDespesa());
        despesaEventoObjOld.setDescricao(despesaEventoObj.getDescricao());
        despesaEventoObjOld.setQuantidade(despesaEventoObj.getQuantidade());
        despesaEventoObjOld.setValor(despesaEventoObj.getValor());

        evento.getDespesas().set(indexDespesa, despesaEventoObjOld);

        this.repository.save(evento);

        log.info("Despesa de id " + idDespesaEvento + " evento " + evento.getTitulo() + " editado com sucesso");

        return "Despesa de id " + idDespesaEvento + " evento " + evento.getTitulo() + " editado com sucesso";
    }

    //Remover a despesa desse evento
    @Override
    public void excluirDespesaInEvento(String idEvento,
                                       Integer idDespesaEvento) {
        Eventos evento = listarEventoById(idEvento);

        int indexDespesa = -1;

        for (DespesaEvento despesaEvento : evento.getDespesas()) {
            if (Objects.equals(idDespesaEvento, despesaEvento.getId())) {
                indexDespesa = evento.getDespesas()
                                     .indexOf(despesaEvento);
            }
        }

        if (indexDespesa == -1) {
            log.info("Despesa com id: " + idDespesaEvento + ", inexistente");
            return;
        }

        evento.getDespesas().remove(indexDespesa);
        this.repository.save(evento);
    }

    //Listar todas as despesas do evento
    @Override
    public List<DespesaEvento> listarDespesasInEvento(
            String idEvento) {
        Eventos evento = listarEventoById(idEvento);
        return evento.getDespesas();
    }


    /*public List<ContribuicaoEvento> listarContribuicoesEvento(String idEvento) {
        Optional<Eventos> optional = this.repository.findById(idEvento);

        if (optional.isPresent()) {
            Eventos evento = optional.get();
            if (evento.getContribuicaoEventoList() != null) {
                log.info("Listando contribuições do evento com id: " + idEvento);
                return evento.getContribuicaoEventoList();
            } else {
                log.info("Nenhuma contribuição para listar no evento com id: " + idEvento);
            }
        } else {
            log.warn("Evento com id: " + idEvento + " não encontrado. Não é possível listar as contribuições.");
        }

        return Collections.emptyList();
    }
*/



    public List<EventosVisualizacaoResponse> listarEventosPorData() {
        List<EventosVisualizacaoResponse> listaResponse = new ArrayList<>();

        // Obtém os eventos ordenados por data
        List<Eventos> eventosOrdenados = this.repository.findAllByOrderByDateAsc();

        for (Eventos eventos : eventosOrdenados) {
            listaResponse.add(new EventosVisualizacaoResponse().toListEventosResponse(eventos));
        }

        log.info("Eventos listados por data!");

        return listaResponse;
    }


    public Boolean adicionarDescontoParaMembro(String id,
                                               Double valorDesconto) {
        Optional<Eventos> eventoOptinal = repository.findById(id);

        System.out.println(valorDesconto);

        if (eventoOptinal.isPresent()) {
            Eventos evento = eventoOptinal.get();
            evento.setValorDescontoMembro(valorDesconto);
            this.repository.save(evento);
            System.out.println(evento.getValorDescontoMembro());
            return true;
        }

        throw new EventoNotFoundException("Evento não encontrado com o ID: " + id);

    }

    public Boolean adicionarDescontoParaAssociado(String id,
                                                  Double valorDesconto) {
        Optional<Eventos> eventoOptinal = repository.findById(id);

        System.out.println(valorDesconto);

        if (eventoOptinal.isPresent()) {
            Eventos evento = eventoOptinal.get();
            evento.setValorDescontoAssociado(valorDesconto);
            this.repository.save(evento);
            return true;
        }

        throw new EventoNotFoundException("Evento não encontrado com o ID: " + id);

    }

    public ValorEventoResponse InformacoesValorEvento(String idEvento,
                                                      String idMembro) {
        ValorEventoResponse response = new ValorEventoResponse();
        Optional<Eventos> optinalEvento = repository.findById(idEvento);

        response.setValorDesconto(0.0);

        Optional<Membro> optinalMembro = null;
        Optional<Associado> optinalAssociado = null;

        if (idMembro != null) {
            optinalMembro = membroRepository.findById(idMembro);

            optinalAssociado = associadoRepository.findById(idMembro);

        }

        if (optinalEvento.isPresent()) {
            Eventos evento = optinalEvento.get();
            response.setValorEvento(evento.getValorDoEvento());

            if (optinalMembro != null) {
                response.setValorDesconto(evento.getValorDescontoMembro());
                response.setTipoDesconto("desconto de Membro");
            }

            if (optinalAssociado != null) {
                response.setValorDesconto(evento.getValorDescontoAssociado());
                response.setTipoDesconto("desconto de Associado");
            }

            response.setValorEventoComDesconto(response.getValorEvento() - response.getValorDesconto());

            return response;

        }

        throw new EventoNotFoundException("Evento não encontrado com o ID: " + idEvento);
    }

    public Boolean AlterarValorEvento(String idEvento,
                                      Double valorEvento) {

        Optional<Eventos> eventosOptional = repository.findById(idEvento);
        if (eventosOptional.isPresent()) {
            Eventos evento = eventosOptional.get();

            evento.setValorDoEvento(valorEvento);

            repository.save(evento);
            return true;

        }

        throw new EventoNotFoundException("Evento não encontrado com o ID: " + idEvento);

    }

    public ValoresEventoResponse informacoesValoresEvento(
            String idEvento) {
        ValoresEventoResponse response = new ValoresEventoResponse();


        Optional<Eventos> optinalEvento = repository.findById(idEvento);

        if (optinalEvento.isPresent()) {

            Eventos eventos = optinalEvento.get();

            response.setValorEvento(eventos.getValorDoEvento());
            response.setValorDescontoMembro(eventos.getValorDescontoMembro());
            response.setValorDescontoAssociado(eventos.getValorDescontoAssociado());

            return response;

        }

        throw new EventoNotFoundException("Evento não encontrado com o ID: " + idEvento);
    }


}
