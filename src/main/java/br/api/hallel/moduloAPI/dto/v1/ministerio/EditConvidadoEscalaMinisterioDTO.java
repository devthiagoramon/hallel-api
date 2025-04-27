package br.api.hallel.moduloAPI.dto.v1.ministerio;

import java.util.Objects;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class EditConvidadoEscalaMinisterioDTO {
    private String nome;
    private String email;
    private String telefone;

    public EditConvidadoEscalaMinisterioDTO() {
    }

    public EditConvidadoEscalaMinisterioDTO(String nome, String email,
                                            String telefone) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EditConvidadoEscalaMinisterioDTO that = (EditConvidadoEscalaMinisterioDTO) o;
        return Objects.equals(nome, that.nome) && Objects.equals(email, that.email) && Objects.equals(telefone, that.telefone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, email, telefone);
    }
}

