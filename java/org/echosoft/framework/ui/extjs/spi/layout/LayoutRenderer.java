package org.echosoft.framework.ui.extjs.spi.layout;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.extjs.layout.Layout;
import org.echosoft.framework.ui.extjs.layout.LayoutItem;

/**
 * Используется для корректной сериализации в JSON той части конфигурационной информации компонент ExtJS что касается поддержки
 * различных классов менеджеров компоновки ExtJS компонент в контейнерах.
 * @author Anton Sharapov
 */
public interface LayoutRenderer {

    /**
     * Сериализует в JSON конфигурацию указанного менеджера компоновки компонент.
     * @param layout  модель менеджера компоненовки.
     * @param out  выходной поток. Должен находиться в состоянии записи свойств компонента.
     * @throws IOException  в случае каких-либо проблем записи данных в поток.
     * @throws InvocationTargetException  в случае проблем с сериализацией данных в JSON.
     * @throws IllegalAccessException  в случае проблем с сериализацией данных в JSON.
     */
    public void renderConfig(final Layout layout, final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException;

    /**
     * Сериализует в JSON конфигурацию указанного менеджера компоновки компонент.
     * @param item  модель менеджера компоненовки.
     * @param out  выходной поток. Должен находиться в состоянии записи свойств компонента.
     * @throws IOException  в случае каких-либо проблем записи данных в поток.
     * @throws InvocationTargetException  в случае проблем с сериализацией данных в JSON.
     * @throws IllegalAccessException  в случае проблем с сериализацией данных в JSON.
     */
    public void renderItem(final LayoutItem item, final JsonWriter out) throws IOException, InvocationTargetException, IllegalAccessException;
}
