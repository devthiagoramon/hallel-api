package br.api.hallel.moduloAPI.dto.v1.ministerio;

import br.api.hallel.moduloAPI.model.Ministerio;
import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MinisterioPublicResponse {
    private String id;
    private String nome;
    private String fileImageUrl;
    public static MinisterioPublicResponse toListMinisterioResponse(Ministerio ministerio) {
        MinisterioPublicResponse response = new MinisterioPublicResponse();
        response.id = ministerio.getId();
        response.nome = ministerio.getNome();
        response.fileImageUrl = ministerio.getFileImageUrl();
        return response;
    }

}
