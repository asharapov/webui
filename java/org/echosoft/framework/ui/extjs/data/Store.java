package org.echosoft.framework.ui.extjs.data;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.echosoft.common.json.JSFunction;
import org.echosoft.common.json.annotate.JsonUseSeriazer;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.extjs.spi.data.StoreJSONSerializer;

/**
 * Инкапсулирует кэш записей с данными на клиентской стороне для всех виджетов ExtJS.
 * @author Anton Sharapov
 */
@JsonUseSeriazer(StoreJSONSerializer.class)
public class Store implements Serializable, Cloneable {

    public static final String DEFAULT_STORE = "store";
    public static final String JSON_STORE = "jsonstore";
    public static final String ARRAY_STORE = "arraystore";
    public static final String GROUPING_STORE = "groupingstore";
    public static final String DIRECT_STORE = "directstore";
    public static final String XML_STORE = "xmlstore";

    private static final Set<String> EVENTS =
            StringUtil.asUnmodifiableSet(
                    "add", "beforeload", "beforesave", "beforewrite", "clear",
                    "datachanged", "exception", "load", "loadexception", "metachange",
                    "remove", "save", "update", "write");

    private String xtype;                   // тип хранилища ExtJS.
    private String storeId;                 // идентификатор данного кэша в реестре ExtJS: <code>Ext.StoreMgr</code>.
    private boolean autoDestroy;            // должен ли данный кэш быть уничтожен при уничтожении компонента, его использующего.
    private boolean autoLoad;               // должны ли подчитываться данные в кэш сразу после загрузки страницы (асинхронно).
    private AutoLoadOptions autoLoadOptions;// дополнительные параметры, передаваемые загрузчику.
    private boolean autoSave;               // TODO: пока не понял чем отличаются autoSave от batch от restful ...
    private boolean batch;                  //
    private boolean restful;                //
    private boolean remoteSort;             // определяет где будет выполняться сортировка: на сервере или на клиенте.
    private SortInfo sortInfo;              // по какому полю будем выполнять сортировку.
    private String url;                     // URL по которому можно будет получить данные (неявно задает Ext.data.HttpProxy)
    private Object data;                    // собственно данные.
    private Map<String,Object> baseParams;  // дополнительные параметры к запросу.
    private Map<String,String> paramNames;  // дает возможность переопределить некоторые стандартные имена параметров.
    private DataReader reader;              // разбирает ответ полученный от сервера и приводит его к некоторому нормальному виду.
    //TODO: proxy, writer
    private Map<String,JSFunction> listeners;// обработчики событий.

    public Store() {
        this(null);
    }
    public Store(final String storeId) {
        this.storeId = storeId;
        xtype = DEFAULT_STORE;
        batch = true;
    }

    /**
     * Возвращает код типа хранилища под которым то было зарегистрировано в менеджере компонент ExtJS.
     * @return код типа хранилища под которым то было зарегистрировано в менеджере компонент ExtJS.
     */
    public String getXType() {
        return xtype;
    }
    /**
     * Указывает код типа хранилища под которым то было зарегистрировано в менеджере компонент ExtJS.
     * @param xtype код типа хранилища под которым то было зарегистрировано в менеджере компонент ExtJS.
     */
    public void setXType(final String xtype) {
        this.xtype = StringUtil.trim(xtype);
    }

    /**
     * Возвращает идентификатор данного хранилища данных в реестре ExtJS: <code>Ext.StoreMgr</code>.
     * @return идентификатор данного хранилища данных в реестре ExtJS: <code>Ext.StoreMgr</code>.
     */
    public String getStoreId() {
        return storeId;
    }
    /**
     * Указывает идентификатор данного хранилища данных в реестре ExtJS: <code>Ext.StoreMgr</code>.
     * @param storeId  идентификатор данного хранилища.
     */
    public void setStoreId(final String storeId) {
        this.storeId = storeId;
    }

