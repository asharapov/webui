package org.echosoft.framework.ui.extjs;

import java.io.Serializable;
import java.io.Writer;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.Application;
import org.echosoft.framework.ui.core.Page;
import org.echosoft.framework.ui.core.UIContext;

/**
 * Базовый вариант класса, описывающего модель страницы построенной на базе фреймворка ExtJS.
 * @author Anton Sharapov
 */
public class ExtJSPage extends Page implements Serializable {

    private AbstractContainerComponent container;

    public ExtJSPage(final UIContext uctx) {
        super(uctx);
    }

    /**
     * Возвращает контейнер в котором (будет) расположен весь видимый контент генерируемой страницы.
     * @return  корневой контейнер в котором будет размещен прямо или опосредовано весь видимый контент страницы. Может вернуть <code>null</code>.
     */
    public AbstractContainerComponent getContainer() {
        return container;
    }
    /**
     * Устанавливает контейнер в котором будет размещен весь видимый контент в странице.
     * @param container  корневой контейнер в котором будет размещен прямо или опосредовано весь видимый контент страницы.
     */
    public void setContainer(final AbstractContainerComponent container) {
        this.container = container;
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
        out.writeProperty("layout", "fit");
        out.writeComplexProperty("items");
        if (container!=null) {
            container.invoke(out);
        } else {
            out.beginArray();
            out.endArray();
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
        final JsonWriter jw = getJsonContext().makeJsonWriter(out);
        jw.beginObject();
        jw.writeProperty("location", uctx.encodeURL("/"));
        jw.writeProperty("theme", uctx.encodeThemeURL("/",true));
        jw.writeProperty("version", Application.VERSION);
        jw.writeProperty("state", Application.getStateSerializer().encodeState(uctx));
        jw.writeProperty("viewport", null);
        jw.endObject();
        out.write('\n');
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
