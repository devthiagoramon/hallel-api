package br.api.hallel.moduloAPI.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Objects;

@Document(collection = "convite_escala_ministerio")
@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class ConviteEscalaMinisterio {

    @Id
    private String id;

    @Field(name = "is_enviado")
    private boolean isEnviado;

    private String mensagem;

    @Field(name = "date_send")
    private Date dateSend;

    @Field(name = "date_edit")
    private Date dateEdit;

    public ConviteEscalaMinisterio() {
    }

    public ConviteEscalaMinisterio(String id, boolean isEnviado,
                                   String mensagem, Date dateSend,
                                   Date dateEdit) {
        this.id = id;
        this.isEnviado = isEnviado;
        this.mensagem = mensagem;
        this.dateSend = dateSend;
        this.dateEdit = dateEdit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEnviado() {
        return isEnviado;
    }

    public void setEnviado(boolean enviado) {
        isEnviado = enviado;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Date getDateSend() {
        return dateSend;
    }

    public void setDateSend(Date dateSend) {
        this.dateSend = dateSend;
    }

    public Date getDateEdit() {
        return dateEdit;
    }

    public void setDateEdit(Date dateEdit) {
        this.dateEdit = dateEdit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConviteEscalaMinisterio that = (ConviteEscalaMinisterio) o;
        return isEnviado == that.isEnviado && Objects.equals(id, that.id) && Objects.equals(mensagem, that.mensagem) && Objects.equals(dateSend, that.dateSend) && Objects.equals(dateEdit, that.dateEdit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isEnviado, mensagem, dateSend, dateEdit);
    }
}
