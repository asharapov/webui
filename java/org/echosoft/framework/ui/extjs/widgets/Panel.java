package org.echosoft.framework.ui.extjs.widgets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.echosoft.common.json.JSFunction;
import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.json.annotate.JsonUseSeriazer;
import org.echosoft.common.json.annotate.JsonWriteNulls;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.AbstractContainerComponent;
import org.echosoft.framework.ui.extjs.model.Template;
import org.echosoft.framework.ui.extjs.spi.model.EnumLCJSONSerializer;

/**
 * Панель.
 * @author Anton Sharapov
 */
public class Panel extends AbstractContainerComponent {

    public static Set<String> EVENTS =
            StringUtil.asUnmodifiableSet(AbstractContainerComponent.EVENTS,
                    "activate", "beforeclose", "beforecollapse", "beforeexpand", "bodyresize",
                    "close", "collapse", "deactivate", "expand", "iconchange", "titlechange");

    /**
     * Типы кнопок которые могут быть размещены на линейке инструментов в панели.
     */
    @JsonUseSeriazer(EnumLCJSONSerializer.class)
    public enum ToolButtonType {
        TOGGLE, CLOSE, MINIMIZE, MAXIMIZE, RESTORE,
        GEAR, PIN, UNPIN, RIGHT, LEFT,
        UP, DOWN, REFRESH, MINUS, PLUS,
        HELP, SEARCH, SAVE, PRINT
    }

    /**
     * Описывает дополнительные кнопки расположенные в заголовке панели.
     */
    @JsonWriteNulls(false)
    public class ToolButton implements Serializable {
        private final ToolButtonType id;      // тип кнопки.
        private JSFunction handler;     // обработчик нажатия на кнопку
        private String qtip;            // всплывающая подсказка для кнопки
        private boolean hidden;         // при загрузке страницы кнопку отрисовывать невидимой.

        public ToolButton(final ToolButtonType id) {
            if (id==null)
                throw new IllegalArgumentException("Tool type must be specified");
            this.id = id;
        }

        public ToolButtonType getId() {
            return id;
        }

        public JSFunction getHandler() {
            return handler;
        }
        public void setHandler(final JSFunction handler) {
            this.handler = handler;
        }

        public String getQtip() {
            return qtip;
        }
        public void setQtip(final String qtip) {
            this.qtip = qtip;
        }

        public boolean isHidden() {
            return hidden;
        }
        public void setHidden(final boolean hidden) {
            this.hidden = hidden;
        }
    }

    private Boolean animCollapse;       // Следует ли использовать анимацию при сворачивании/разворачивании панели.
    private String bodyCssClass;        // CSS класс применяемый к основному телу панели.
    private String bodyStyle;           // CSS стиль применяемый к основному телу панели.
    private String title;               // Заголовок панели.
    private String tabTip;              // всплывающая подсказка к заголовку панели. Используется в компоненте TabPanel.
    private boolean collapsible;        // Может ли содержимое панели схлопываться.
    private boolean collapsed;          // По умолчанию содержимое панели схлопнуто ?
    private boolean titleCollapse;      // Позволять ли схлопывание панели по клику на любое место заголовка ?.
    private boolean frame;              // Использовать или нет продвинутое оформление панели.
    private boolean border;             // Выделять или нет границы панели.
    private boolean preventBodyReset;   // Сброс ExtJS стилей в основном теле панели (имеет смысл если мы там отображаем HTML текст)
    private Toolbar tbar;               // Панель инструментов сверху панели.
    private Toolbar bbar;               // Панель инструментов внизу панели.
    private Toolbar fbar;               // Панель инструментов в самом низу панели.
    private List<ToolButton> tools;     // Кнопки расположенные в заголовке панели.
    private String html;
    private Template template;
    private Object data;

    public Panel() {
        this(null);
    }
    public Panel(final ComponentContext ctx) {
        super(ctx);
        this.border = true;
    }

    /**
     * Определяет следует ли анимировать сворачивание/разворачивание панели.
     * @return <code>true</code>  если требуется анимация; <code>false</code> если анимация не требуется;
     *         <code>null</code> (по умолчанию) если значение данного свойства должно определяться внутри ExtJS.
     */
    public Boolean isAnimCollapse() {
        return animCollapse;
    }
    /**
     * Задает признак определяющий следует ли анимировать сворачивание/разворачивание панели.
     * @param animCollapse <code>true</code>  если требуется анимация; <code>false</code> если анимация не требуется;
     *     <code>null</code> (по умолчанию) если значение данного свойства должно определяться внутри ExtJS.
     */
    public void setAnimCollapse(final Boolean animCollapse) {
        this.animCollapse = animCollapse;
    }