    /**
     * Возвращает <code>true</code> если это хранилище данных должно быть уничтожено вместе с
     * компонентом который его использует. Имеет смысл устанавливать это свойство только в тех случаях,
     * когда хранилище данных используется только одним компонентом.
     * @return <code>true</code> если это хранилище данных должно быть уничтожено вместе с использующим его компонентом.
     *  Значение свойства по умолчанию: <code>false</code>.
     */
    public boolean isAutoDestroy() {
        return autoDestroy;
    }
    /**
     * Определяет должно ли данное хранилище быть уничтожено во время уничтожения использующего его компонента.
     * Имеет смысл устанавливать это свойство только в тех случаях, когда хранилище используется только одним компонентом.
     * @param autoDestroy <code>true</code> если это хранилище данных должно быть уничтожено вместе с использующим его компонентом.
     */
    public void setAutoDestroy(final boolean autoDestroy) {
        this.autoDestroy = autoDestroy;
    }

    /**
     * Возвращает <code>true</code> если требуемые для хранилища данные будут подгружены непосредственно
     * сразу после загрузки страницы. Данное свойство имеет смысл только в том случае когда свойство <code>data</code>
     * равно <code>null</code>, т.е. требуемые данные не были заранее указаны в конфигурации хранилища.
     * @return <code>true</code> если требуемые для хранилища данные будут подгружены непосредственно
     * сразу после загрузки страницы. Значение свойства по умолчанию: <code>false</code>.
     */
    public boolean isAutoLoad() {
        return autoLoad;
    }
    /**
     * Определяет должно ли данное хранилище подгружать требуемые данные сразу по окончании загрузки страницы.
     * Данное свойство имеет смысл только в том случае когда свойство <code>data</code>
     * равно <code>null</code>, т.е. требуемые данные не были заранее указаны в конфигурации хранилища.
     * @param autoLoad <code>true</code> если требуемые для хранилища данные будут подгружены непосредственно
     * сразу после загрузки страницы. Значение свойства по умолчанию: <code>false</code>.
     * @see #setAutoLoadOptions(AutoLoadOptions)
     */
    public void setAutoLoad(final boolean autoLoad) {
        this.autoLoad = autoLoad;
        if (!autoLoad)
            autoLoadOptions = null;
    }

    /**
     * Возвращает дополнительные опции используемые при автоматической подгрузке данных с сервера в момент
     * завершения загрузки страницы.
     * @return дополнительные опции используемые при автоматической подгрузке данных с сервера в момент
     * завершения загрузки страницы.
     * @see #isAutoLoad()
     */
    public AutoLoadOptions getAutoLoadOptions() {
        return autoLoadOptions;
    }
    /**
     * Указывает дополнительные опции используемые при автоматической подгрузке данных с сервера в момент завершения
     * загрузки страницы. Установка данного свойства приводит к неявной установке опции <code>autoLoad</code>.
     * @param options опции используемые при автоматической подгрузке данных с сервера в момент
     * завершения загрузки страницы.
     * @see #setAutoLoad(boolean)
     */
    public void setAutoLoadOptions(final AutoLoadOptions options) {
        this.autoLoadOptions = options;
        if (options!=null)
            autoLoad = true;
    }

    public boolean isAutoSave() {
        return autoSave;
    }
    public void setAutoSave(final boolean autoSave) {
        this.autoSave = autoSave;
    }

    public boolean isBatch() {
        return batch;
    }
    public void setBatch(final boolean batch) {
        this.batch = batch;
        if (batch)
            restful = false;
    }

    public boolean isRestful() {
        return restful;
    }
    public void setRestful(final boolean restful) {
        this.restful = restful;
        if (restful)
            batch = false;
    }

    /**
     * Возвращает <code>true</code> если вся требуемая сортировка записей должна будет выполняться на сервере а не
     * в самом хранилище на клиенсткой стороне.
     * @return <code>true</code> если вся требуемая сортировка записей должна будет выполняться на сервере а не клиенте.
     *   Значение свойства по умолчанию: <code>false</code>.
     */
    public boolean isRemoteSort() {
        return remoteSort;
    }
    /**
     * Указывает где должна выполняться сортировка данных - на сервере или на клиенте.
     * @param remoteSort <code>true</code> если вся требуемая сортировка записей должна будет выполняться на сервере а не клиенте.
     *   Значение свойства по умолчанию: <code>false</code>.
     */
    public void setRemoteSort(final boolean remoteSort) {
        this.remoteSort = remoteSort;
    }

