package br.api.hallel.moduloAPI.dto.v2.ministerio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MinisterioResponseV2 {
    private String id;
    private String nome;
    private String descricao;
    private String coordenadorId;
    private String viceCoordenadorId;
    private List<String> objetivos;
    private String fileImageUrl;
    private boolean hasRepertorio;
    private boolean hasMusic;
    private boolean hasDance;
}
