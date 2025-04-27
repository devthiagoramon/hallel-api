package br.api.hallel.moduloAPI.dto.v1.ministerio;

import java.util.Objects;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class ConvidadoEscalaMinisterioWithInfos {

    private String id;
    private String nome;
    private String email;
    private String telefone;
    private String conviteEscalaId;

    public ConvidadoEscalaMinisterioWithInfos() {
    }

    public ConvidadoEscalaMinisterioWithInfos(String id, String nome,
                                              String email,
                                              String telefone,
                                              String conviteEscalaId) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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
        ConvidadoEscalaMinisterioWithInfos that = (ConvidadoEscalaMinisterioWithInfos) o;
        return Objects.equals(id, that.id) && Objects.equals(nome, that.nome) && Objects.equals(email, that.email) && Objects.equals(telefone, that.telefone) && Objects.equals(conviteEscalaId, that.conviteEscalaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, email, telefone, conviteEscalaId);
    }
}
