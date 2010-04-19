package org.echosoft.framework.ui.extjs;

import java.io.Serializable;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.echosoft.common.json.JSFunction;
import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.Application;
import org.echosoft.framework.ui.core.Page;
import org.echosoft.framework.ui.core.UIComponent;
import org.echosoft.framework.ui.core.UIContext;
import org.echosoft.framework.ui.extjs.layout.AutoLayout;
import org.echosoft.framework.ui.extjs.layout.Layout;

/**
 * Базовый вариант класса, описывающего модель страницы построенной на базе фреймворка ExtJS.
 * @author Anton Sharapov
 */
public class ExtJSPage extends Page implements Serializable {

    /**
     * Зарегистрированные в компоненте обработчики событий.
     */
    private Map<String,JSFunction> listeners;
    private Layout layout;

    public ExtJSPage(final UIContext uctx) {
        super(uctx);
        layout = new AutoLayout();
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
     * Если компонент, являющийся наследником данного класса поддерживает иной перечень событий то он должен переопределить данный метод.
     * @return  неизменяемый перечень событий, поддерживаемых данным компонентом. Метод никогда не возвращает <code>null</code>.
     */
    protected Set<String> getSupportedEvents() {
        return AbstractContainerComponent.EVENTS;
    }

    /**
     * Возвращает менеджер упаковки компонент в контейнере.
     * По умолчанию используется компоновщик {@link AutoLayout}.
     * @return используемый в настоящий момент упаковщик компонент. Никогда не возвращает <code>null</code>.
     */
    public Layout getLayout() {
        return layout;
    }
    /**
     * Устанавливает менеджер упаковки компонент в контейнере.<br/>
     * При смене типа упаковщика метод для каждого компонента в контейнере заменяет информацию о его расположении в контейнере.
     * @param layout  упаковщик компонент или <code>null</code> если требуется использовать упаковщик по умолчанию.
     */
    public void setLayout(final Layout layout) {
        this.layout = layout!=null ? layout : new AutoLayout();
    }
    /**
     * Устанавливает менеджер упаковки компонент в контейнере и возвращает ссылку на него.<br/>
     * При смене типа упаковщика метод для каждого компонента в контейнере заменяет информацию о его расположении в контейнере.
     * @param layout  упаковщик компонент или <code>null</code> если требуется использовать упаковщик по умолчанию.
     * @return используемый в настоящий момент упаковщик компонент. Никогда не возвращает <code>null</code>.
     * @see #getLayout()
     * @see #setLayout(Layout)
     */
    @SuppressWarnings("unchecked")
    public <T extends Layout> T assignLayout(final T layout) {
        setLayout(layout);
        return (T)this.layout;
    }

    /**
     * Возвращает количество компонент помещенных в данный контейнер.
     * @return  кол-во компонент в данном контейнере.
     */
    public int getItemsCount() {
        return layout!=null ? layout.getItemsCount() : 0;
    }

    /**
     * Возвращает итератор по всем компонентам помещенным в данный контейнер.
     * @return итератор по всем компонентам помещенным в данный контейнер.
     */
    public Iterable<UIComponent> getItems() {
        return layout!=null ? layout.getItems() : Collections.<UIComponent>emptyList();
    }

    /**
     * Регистрирует новый компонент в данном регионе.
     * @param  item  новый компонент который должен быть добавлен в данный регион.
     * @return  Добавленный в регион компонент.
     */
    public <T extends UIComponent> T append(final T item) {
        return getLayout().append(item);
    }


    /**
     * Метод унаследованный от интерфейса {@link org.echosoft.framework.ui.core.UIComponent}.
     * Как правило используется для декорирования отображаемых на странице основных данных.
     * Часто к данной задаче относится отображение заголовков, подвалов и навигационных меню на страницах.
     * Т.е. того контента который слабо или вообще никак не меняется от страницы к странице в рамках определенного веб приложения.
     * @param out  выходной поток в который компонент должен вывести свою модель в формате JSON.
     * @throws Exception  поднимается в случае каких-либо проблем.
     */
    public void invoke(final JsonWriter out) throws Exception {
        out.getOutputWriter().write("WUI.viewport = new Ext.Viewport(");
        out.beginObject();
        out.writeProperty("id", getContext().getClientId());
        layout.serialize(out);
        if (listeners!=null && !listeners.isEmpty()) {
            out.writeComplexProperty("listeners");
            out.beginObject();
            for (Map.Entry<String,JSFunction> entry : listeners.entrySet()) {
                out.writeProperty(entry.getKey(), entry.getValue());
            }
            out.endObject();
        }
        out.endObject();
        out.getOutputWriter().write(");\n");
    }

    /**
     * Метод отвечает за генерацию javascript кода, отвечающего за инициализацию фреймворка на стороне клиента.
     * @param uctx  контекст обработки текущего запроса.
     * @param out  выходной поток куда должен быть помещен сгенерированный код.
     * @throws Exception  в случае каких-либо проблем.
     */
    protected void init(final UIContext uctx, final Writer out) throws Exception {
        out.write("Ext.BLANK_IMAGE_URL=\"");
        out.write( uctx.encodeThemeURL("/resources/images/default/s.gif", false) );
        out.write("\";\n");
        out.write("Ext.QuickTips.init();\n");
        out.write("Ext.namespace(\"WUI\");\n");
        out.write("WUI.env = ");
        JsonWriter jw = getJsonContext().makeJsonWriter(out);
        jw.beginObject();
        jw.writeProperty("location", uctx.encodeURL("/"));
        jw.writeProperty("theme", uctx.encodeThemeURL("/",true));
        jw.writeProperty("version", Application.VERSION);
        jw.endObject();
        out.write(";\n");
        out.write("WUI.params = ");
        jw = getJsonContext().makeJsonWriter(out);
        jw.beginObject();
        jw.writeProperty("__VIEWSTATE", Application.getStateSerializer().encodeState(uctx));
        jw.endObject();
        out.write(";\n");
    }

    /**
     * Если код инициализации состояния страницы на стороне клиента должен быть выполнен по наступлению определенного события то
     * данный метод должен вернуть имя функции в вызов которой будет передан код отвечающий за инициализацию страницы.
     * @return  имя функции в которую должен быть передан код инициализации страницы или <code>null</code> если этот код инициализации
     * надо будет выполнить еще на стадии загрузки DOM страницы.
     */
    protected String getInitCallbackFunction() {
        return "Ext.onReady";
    }
}
