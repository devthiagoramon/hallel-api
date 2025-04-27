package br.api.hallel.moduloAPI.dto.v1.ministerio;

import java.util.Objects;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class ConvidadoEscalaMinisterioWithConviteResponse {

    private String id;
    private String nome;
    private String email;
    private String conviteEscalaId;

    public ConvidadoEscalaMinisterioWithConviteResponse() {
    }

    public ConvidadoEscalaMinisterioWithConviteResponse(String id,
                                                        String nome,
                                                        String email,
                                                        String conviteEscalaId) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.conviteEscalaId = conviteEscalaId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getConviteEscalaId() {
        return conviteEscalaId;
    }

    public void setConviteEscalaId(String conviteEscalaId) {
        this.conviteEscalaId = conviteEscalaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConvidadoEscalaMinisterioWithConviteResponse that = (ConvidadoEscalaMinisterioWithConviteResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(nome, that.nome) && Objects.equals(email, that.email) && Objects.equals(conviteEscalaId, that.conviteEscalaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, email, conviteEscalaId);
    }
}
