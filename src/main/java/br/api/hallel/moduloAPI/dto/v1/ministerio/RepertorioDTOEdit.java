package br.api.hallel.moduloAPI.dto.v1.ministerio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RepertorioDTOEdit {
    private String nome;
    private String descricao;
    private List<String> musicasIds;
    private List<String> dancaMinisterioIds;
}
