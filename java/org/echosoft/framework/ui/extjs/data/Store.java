package org.echosoft.framework.ui.extjs.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.echosoft.common.json.JSFunction;
import org.echosoft.common.utils.StringUtil;

/**
 * Инкапсулирует источник данных на клиентской стороне для всех виджетов ExtJS.
 * @author Anton Sharapov
 */
public abstract class Store implements Serializable {

    private static final Set<String> EVENTS =
            StringUtil.asUnmodifiableSet(
                    "add", "beforeload", "beforesave", "beforewrite", "clear",
                    "datachanged", "exception", "load", "loadexception", "metachange",
                    "remove", "save", "update", "write");

    private String storeId;                 // идентификатор объекта в реестре ExtJS: <code>Ext.StoreMgr</code>.
    private Map<String,JSFunction> listeners; // обработчики событий

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
            listeners = new HashMap<String,JSFunction>();
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

}
