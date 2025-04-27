package br.api.hallel.moduloAPI.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Repertorio {
    private String id;
    private String ministerioId;
    private String nome;
    private String descricao;
    private List<String> musicasIds;
    private List<String> dancaMinisterioIds;
}

