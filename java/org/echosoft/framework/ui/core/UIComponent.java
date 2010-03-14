package org.echosoft.framework.ui.core;

import java.io.Serializable;

import org.echosoft.common.json.JsonWriter;

/**
 * Базовый интерфейс который должны реализовывать все веб компоненты.
 * При конструировании классов реализующих данный интерфейс следует учитывать возможность участия одного экземпляра
 * компонента одновоременно в обработке нескольких пользовательских запросов обращенных к одной и той же странице. 
 *
 * @author Anton Sharapov
 */
public interface UIComponent extends Serializable {

    /**
     * Возвращает экземпляр контекста компонента для обработки текущего запроса.
     * @return контекста компонента.
     */
    public ComponentContext getContext();

    /**
     * Данный метод вызывается после инициализации свойств компонента и предназначен для выполнения всей логики работы компонента.
     * Результатом работы является вывод в выходной поток данных, определяющих текущее представление данного компонента.<br/>
     * Если компоненту для его корректной работы требуется подключение разного рода дополнительных разделяемых рерсурсов то он регистрирует их в
     * соответствующем экземпляре класса {@link org.echosoft.framework.ui.core.Resources} доступном из контекста выполнения.
     * @param out  выходной поток в который компонент должен вывести свою модель в формате JSON.
     * @throws Exception в случае каких-либо проблем.
     */
    public void invoke(JsonWriter out) throws Exception;
}
