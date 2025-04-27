package br.api.hallel.moduloAPI.dto.v2.ministerio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MinisterioDTOV2 {
    @NotNull
    @NotBlank(message = "Digite um nome para o ministerio!")
    private String nome;
    private String descricao;
    private String fileImageUrl;
    private List<String> objetivos;
    private String coordenadorId;
    private String viceCoordenadorId;
    private boolean hasRepertorio;
    private boolean hasMusic;
    private boolean hasDance;
}
