package org.echosoft.framework.ui.core;

import java.io.Serializable;

import org.echosoft.common.utils.StringUtil;

/**
 * Данный класс представляет собой некоторую строку которая в процессе обработки пользовательского запроса ассоциируется с произвольным компонентом в иерархии
 * компонент на генерируемой странице. Как правило сообщения используются для указания каких-либо проблем возникших при обработке связанных с соответствующим
 * компонентом данных. Сообщения ассоциируются с соответствующими компонентамами путем указания их полных идентификаторов.
 * @author Anton Sharapov
 */
public class Message implements Serializable, Comparable<Message> {

    /**
     * Описывает уровни т.н. "серьезности" сообщений.
     */
    public static enum Severity {
        FATAL, ERROR, WARN, INFO
    }

    private final String clientId;
    private final Severity severity;
    private final String subject;
    private final String detail;
    private final Throwable cause;

    public Message(final String clientId, final Severity severity, final String subject) {
        this(clientId, severity, subject, null, null);
    }

    public Message(final String clientId, final Severity severity, final String subject, final String detail) {
        this(clientId, severity, subject, detail, null);
    }

    public Message(final String clientId, final Severity severity, final Throwable cause) {
        this(clientId, severity, cause.getMessage(), null, cause);
    }

    public Message(final String clientId, final Severity severity, final String subject, final String detail, final Throwable cause) {
        this.clientId = StringUtil.getNonEmpty(StringUtil.trim(clientId), "");
        this.severity = severity!=null ? severity : Severity.INFO;
        this.subject = StringUtil.getNonEmpty(StringUtil.trim(subject), "");
        this.detail = detail;
        this.cause = cause;
    }

    /**
     * Полный идентификатор компонента (т.е. включающий полный идентификатор родительского компонента) для кого было адресовано данное сообщение.
     * @return  полный идентификатор компонента кому предназначено данное сообщение.
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Степень "серьезности" сообщения.
     * @return  степень "серьезности" сообщения.
     */
    public Severity getSeverity() {
        return severity;
    }

    /**
     * Обязательное краткое описание данного сообщения.
     * @return  Локализованная строка с кратким описанием данного сообщения, его заголовок. Не может быть <code>null</code>.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Дополнительное и как правило более развернутое описание к данному сообщению. Может отсутствовать.
     * @return  локализованная строка с подробной информацией по данному сообщению. Может быть <code>null</code>.
     */
    public String getDetail() {
        return detail;
    }

    /**
     * Используется в случаях когда данное сообщение было послано в качестве реакции системы на какую-либо ошибку.
     * @return  исключительная ситуация послужившая причиной отправки данного сообщения. Очень часто равно <code>null</code>.
     */
    public Throwable getCause() {
        return cause;
    }

    @Override
    public int compareTo(final Message other) {
        int result = severity.compareTo(other.severity);
        if (result!=0)
            return result;
        result = clientId.compareTo(other.clientId);
        if (result!=0)
            return result;
        return subject.compareTo(other.subject);
    }

    @Override
    public int hashCode() {
        return (clientId.hashCode()<<4) + severity.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj==null || !getClass().equals(obj.getClass()))
            return false;
        final Message other = (Message)obj;
        return clientId.equals(other.clientId) &&
               severity.equals(other.severity) &&
               subject.equals(other.subject) &&
               (detail!=null ? detail.equals(other.detail) : other.detail==null) &&
               (cause!=null ? cause.equals(other.cause) : other.cause==null);
    }

    @Override
    public String toString() {
        return "[Message{id:"+clientId+", severity:"+severity+", summary:"+ subject +"}]";
    }
}