    /**
     * Возвращает информацию о том по какому полю требуется отсортировать возвращаемые из хранилища данные.
     * @return направление сортировки и название поля по которому требуется отсортировать данные в хранилище.
     */
    public SortInfo getSortInfo() {
        return sortInfo;
    }
    /**
     * Задает информацию о том по какому полю требуется отсортировать возвращаемые из хранилища данные.
     * @param sortInfo направление сортировки и название поля по которому требуется отсортировать данные в хранилище.
     */
    public void setSortInfo(final SortInfo sortInfo) {
        this.sortInfo = sortInfo;
    }

    /**
     * Возвращает URL по которому будет отправлен запрос за получением данных. Данное свойство используется
     * для неявного конструирования экземпляра <code>Ext.data.HttpProxy</code>.
     * @return  URL по которому будет отправлен запрос за получением данных.
     */
    public String getUrl() {
        return url;
    }
    /**
     * Указывает URL по которому будет отправлен запрос за получением данных. Данное свойство используется
     * для неявного конструирования экземпляра <code>Ext.data.HttpProxy</code>.
     * @param url  URL по которому будет отправлен запрос за получением данных.
     */
    public void setUrl(final String url) {
        this.url = StringUtil.trim(url);
    }

    /**
     * Возвращает массив данных которыми будет инициализировано хранилище.
     * В подавляющем большинстве случае в конфигурации хранилища используется одно из двух свойств:
     * либо <code>data</code> либо <code>url</code>.
     * @return данные (массив данных) которыми будет инициализировано хранилище. 
     */
    public Object getData() {
        return data;
    }
    /**
     * Указывает массив данных которыми будет инициализировано хранилище.
     * В подавляющем большинстве случае в конфигурации хранилища используется одно из двух свойств:
     * либо <code>data</code> либо <code>url</code>.
     * @param data данные (массив данных) которыми будет инициализировано хранилище.
     */
    public void setData(final Object data) {
        this.data = data;
    }

    /**
     * Возвращает перечень всех дополнительных параметров в запросе.
     * @return дополнительные параметры для запроса на сервер.
     */
    public Map<String,Object> getBaseParams() {
        return baseParams;
    }
    /**
     * Указывает перечень всех дополнительных параметров в запросе.
     * @param baseParams дополнительные параметры для запроса на сервер.
     */
    public void setBaseParams(final Map<String,Object> baseParams) {
        this.baseParams = baseParams;
    }
    /**
     * Добавляет еще один параметр запроса.
     * @param name  имя параметра.
     * @param value  значение параметра.
     */
    public void putBaseParam(final String name, final Object value) {
        if (baseParams==null)
            baseParams = new HashMap<String,Object>(4);
        baseParams.put(name, value);
    }

    /**
     * Возвращает перечень переопределенных имен параметров используемых ExtJS для отправки разного рода информации с клиента на сервер.
     * @return перечень измененных имен параметров в виде соответствия "код параметра" -> "реальное имя параметра"
     * Метод может возвращать <code>null</code> в случае если никаких переопеределений имен параметров выполнять не требуется
     * (т.е. будут использоваться имена параметров по умолчанию).
     * @see #setParamName(String, String)
     */
    public Map<String,String> getParamNames() {
        return paramNames;
    }
    /**
     * Дает возможность переопределить некоторые имена системных параметров которые ExtJS отправляет на сервер.
     * К таким именам относятся:
     * <table border="1">
     *  <tr>
     *   <th>Код параметра</th>
     *   <th>Имя параметра по умолчанию</th>
     *   <th>Описание параметра</th>
     *  </tr>
     *  <tr>
     *   <td align="center">start</td>
     *   <td align="center">start</td>
     *   <td>Содержит номер стартовой строки с которой должна начинаться выборка</td>
     *  </tr>
     *  <tr>
     *   <td align="center">limit</td>
     *   <td align="center">limit</td>
     *   <td>Содержит максимальное допустимое количество строк которые может вернуть сервер</td>
     *  </tr>
     *  <tr>
     *   <td align="center">sort</td>
     *   <td align="center">sort</td>
     *   <td>Содержит имя поля по которому требуется отсортировать данные</td>
     *  </tr>
     *  <tr>
     *   <td align="center">dir</td>
     *   <td align="center">dir</td>
     *   <td>Задает направление сортировки ('ASC', 'DESC')</td>
     *  </tr>
     * </table>
     * Таким образом чтобы изменить имя параметра в котором ExtJS будет передавать на сервер  направление сортировки на "order" мы должны
     * будет указать:<br/>
     *  <code>store.setParamName("dir", "order");</code>.
     * @param paramId  код параметра
     * @param paramName  реально используемое имя параметра.
     */
    public void setParamName(final String paramId, final String paramName) {
        if (paramNames==null)
            paramNames = new HashMap<String,String>(4);
        paramNames.put(paramId, paramName);
    }

