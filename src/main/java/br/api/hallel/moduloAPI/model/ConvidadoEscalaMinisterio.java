package br.api.hallel.moduloAPI.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

@Document(collection = "convidado_escala_ministerio")
@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class ConvidadoEscalaMinisterio {

    @Id
    private String id;
    private String nome;
    private String email;
    private String telefone;
    @Field(name = "escala_ministerio_id")
    private String escalaMinisterioId;
    @Field(name = "convite_escala_id")
    private String conviteEscalaId;

    public ConvidadoEscalaMinisterio() {
    }

    public ConvidadoEscalaMinisterio(String id, String nome,
                                     String email,
                                     String telefone,
                                     String escalaMinisterioId,
                                     String conviteEscalaId) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.escalaMinisterioId = escalaMinisterioId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConvidadoEscalaMinisterio that = (ConvidadoEscalaMinisterio) o;
        return Objects.equals(id, that.id) && Objects.equals(nome, that.nome) && Objects.equals(email, that.email) && Objects.equals(telefone, that.telefone) && Objects.equals(escalaMinisterioId, that.escalaMinisterioId) && Objects.equals(conviteEscalaId, that.conviteEscalaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, email, telefone, escalaMinisterioId, conviteEscalaId);
    }
}
