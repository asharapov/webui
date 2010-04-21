package org.echosoft.framework.ui.extjs.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.echosoft.common.json.annotate.JsonWriteNulls;

/**
 * Абстрактный класс для чтения структурированных данных из источников данных и конвертирования из
 * в объекты класса <code>Ext.data.Record</code> (ExtJS)
 * @author Anton Sharapov
 */
@JsonWriteNulls(false)
public abstract class DataReader implements Serializable, Cloneable {

    private List<DataField> fields;         // описания полей данных; должно быть обязательно.
    private String messageProperty;         // опциональное имя поля, содержащее текст сообщения пользователю от сервера.

    public DataReader() {
        fields = new ArrayList<DataField>();
    }

    /**
     * Возвращает неизменяемый список дескрипторов всех полей из информации о записях хранилища.
     * @return неизменяемый список дескрипторов всех полей.
     */
    public Collection<DataField> getFields() {
        return Collections.unmodifiableCollection(fields);
    }
    /**
     * Добавляет дескриптор нового поля в информацию о записях хранилищя.
     * @param name  логическое имя поля. Должно быть уникальным среди всех прочих полей записи.
     * @return дескриптор поля.
     */
    public DataField appendField(final String name) {
        final DataField field = new DataField(name);
        fields.add( field );
        return field;
    }

    /**
     * Возвращает информацию о свойстве (объекта/документа который был получен в качестве ответа от сервера)
     * в котором хранится информация о сообщении передаваемом от сервера клиенту. Используется, как правило,
     * в качестве описания различных ошибок обнаруженных на стороне сервера.
     * @return информация о свойстве (объекта/документа полученного от сервера) в котором хранится
     * информационное сообщение. Может быть <code>null</code>. 
     */
    public String getMessageProperty() {
        return messageProperty;
    }
    /**
     * Указывает информацию о свойстве (объекта/документа который был получен в качестве ответа от сервера)
     * в котором хранится информация о сообщении передаваемом от сервера клиенту. Используется, как правило,
     * в качестве описания различных ошибок обнаруженных на стороне сервера.
     * @param messageProperty информация о свойстве (объекта/документа полученного от сервера) в котором хранится
     * информационное сообщение. Может быть <code>null</code>.
     */
    public void setMessageProperty(final String messageProperty) {
        this.messageProperty = messageProperty;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        final DataReader result = (DataReader)super.clone();
        result.fields = new ArrayList<DataField>(fields.size());
        for (DataField field : fields) {
            result.fields.add( (DataField)field.clone() );
        }
        return result;
    }
}
