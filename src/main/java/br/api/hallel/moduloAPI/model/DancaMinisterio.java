package br.api.hallel.moduloAPI.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DancaMinisterio {
    private String id;
    private String ministerioId;
    private String nome;
    private String descricao;
    private String linkVideo;
}
