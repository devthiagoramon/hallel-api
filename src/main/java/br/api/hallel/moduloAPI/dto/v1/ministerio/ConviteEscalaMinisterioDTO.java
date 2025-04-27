package br.api.hallel.moduloAPI.dto.v1.ministerio;

import java.util.Objects;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class ConviteEscalaMinisterioDTO {
    private String mensagem;

    public ConviteEscalaMinisterioDTO(String mensagem) {
        this.mensagem = mensagem;
    }


    public ConviteEscalaMinisterioDTO() {
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConviteEscalaMinisterioDTO that = (ConviteEscalaMinisterioDTO) o;
        return Objects.equals(mensagem, that.mensagem);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mensagem);
    }
}
