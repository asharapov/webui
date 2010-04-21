package org.echosoft.framework.ui.extjs.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.echosoft.common.json.JSFunction;
import org.echosoft.common.json.annotate.JsonWriteNulls;

/**
 * Набор опциональных параметров, для загрузки данных из сервера.
 * @see Store#isAutoLoad()
 * @author Anton Sharapov
 */
@JsonWriteNulls(false)
public class AutoLoadOptions implements Serializable, Cloneable {

    private Map<String,Object> params;      // дополнительные параметры, передаваемые в запросе на сервер.
    private JSFunction callback;            // js функция вызываемая при получении ответа от сервера.
    private boolean add;                    // указывает что полученные от сервера данные надо добавлять в хранилище без удаления уже имеющихся.

    /**
     * Возвращает перечень всех дополнительных параметров в запросе.
     * @return дополнительные параметры для запроса на сервер.
     */
    public Map<String,Object> getParams() {
        return params;
    }
    /**
     * Указывает перечень всех дополнительных параметров в запросе.
     * @param params дополнительные параметры для запроса на сервер.
     */
    public void setParams(final Map<String,Object> params) {
        this.params = params;
    }
    /**
     * Добавляет еще один параметр запроса.
     * @param name  имя параметра.
     * @param value  значение параметра.
     */
    public void putParam(final String name, final Object value) {
        if (params==null)
            params = new HashMap<String,Object>(4);
        params.put(name, value);
    }

    /**
     * Возвращает callback функцию, которая будет вызвана при получении ответа от сервера.
     * @return  функция которая будет вызвана при получении ответа от сервера.
     */
    public JSFunction getCallback() {
        return callback;
    }
    /**
     * Указывает callback функцию, которая будет вызвана при получении ответа от сервера.
     * @param callback  функция которая будет вызвана при получении ответа от сервера.
     */
    public void setCallback(final JSFunction callback) {
        this.callback = callback;
    }

    /**
     * Возвращает <code>true</code> если полученные от сервера данные должны быть просто добавлены к
     * тем данным что уже были в хранилище на стороне клиента. В противном случае старые данные будут
     * замещаться новыми.
     * @return <code>true</code> если полученные от сервера данные должны быть просто добавлены к
     * тем данным что уже были в хранилище на стороне клиента.
     * Значение свойства по умолчанию: <code>false</code>.
     */
    public boolean isAdd() {
        return add;
    }
    /**
     * Указывает должны ли получаемые от сервера данные быть просто добавлены к
     * тем данным что уже были в хранилище на стороне клиента. В противном случае старые данные будут
     * замещаться новыми.
     * @param add <code>true</code> если полученные от сервера данные должны быть просто добавлены к
     * тем данным что уже были в хранилище на стороне клиента.
     * Значение свойства по умолчанию: <code>false</code>.
     */
    public void setAdd(final boolean add) {
        this.add = add;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        final AutoLoadOptions result = (AutoLoadOptions)super.clone();
        if (params!=null) {
            result.params = new HashMap<String,Object>(params.size());
            result.params.putAll( params );
        }
        return result;
    }
}
