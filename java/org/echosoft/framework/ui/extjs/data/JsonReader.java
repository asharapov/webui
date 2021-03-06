package org.echosoft.framework.ui.extjs.data;

import org.echosoft.common.json.annotate.JsonUseSeriazer;
import org.echosoft.framework.ui.extjs.spi.data.JsonReaderJsonSerializer;

/**
 * Предназначен для чтения структурированных данных представленных в формате JSON в объекты класса
 * <code>Ext.data.Record</code> (ExtJS)
 * @author Anton Sharapov
 */
@JsonUseSeriazer(JsonReaderJsonSerializer.class)
public class JsonReader extends DataReader {
    private String root;                    // Свойство содержащее массив записей.
    private String successProperty;         // Свойство содержащее статус выполнения запроса (успешно/неуспешно).
    private String totalProperty;           // Свойство содержащее общее кол-во записей удовлетворяемое данному запросу.
    private String idProperty;              // Имя свойства в каждой записи соответствующее ее уникальному идентификатору.

    public JsonReader() {
        super();
    }
    public JsonReader(final String... fields) {
        super(fields);
    }

    /**
     * Для объекта описывающего все данные полученные от сервера возвращает имя свойства содержащее
     * массив объектов каждый из которых соответствует одной записи в хранилище данных ExtJS.
     * @return Свойство содержащее массив записей.
     */
    public String getRoot() {
        return root;
    }
    /**
     * Для объекта описывающего все данные полученные от сервера задает имя свойства содержащее
     * массив объектов каждый из которых соответствует одной записи в хранилище данных ExtJS.
     * @param root имя свойства (выражение javascript) содержащее массив всех записей полученных от сервера.
     */
    public void setRoot(final String root) {
        this.root = root;
    }

    /**
     * Для объекта описывающего все данные полученные от сервера возвращает имя свойства содержащее
     * информацию о статусе выполнения данного запроса на сервер (успешно/неуспешно).
     * @return имя свойства содержащее статус данного запроса:
     * <ul>
     *  <li><code>true</code> - запрос выполнен успешно
     *  <li><code>false</code> - запрос выполнен с ошибками.
     * </ul>
     */
    public String getSuccessProperty() {
        return successProperty;
    }
    /**
     * Для объекта описывающего все данные полученные от сервера задает имя свойства содержащее
     * информацию о статусе выполнения данного запроса на сервер (успешно/неуспешно).
     * @param successProperty имя свойства содержащее статус данного запроса:
     * <ul>
     *  <li><code>true</code> - запрос выполнен успешно
     *  <li><code>false</code> - запрос выполнен с ошибками.
     * </ul>
     */
    public void setSuccessProperty(String successProperty) {
        this.successProperty = successProperty;
    }

    /**
     * Для объекта описывающего все данные полученные от сервера возвращает имя свойства содержащее
     * общее количество записей на сервере которые удовлетворяют запросу клиента (в ответ сервер может
     * отправить только часть удовлетворяющих заданным критериям записей).
     * @return имя свойства содержащее общее кол-во записей на сервере удовлетворяющих заданным в запросе критериям.
     */
    public String getTotalProperty() {
        return totalProperty;
    }
    /**
     * Для объекта описывающего все данные полученные от сервера задает имя свойства содержащее
     * общее количество записей на сервере которые удовлетворяют запросу клиента (в ответ сервер может
     * отправить только часть удовлетворяющих заданным критериям записей).
     * @param totalProperty имя свойства содержащее общее кол-во записей на сервере удовлетворяющих заданным в запросе критериям.
     */
    public void setTotalProperty(String totalProperty) {
        this.totalProperty = totalProperty;
    }

    /**
     * Для объекта описывающего одну запись возвращает имя свойства (выражение javascript) содержащее
     * уникальный идентификатор данной записи.
     * @return имя поля объекта соответствующего отдельной записи в котором содержится уникальный идентификатор данной записи.
     */
    public String getIdProperty() {
        return idProperty;
    }
    /**
     * Для объекта описывающего одну запись задает имя свойства (выражение javascript) содержащее
     * уникальный идентификатор данной записи.
     * @param idProperty имя поля объекта соответствующего отдельной записи в котором содержится уникальный идентификатор данной записи.
     */
    public void setIdProperty(String idProperty) {
        this.idProperty = idProperty;
    }

}