    /**
     * Возвращает CSS класс который будет применяться к элементу внутри которого расположен основной контент панели.
     * @return CSS класс который будет применяться к основному контенту панели.
     */
    public String getBodyCssClass() {
        return bodyCssClass;
    }
    /**
     * Указывает CSS класс который будет применяться к элементу внутри которого расположен основной контент панели.
     * @param bodyCssClass CSS класс который будет применяться к основному контенту панели.
     */
    public void setBodyCssClass(final String bodyCssClass) {
        this.bodyCssClass = StringUtil.trim(bodyCssClass);
    }

    /**
     * Возвращает стиль CSS который будет применяться к элементу внутри которого расположен основной контент панели.
     * @return стиль CSS который будет применяться к основному контенту панели.
     */
    public String getBodyStyle() {
        return bodyStyle;
    }
    /**
     * Указывает стиль CSS который будет применяться к элементу внутри которого расположен основной контент панели.
     * @param bodyStyle стиль CSS который будет применяться к основному контенту панели.
     */
    public void setBodyStyle(final String bodyStyle) {
        this.bodyStyle = StringUtil.trim(bodyStyle);
    }

    /**
     * Возвращает текст, отображаемый в заголовке панели.
     * @return текст отображаемый в заголовке панели или <code>null</code> если в заголовке отображать название не требуется.
     * По умолчанию возвращает <code>null</code>.
     */
    public String getTitle() {
        return title;
    }
    /**
     * Задает текст, отображаемый в заголовке панели.
     * @param title текст отображаемый в заголовке панели или <code>null</code> если в заголовке отображать название не требуется.
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Возвращает текст отображаемый во всплывающей подсказке к заголовку для данной панели.
     * Используется в компоненте <code>TabPanel</code>.
     * @return текст всплывающей подсказки к заголовку для данной панели.
     */
    public String getTabTip() {
        return tabTip;
    }
    /**
     * Указывает текст который должен отображаться во всплывающей подсказке к заголовку для данной панели.
     * Используется в компоненте <code>TabPanel</code>.
     * @param tabTip текст всплывающей подсказки к заголовку для данной панели.
     */
    public void setTabTip(final String tabTip) {
        this.tabTip = tabTip;
    }

    /**
     * Возвращает <code>true</code> если содержимое данной панели можно сворачивать/разворачивать.
     * @return <code>true</code> если содержимое данной панели можно сворачивать/разворачивать.
     *      По умолчанию возвращает <code>false</code>.
     */
    public boolean isCollapsible() {
        return collapsible;
    }
    /**
     * Задает <code>true</code> если содержимое данной панели можно сворачивать/разворачивать.
     * @param collapsible <code>true</code> если содержимое данной панели можно сворачивать/разворачивать.
     */
    public void setCollapsible(final boolean collapsible) {
        this.collapsible = collapsible;
    }

    /**
     * Возвращает состояние панели по умолчанию (состояние на момент загрузки страницы)
     * @return <code>true</code> если содержимое панели по умолчанию свернуто; 
     *         <code>false</code> если содержимое панели по умолчанию развернуто.
     */
    public boolean isCollapsed() {
        return collapsed;
    }
    /**
     * Задает состояние панели по умолчанию (состояние на момент загрузки страницы)
     * @param collapsed <code>true</code> если содержимое панели по умолчанию свернуто;
     *         <code>false</code> если содержимое панели по умолчанию развернуто.
     */
    public void setCollapsed(final boolean collapsed) {
        this.collapsed = collapsed;
    }

    /**
     * Возвращает <code>true</code> если при клике по заголовку панели должно происходить
     * сворачивание или разворачивание содержимого панели (в зависимости от текущего состояния).
     * Имеет смысл только когда {@link #isCollapsible()} = true.
     * @return <code>true</code> если при клике по заголовку панели должно происходить сворачивание/разворачивание
     * содержимого панели.
     */
    public boolean isTitleCollapse() {
        return titleCollapse;
    }
    /**
     * Задает <code>true</code> если при клике по заголовку панели должно происходить
     * сворачивание или разворачивание содержимого панели (в зависимости от текущего состояния).
     * Имеет смысл только когда {@link #isCollapsible()} = true.
     * @param titleCollapse <code>true</code> если при клике по заголовку панели должно происходить сворачивание/разворачивание
     * содержимого панели.
     */
    public void setTitleCollapse(final boolean titleCollapse) {
        this.titleCollapse = titleCollapse;
    }

