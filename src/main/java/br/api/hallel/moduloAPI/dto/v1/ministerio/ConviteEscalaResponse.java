package br.api.hallel.moduloAPI.dto.v1.ministerio;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Objects;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class ConviteEscalaResponse {

    private String id;
    private boolean isEnviado;
    private String mensagem;
    private Date dateSend;
    private Date dateEdit;

    public ConviteEscalaResponse() {
    }

    public ConviteEscalaResponse(String id, boolean isEnviado,
                                 String mensagem, Date dateSend,
                                 Date dateEdit) {
        this.id = id;
        this.isEnviado = isEnviado;
        this.mensagem = mensagem;
        this.dateSend = dateSend;
        this.dateEdit = dateEdit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConviteEscalaResponse that = (ConviteEscalaResponse) o;
        return isEnviado == that.isEnviado && Objects.equals(id, that.id) && Objects.equals(mensagem, that.mensagem) && Objects.equals(dateSend, that.dateSend) && Objects.equals(dateEdit, that.dateEdit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isEnviado, mensagem, dateSend, dateEdit);
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
}
