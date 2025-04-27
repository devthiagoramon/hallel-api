package br.api.hallel.moduloAPI.dto.v1.ministerio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class NaoConfirmarEnsaioDTO {
    @NotNull(message = "Insira o id do membro")
    @NotBlank(message = "Insira o id do membro")
    private String idMembro;
    @NotNull(message = "Insira o id da escala ministerio")
    @NotBlank(message = "Insira o id da escala ministerio")
    private String idEnsaioMinisterio;
    @NotNull(message = "Insira o motivo da ausencia")
    @NotBlank(message = "Insira o motivo da ausencia")
    private String motivo;

    public NaoConfirmarEnsaioDTO() {
    }

    public NaoConfirmarEnsaioDTO(String idMembro,
                                 String idEnsaioMinisterio,
                                 String motivo) {
        this.idMembro = idMembro;
        this.idEnsaioMinisterio = idEnsaioMinisterio;
        this.motivo = motivo;
    }

    public @NotNull(
            message = "Insira o id do membro") @NotBlank(
            message = "Insira o id do membro") String getIdMembro() {
        return idMembro;
    }

    public void setIdMembro(@NotNull(
            message = "Insira o id do membro") @NotBlank(
            message = "Insira o id do membro") String idMembro) {
        this.idMembro = idMembro;
    }

    public @NotNull(
            message = "Insira o id da escala ministerio") @NotBlank(
            message = "Insira o id da escala ministerio") String getIdEnsaioMinisterio() {
        return idEnsaioMinisterio;
    }

    public void setIdEnsaioMinisterio(
            @NotNull(
                    message = "Insira o id da escala ministerio")
            @NotBlank(
                    message = "Insira o id da escala ministerio") String idEnsaioMinisterio) {
        this.idEnsaioMinisterio = idEnsaioMinisterio;
    }

    public @NotNull(
            message = "Insira o motivo da ausencia") @NotBlank(
            message = "Insira o motivo da ausencia") String getMotivo() {
        return motivo;
    }

    public void setMotivo(@NotNull(
            message = "Insira o motivo da ausencia") @NotBlank(
            message = "Insira o motivo da ausencia") String motivo) {
        this.motivo = motivo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NaoConfirmarEnsaioDTO that = (NaoConfirmarEnsaioDTO) o;
        return Objects.equals(idMembro, that.idMembro) && Objects.equals(idEnsaioMinisterio, that.idEnsaioMinisterio) && Objects.equals(motivo, that.motivo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMembro, idEnsaioMinisterio, motivo);
    }
}
