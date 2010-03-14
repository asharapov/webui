package org.echosoft.framework.ui.core;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.echosoft.common.collections.FilterIterator;
import org.echosoft.common.model.Predicate;

/**
 * Представляет собой коллекцию сообщений, адресованных произвольных web компонентам системы.
 * Любой класс в системе может как адресовать новое сообщение для произвольного web комонента на странице так и просмотреть все
 * ранее зарегистрированные (в рамках обработки данного пользовательского запроса) сообщения для произвольного web компонента на странице.
 * @author Anton Sharapov
 */
public class Messages {

    private final SortedSet<Message> messages;

    public Messages() {
        this.messages = new TreeSet<Message>();
    }

    /**
     * Регистрирует новое сообщение для определенного web компонента.
     * @param msg  новое сообщение; не может быть <code>null</code>.
     */
    public void addMessage(final Message msg) {
        if (msg==null)
            throw new IllegalArgumentException("Message must be specified");
        messages.add( msg );
    }

    /**
     * Возвращает информацию о том какова наивысшая важность стоит у зарегистрированных в данной коллекции сообщений.
     * Если в коллекции нет ни одного сообщения то метод возвращает <code>null</code>.
     * @return  наивысшая важность у зарегистрированных в коллекции сообщений или <code>null</code> если коллекция пуста.
     */
    public Message.Severity getMaximalSeverity() {
        return !messages.isEmpty() ? messages.first().getSeverity() : null;
    }

    /**
     * Возвращает итератор по всем сообщениям зарегистрированным в коллекции.
     * Сообщения отсортированы по важности и идентификатору компонента.
     * @return  итератор по всем зарегистрированным в коллекции сообщениям.
     */
    public Iterator<Message> messages() {
        return messages.iterator();
    }

    /**
     * Return an Iterator over the Messages that have been queued and associated with the
     * specific client identifier.
     * @param  clientId  qualified component identifier.
     * @return  all messages associated for the specified component.
     */
    public Iterator<Message> messages(final String clientId) {
        if (clientId==null)
            return messages();
        return new FilterIterator<Message>(
            messages.iterator(),
            new Predicate<Message>() {
                public boolean accept(final Message msg) {
                    return msg.getClientId().equals(clientId);
                }
            }
        );
    }

    /**
     * Return an Iterator over the Messages that have been queued, and has specific or more important severity.
     * @param severity  minimal message severity.
     * @return  all messages with same (or greater) severity.
     */
    public Iterator<Message> messages(final Message.Severity severity) {
        if (severity==null)
            return messages();
        return new FilterIterator<Message>(
            messages.iterator(),
            new Predicate<Message>() {
                public boolean accept(final Message msg) {
                    return msg.getSeverity().ordinal() <= severity.ordinal();
                }
            }
        );
    }

    /**
     * Return an Iterator over the Messages that have been queued, has specific or more important severity 
     * and associated with specific client identifier.
     * @param clientId  clientId  qualified component identifier.
     * @param severity  minimal message severity.
     * @return  all messages that confirms specified constraints.
     */
    public Iterator<Message> messages(final String clientId, final Message.Severity severity) {
        if (clientId==null) {
            return messages(severity);
        } else
        if (severity==null)
            return messages(clientId);

        return new FilterIterator<Message>(
            messages.iterator(),
            new Predicate<Message>() {
                public boolean accept(final Message msg) {
                    return
                        msg.getClientId().equals(clientId) &&
                        msg.getSeverity().ordinal() <= severity.ordinal();
                }
            }
        );
    }

    /**
     * Возвращает количество сообщений зарегистрированных в данной коллеции сообщений.
     * @return общее количество сообщений в данной коллекции.
     */
    public int size() {
        return messages.size();
    }

    /**
     * Создает новую точку сохранения.
     * Пример использования:
     * <code>
     * <pre>
     *  final Messages.SavePoint sp = messages.makeSavePoint();
     *  try {
     *      messages.addMessage( new Message(...) );
     *      // do something ...
     *  } catch (Exception e) {
     *      messages.rollback(sp);
     *  }
     * </pre>
     * </code>
     * @return точка сохранения.
     */
    public SavePoint makeSavePoint() {
        return new SavePoint();
    }

    /**
     * Откатывает все изменения, сделанные после создания указанной в аргументе точки сохранения.
     * Точка сохранения должна быть ранее получена при вызове метода {@link #makeSavePoint()} этого же экземпляра класса <code>Messages</code>.
     * @param savePoint  точка сохранения.
     */
    public void rollback(final SavePoint savePoint) {
        if (savePoint==null || savePoint.getOwner()!=this)
            throw new IllegalArgumentException("no correct savepoint instance specified");
        if (savePoint.annuled)
            throw new IllegalStateException("given savepoint already annuled");
        int cnt = 0;
        for (Iterator<Message> it=messages.iterator(); it.hasNext(); ) {
            it.next();
            if (cnt++ >= savePoint.size)
                it.remove();
        }
        savePoint.annuled = true;
    }


    /**
     * Описывает состояние менеджера ресурсов на определенный момент времени.
     * Дает возможность откатить текущее состояние менеджера ресурсов до состояния в котором находился менеджер в момент создания данной точки сохранения.
     */
    public final class SavePoint {
        private final int size;
        private boolean annuled;

        private SavePoint() {
            this.size = messages.size();
            this.annuled = false;
        }
        private Messages getOwner() {
            return Messages.this;
        }
        public boolean isAnnuled() {
            return annuled;
        }
        public void rollback() {
            Messages.this.rollback(this);
        }
    }

}
