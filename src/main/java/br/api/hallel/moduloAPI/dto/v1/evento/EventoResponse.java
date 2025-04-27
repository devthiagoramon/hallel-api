package br.api.hallel.moduloAPI.dto.v1.evento;

import br.api.hallel.moduloAPI.dto.v1.ministerio.EscalaMinisterioResponse;
import br.api.hallel.moduloAPI.financeiroNovo.model.PagamentoEntradaEvento;
import br.api.hallel.moduloAPI.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoResponse {
    private String id;
    private List<Associado> associadosParticipando;
    private String descricao;
    private String titulo;
    private List<Membro> integrantes;
    private Date date;
    private LocalEvento localEvento;
    private String fileImageUrl;
    private MultipartFile fileImage;
    private String banner;
    private Boolean destaque;
    private List<String> palestrantes;
    private List<PagamentoEntradaEvento> pagamentoEntradaEventoList;
    private List<VoluntarioEvento> voluntarios;
    private Double valorDoEvento;
    private Double ValorDescontoMembro;
    private Double ValorDescontoAssociado;
    private List<String> ministeriosAssociados;
    private List<EscalaMinisterioResponse> escalasMinisterio;
}
