package br.api.hallel.moduloAPI.dto.v1.evento;

import br.api.hallel.moduloAPI.financeiroNovo.model.PagamentoEntradaEvento;
import br.api.hallel.moduloAPI.model.ContribuicaoEvento;
import br.api.hallel.moduloAPI.model.LocalEvento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoDTO {

    private String descricao;
    private String titulo;
    private Date date;
    private LocalEvento localEvento;
    private String horario;
    private String fileImageUrl;
    private String banner;
    private List<String> palestrantes;
    private List<PagamentoEntradaEvento> pagamentoEntradaEventoList;
    private Boolean destaque;
    private List<ContribuicaoEvento> contribuicaoEventosList;
    private String valorDoEvento;
    private Double ValorDescontoMembro;
    private Double ValorDescontoAssociado;
    private List<String> ministeriosAssociados;
}
