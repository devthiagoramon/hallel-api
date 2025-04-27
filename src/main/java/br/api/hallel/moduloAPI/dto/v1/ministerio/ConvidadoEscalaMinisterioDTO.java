package br.api.hallel.moduloAPI.dto.v1.ministerio;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class ConvidadoEscalaMinisterioDTO {

    @NotBlank(message = "Nome must be inserted")
    private String nome;
    @NotBlank(message = "Email must be inserted")
    private String email;
    @NotBlank(message = "Telefone must be inserted")
    private String telefone;
    private String escalaMinisterioId;
    private String conviteEscalaId;
    private String mensagem;

    public ConvidadoEscalaMinisterioDTO() {
    }

    public ConvidadoEscalaMinisterioDTO(String conviteEscalaId,
                                        String nome, String email,
                                        String telefone,
                                        String escalaMinisterioId,
                                        String mensagem) {
        this.conviteEscalaId = conviteEscalaId;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.escalaMinisterioId = escalaMinisterioId;
        this.mensagem = mensagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEscalaMinisterioId() {
        return escalaMinisterioId;
    }

    public void setEscalaMinisterioId(String escalaMinisterioId) {
        this.escalaMinisterioId = escalaMinisterioId;
    }

    public String getConviteEscalaId() {
        return conviteEscalaId;
    }

    public void setConviteEscalaId(String conviteEscalaId) {
        this.conviteEscalaId = conviteEscalaId;
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
        ConvidadoEscalaMinisterioDTO that = (ConvidadoEscalaMinisterioDTO) o;
        return Objects.equals(nome, that.nome) && Objects.equals(email, that.email) && Objects.equals(telefone, that.telefone) && Objects.equals(escalaMinisterioId, that.escalaMinisterioId) && Objects.equals(conviteEscalaId, that.conviteEscalaId) && Objects.equals(mensagem, that.mensagem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, email, telefone, escalaMinisterioId, conviteEscalaId, mensagem);
    }
}
