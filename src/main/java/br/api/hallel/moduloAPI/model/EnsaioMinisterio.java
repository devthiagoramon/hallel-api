package br.api.hallel.moduloAPI.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class EnsaioMinisterio implements Serializable {
    private String id;
    private String titulo;
    private String descricao;
    private Date date;
    private String idEscalaMinisterioAssociated;
    private String idMinisterio;

    public EnsaioMinisterio(String id, String titulo,
                            String descricao,
                            Date date, String idEscalaMinisterioAssociated,
                            String idMinisterio) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.date = date;
        this.idEscalaMinisterioAssociated = idEscalaMinisterioAssociated;
        this.idMinisterio = idMinisterio;
    }

    public EnsaioMinisterio() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public void setIdEscalaMinisterioAssociated(String idEscalaMinisterioAssociated) {
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
        EnsaioMinisterio that = (EnsaioMinisterio) o;
        return Objects.equals(id, that.id) && Objects.equals(titulo, that.titulo) && Objects.equals(descricao, that.descricao) && Objects.equals(date, that.date) && Objects.equals(idEscalaMinisterioAssociated, that.idEscalaMinisterioAssociated) && Objects.equals(idMinisterio, that.idMinisterio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, descricao, date, idEscalaMinisterioAssociated, idMinisterio);
    }
}