    /**
     * Возвращает <code>true</code> если требуется продвинутое оформление панели.
     * Пример оформления панели в различных состояниях:<pre><code>
     * // frame = false
     * &lt;div id="developer-specified-id-goes-here" class="x-panel">
     *   &lt;div class="x-panel-header">&lt;span class="x-panel-header-text">Title: (frame:false)&lt;/span>&lt;/div>
     *   &lt;div class="x-panel-bwrap">
     *     &lt;div class="x-panel-body">&lt;p>html value goes here&lt;/p>&lt;/div>
     *   &lt;/div>
     * &lt;/div>
     * // frame = true (create 9 elements)
     * &lt;div id="developer-specified-id-goes-here" class="x-panel">
     *   &lt;div class="x-panel-tl">&lt;div class="x-panel-tr">&lt;div class="x-panel-tc">
     *     &lt;div class="x-panel-header">&lt;span class="x-panel-header-text">Title: (frame:true)&lt;/span>&lt;/div>
     *   &lt;/div>&lt;/div>&lt;/div>
     *  &lt;div class="x-panel-bwrap">
     *    &lt;div class="x-panel-ml">&lt;div class="x-panel-mr">&lt;div class="x-panel-mc">
     *      &lt;div class="x-panel-body">&lt;p>html value goes here&lt;/p>&lt;/div>
     *    &lt;/div>&lt;/div>&lt;/div>
     *    &lt;div class="x-panel-bl">&lt;div class="x-panel-br">&lt;div class="x-panel-bc"/>
     *    &lt;/div>&lt;/div>&lt;/div>
     * &lt;/div>
     * </code></pre>
     * @return <code>true</code> если требуется продвинутое оформление панели. По умолчанию возвращает <code>false</code>.
     */
    public boolean getFrame() {
        return frame;
    }
    /**
     * Задает <code>true</code> если требуется продвинутое оформление панели.
     * @param frame <code>true</code> если требуется продвинутое оформление панели. По умолчанию возвращает <code>false</code>.
     */
    public void setFrame(final boolean frame) {
        this.frame = frame;
    }

    /**
     * Возвращает <code>true</code> если требуется отображать границы панели.
     * @return <code>true</code> если требуется отображать границы панели.
     *  По умолчанию возвращает <code>true</code>.
     */
    public boolean getBorder() {
        return border;
    }
    /**
     * Задает <code>true</code> если требуется отображать границы панели.
     * @param border <code>true</code> если требуется отображать границы панели.
     *  По умолчанию возвращает <code>true</code>.
     */
    public void setBorder(final boolean border) {
        this.border = border;
    }

    /**
     * Возвращает <code>true</code> если требуется добавить CSS класс <code>x-panel-reset</code> к корневому элементу панели.
     * Данный класс используется для сброса всех специфичных для ExtJS стилей в теле панели.
     * @return <code>true</code> если требуется добавить CSS класс <code>x-panel-reset</code> к корневому элементу панели.
     *  По умолчанию возвращает <code>false</code>.
     */
    public boolean isPreventBodyReset() {
        return preventBodyReset;
    }
    /**
     * Задает <code>true</code> если требуется добавить CSS класс <code>x-panel-reset</code> к корневому элементу панели.
     * Данный класс используется для сброса всех специфичных для ExtJS стилей в теле панели.
     * @param preventBodyReset <code>true</code> если требуется добавить CSS класс <code>x-panel-reset</code> к корневому элементу панели.
     *  По умолчанию возвращает <code>false</code>.
     */
    public void setPreventBodyReset(final boolean preventBodyReset) {
        this.preventBodyReset = preventBodyReset;
    }

    /**
     * Возвращает ссылку на линейку инструментов расположенную в верхней части панели.
     * @return ссылку на линейку инструментов расположенную в верхней части панели или <code>null</code>.
     */
    public Toolbar getTopToolbar() {
        return tbar;
    }
    /**
     * Указывает ссылку на линейку инструментов которая должна располагаться в верхней части панели.
     * @param tbar ссылку на линейку инструментов которая должна располагаться в верхней части панели или <code>null</code>.
     */
    public void setTopToolbar(final Toolbar tbar) {
        this.tbar = tbar;
    }
    public Toolbar assignTopToolbar() {
        this.tbar = new Toolbar();
        return tbar;
    }
    public Toolbar assignTopToolbar(final Toolbar tbar) {
        this.tbar = tbar;
        return tbar;
    }

    /**
     * Возвращает ссылку на линейку инструментов расположенную в нижней части панели.
     * @return ссылку на линейку инструментов расположенную в нижней части панели или <code>null</code>.
     */
    public Toolbar getBottomToolbar() {
        return bbar;
    }
    /**
     * Указывает ссылку на линейку инструментов которая должна располагаться в нижней части панели.
     * @param bbar ссылку на линейку инструментов которая должна располагаться в нижней части панели или <code>null</code>.
     */
    public void setBottomToolbar(final Toolbar bbar) {
        this.bbar = bbar;
    }
    public Toolbar assignBottomToolbar() {
        this.bbar = new Toolbar();
        return bbar;
    }
    public Toolbar assignBottomToolbar(final Toolbar bbar) {
        this.bbar = bbar;
        return bbar;
    }