    /**
     * Содержит структуру, описывающую экземпляр класса <code>Ext.data.DataReader</code> (ExtJS) отвечающий
     * за разбор полученного от сервера ответа и приведение его к некоторому нормальному виду.
     * Результатом работы данного ридера должен быть массив записей <code>Ext.data.Record</code> (ExtJS).<br/>
     * Корректно сформированная модель хранилища в ExtJS обязано иметь описание своего ридера.  
     * @return модель ридера используемого для разбора полученного со стороны сервера ответа.
     */
    public DataReader getReader() {
        return reader;
    }
    /**
     * Указывает структуру, описывающую экземпляр класса <code>Ext.data.DataReader</code> (ExtJS) отвечающий
     * за разбор полученного от сервера ответа и приведение его к некоторому нормальному виду.
     * Результатом работы данного ридера должен быть массив записей <code>Ext.data.Record</code> (ExtJS).<br/>
     * Корректно сформированная модель хранилища в ExtJS обязано иметь описание своего ридера.
     * @param reader модель ридера используемого для разбора полученного со стороны сервера ответа.
     */
    public void setReader(final DataReader reader) {
        this.reader = reader;
    }
    public DataReader assignReader( final DataReader reader ) {
        setReader( reader );
        return getReader();
    }

    /**
     * Возвращает перечень всех ассоциированных с данных хранилищем обработчиков событий.
     * Данный метод предназначен главным образом для использования при сериализации модели хранилища в JSON формат.
     * @return неизменяемый map где в качестве ключа используется имя события хранилища а значением -
     * функция - обработчик данного события.
     */
    public Map<String,JSFunction> getListeners() {
        return listeners!=null
                ? Collections.unmodifiableMap(listeners)
                : null;
    }
    /**
     * Подключает новый обработчик определенного события.
     * @param eventName  имя события. Не может быть <code>null</code>.
     * @param handler  обработчик события.
     */
    public void addListener(final String eventName, final JSFunction handler) {
        if (eventName==null || handler==null)
            throw new IllegalArgumentException("Event name and listener must be specified");
        final String event = eventName.trim().toLowerCase();
        if (!getSupportedEvents().contains(event))
            throw new IllegalArgumentException("Unsupported event: "+ eventName);
        if (listeners==null)
            listeners = new HashMap<String,JSFunction>(4);
        listeners.put(event, handler);
    }

    /**
     * Возвращает перечень всех поддерживаемых компонентом событий.<br/>
     * Если хранилище, являющееся наследником данного класса поддерживает иной перечень событий то он должен переопределить данный метод.
     * @return  неизменяемый перечень событий, поддерживаемых данным хранилищем. Метод никогда не возвращает <code>null</code>.
     */
    protected Set<String> getSupportedEvents() {
        return Store.EVENTS;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        final Store result = (Store)super.clone();
        if (autoLoadOptions!=null)
            result.autoLoadOptions = (AutoLoadOptions)autoLoadOptions.clone();
        if (baseParams!=null) {
            result.baseParams = new HashMap<String,Object>(4);
            result.baseParams.putAll( baseParams );
        }
        if (paramNames!=null) {
            result.paramNames = new HashMap<String,String>(4);
            result.paramNames.putAll( paramNames );
        }
        if (reader!=null) {
            result.reader = (DataReader)reader.clone();
        }
        if (listeners!=null) {
            result.listeners = new HashMap<String,JSFunction>(4);
            result.listeners.putAll( listeners );
        }
        return result;
    }

    @Override
    public String toString() {
        return "[Store{id:"+storeId+"}]";
    }
}
