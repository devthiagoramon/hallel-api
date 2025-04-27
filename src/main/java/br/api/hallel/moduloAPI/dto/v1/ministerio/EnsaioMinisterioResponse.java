package br.api.hallel.moduloAPI.dto.v1.ministerio;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class EnsaioMinisterioResponse implements Serializable {
    private String id;
    private String descricao;
    private String titulo;
    private Date date;
    private String idEscalaMinisterioAssociated;
    private String idMinisterio;

    public EnsaioMinisterioResponse(String id, String descricao,
                                    String titulo, Date date,
                                    String idEscalaMinisterioAssociated,
                                    String idMinisterio) {
        this.id = id;
        this.descricao = descricao;
        this.titulo = titulo;
        this.date = date;
        this.idEscalaMinisterioAssociated = idEscalaMinisterioAssociated;
        this.idMinisterio = idMinisterio;
    }

    public EnsaioMinisterioResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIdEscalaMinisterioAssociated() {
        return idEscalaMinisterioAssociated;
    }

    public void setIdEscalaMinisterioAssociated(
            String idEscalaMinisterioAssociated) {
        this.idEscalaMinisterioAssociated = idEscalaMinisterioAssociated;
    }

    public String getIdMinisterio() {
        return idMinisterio;
    }

    public void setIdMinisterio(String idMinisterio) {
        this.idMinisterio = idMinisterio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnsaioMinisterioResponse that = (EnsaioMinisterioResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(descricao, that.descricao) && Objects.equals(titulo, that.titulo) && Objects.equals(date, that.date) && Objects.equals(idEscalaMinisterioAssociated, that.idEscalaMinisterioAssociated) && Objects.equals(idMinisterio, that.idMinisterio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descricao, titulo, date, idEscalaMinisterioAssociated, idMinisterio);
    }
}
