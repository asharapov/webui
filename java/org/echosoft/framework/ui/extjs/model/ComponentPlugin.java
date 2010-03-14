package org.echosoft.framework.ui.extjs.model;

import java.io.Serializable;

import org.echosoft.common.json.JSFunction;

/**
 * Описывает плагины к компонентам ExtJS.<br/>
 * <strong color="blue">TODO: содержимое данного интерфейса не является окончательным и может еще очень сильно изменяться ...</strong>
 * @author Anton Sharapov
 */
public interface ComponentPlugin extends Serializable {

    /**
     * Используется для идентификации ранее зарегистрированного во фреймворке плагина.
     * Если же это свойство равно <code>null</code> то тогда должно быть обязательно указано значение в свойстве <code>init()</code>
     * {@inheritDoc}
     */
    public String getType();

    /**
     * Код инициализации плагина. Используется только в случае работы с нетиповыми плагинами (теми что уже зарегистрированы во фремворке).
     * {@inheritDoc}
     */
    public JSFunction init();
}