    /**
     * Возвращает ссылку на линейку инструментов расположенную в самой нижней части панели.
     * @return ссылку на линейку инструментов расположенную в самой нижней части панели или <code>null</code>.
     */
    public Toolbar getFooter() {
        return fbar;
    }
    /**
     * Указывает ссылку на линейку инструментов расположенную в самой нижней части панели.
     * @param fbar ссылка на линейку инструментов расположенную в самой нижней части панели или <code>null</code>.
     */
    public void setFooter(final Toolbar fbar) {
        this.fbar = fbar;
    }
    public Toolbar assignFooter() {
        this.fbar = new Toolbar();
        return fbar;
    }
    public Toolbar assignFooter(final Toolbar fbar) {
        this.fbar = fbar;
        return fbar;
    }

    public int getToolButtonsCount() {
        return tools!=null ? tools.size() : 0;
    }
    public Iterable<ToolButton> getToolButtons() {
        return tools!=null ? tools : Collections.<ToolButton>emptyList();
    }
    public ToolButton appendToolButton(final ToolButtonType id) {
        if (tools==null)
            tools = new ArrayList<ToolButton>(4);
        final ToolButton result = new ToolButton(id);
        tools.add(result);
        return result;
    }


    /**
     * Возвращает фрагмент html кода который будет отображаться в качестве основного содержимого панели.
     * @return содержимое компонента.
     */
    public String getHtml() {
        return html;
    }
    /**
     * Указывает фрагмент html кода который будет в качестве основного содержимого панели.
     * @param html содержимое компонента.
     */
    public void setHtml(final String html) {
        this.html = html;
    }

    /**
     * Возвращает шаблон фрагмента html который будет использоваться в качестве содержимого данной панели.
     * Если шаблон не задан то содержимое панели будет вычисляться на основе свойств <code>html</code> и
     * <code>contentEl</code> компонента.
     * Используется совместно со свойством <code>data</code>.
     * @return шаблон используемый для вычисления содержимого данного компонента или <code>null</code>.
     */
    public Template getTemplate() {
        return template;
    }
    /**
     * Задает шаблон фрагмента html который будет использоваться в качестве содержимого данной панели.
     * Если шаблон не задан то содержимое панели будет вычисляться на основе свойств <code>html</code> и
     * <code>contentEl</code> компонента.
     * Используется совместно со свойством <code>data</code>.
     * @param template шаблон используемый для вычисления содержимого данного компонента или <code>null</code>.
     */
    public void setTemplate(final Template template) {
        this.template = template;
    }

    /**
     * Возвращает данные которые будут использоваться при вычислении содержимого компонента на основе шаблона.
     * Используется совместно со свойством <code>template</code>.
     * @return данные для шаблона.
     */
    public Object getData() {
        return data;
    }
    /**
     * Указывает данные которые будут использоваться при вычислении содержимого компонента на основе шаблона.
     * Используется совместно со свойством <code>template</code>.
     * @param data данные для шаблона.
     */
    public void setData(final Object data) {
        this.data = data;
    }


    @Override
    public void invoke(final JsonWriter out) throws Exception {
        out.beginObject();
        out.writeProperty("xtype", "panel");
        renderContent(out);
        out.endObject();
    }

    @Override
    protected void renderContent(final JsonWriter out) throws Exception {
        super.renderContent(out);
        if (animCollapse != null)
            out.writeProperty("animCollapse", animCollapse);
        if (bodyCssClass != null)
            out.writeProperty("bodyCssClass", bodyCssClass);
        if (bodyStyle != null)
            out.writeProperty("bodyStyle", bodyStyle);
        if (title != null)
            out.writeProperty("title", title);
        if (tabTip != null)
            out.writeProperty("tabTip", tabTip);
        if (collapsible)
            out.writeProperty("collapsible", true);
        if (collapsed)
            out.writeProperty("collapsed", true);
        if (titleCollapse)
            out.writeProperty("titleCollapse", true);
        if (frame)
            out.writeProperty("frame", true);
        if (!border)
            out.writeProperty("border", false);
        if (preventBodyReset)
            out.writeProperty("preventBorderReset", true);
        if (tbar != null) {
            out.writeComplexProperty("tbar");
            tbar.invoke(out);
        }
        if (bbar != null) {
            out.writeComplexProperty("bbar");
            bbar.invoke(out);
        }
        if (fbar != null) {
            out.writeComplexProperty("fbar");
            fbar.invoke(out);
        }
        if (tools != null) {
            out.writeComplexProperty("tools");
            out.beginArray();
            for (ToolButton tb : tools) {
                out.writeObject(tb);
            }
            out.endArray();
        }
        if (html != null)
            out.writeProperty("html", html);
        if (template != null)
            out.writeProperty("tpl", template);
        if (data != null)
            out.writeProperty("data", data);
    }

    @Override
    protected Set<String> getSupportedEvents() {
        return Panel.EVENTS;
    }

}
