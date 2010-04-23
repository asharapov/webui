package org.echosoft.framework.ui.extjs.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

/**
 * Абстрактный класс для чтения структурированных данных из источников данных и конвертирования из
 * в объекты класса <code>Ext.data.Record</code> (ExtJS)
 * @author Anton Sharapov
 */
public abstract class DataReader implements Serializable, Cloneable {

    private LinkedHashMap<String,DataField> fields; // описания полей данных; должно быть обязательно.
    private String messageProperty;             // опциональное имя поля, содержащее текст сообщения пользователю от сервера.

    public DataReader() {
        fields = new LinkedHashMap<String,DataField>();
    }
    public DataReader(final String... fields) {
        this();
        for (String field : fields) {
            appendField( field );
        }
    }

    /**
     * Возвращает неизменяемый список дескрипторов всех полей из информации о записях хранилища.
     * @return неизменяемый список дескрипторов всех полей.
     */
    public Collection<DataField> getFields() {
        return Collections.unmodifiableCollection(fields.values());
    }

    /**
     * Возвращает дескриптор поля по его имени.
     * @param name  логическое имя поля.
     * @return дескриптор поля по его имени или <code>null</code> если поле с таким именем
     * в источнике данных не определено.
     */
    public DataField getField(final String name) {
        return fields.get(name);
    }

    /**
     * Добавляет дескриптор нового поля в информацию о записях хранилищя.
     * @param name  логическое имя поля. Должно быть уникальным среди всех прочих полей записи.
     * @return дескриптор поля.
     */
    public DataField appendField(final String name) {
        DataField field = fields.get(name);
        if (field==null) {
            field = new DataField(name);
            fields.put(name, field);
        }
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
        result.fields = new LinkedHashMap<String,DataField>(fields.size());
        for (DataField field : fields.values()) {
            result.fields.put( field.getName(), (DataField)field.clone() );
        }
        return result;
    }
}
