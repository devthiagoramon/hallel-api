package br.api.hallel.moduloAPI.dto.v1.ministerio;

import java.util.Objects;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class ConvidadoEscalaMinisterioUserResponse {
    private String id;
    private String nome;
    private String email;

    public ConvidadoEscalaMinisterioUserResponse() {
    }

    public ConvidadoEscalaMinisterioUserResponse(String id,
                                                 String nome,
                                                 String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConvidadoEscalaMinisterioUserResponse that = (ConvidadoEscalaMinisterioUserResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(nome, that.nome) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, email);
    }
}
