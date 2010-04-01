package org.echosoft.framework.ui.extjs;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.echosoft.common.json.JSFunction;
import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.UIComponent;
import org.echosoft.framework.ui.extjs.model.ComponentPlugin;

/**
 * Базовый класс, от которого наследуются все компоненты ExtJS.
 * @author Anton Sharapov
 */
public abstract class AbstractComponent implements UIComponent, Serializable {

    public static final Set<String> EVENTS = StringUtil.asUnmodifiableSet(
            "added", "afterrender", "beforedestroy", "beforehide", "beforerender",
            "beforeshow", "beforestaterestore", "beforestatesave","destroy", "disable",
            "enable", "hide", "removed", "render", "show",
            "staterestore", "statesave"
    );

    /**
     * Контекст компонента. Рекомендуется устанавливать непосредственно при создании экземпляра компонента,
     * хотя допускается отложенная установка контекста. Главное правило - контекст должен быть гарантированно установлен
     * перед вызовом метода {@link #invoke(org.echosoft.common.json.JsonWriter)} компонента.
     */
    private ComponentContext ctx;
    /**
     * Зарегистрированные в компоненте обработчики событий.
     */
    private Map<String,JSFunction> listeners;
    /**
     * Перечень плагинов, сопряженных с данным компонентом.
     */
    private Set<ComponentPlugin> plugins;

    private String cssClass;        // CSS класс, дополнительно применяемый к корневому элементу компонента.
    private String overCssClass;    // CSS класс, дополнительно применяемый к корневому элементу компонента в момент когда над ним находится курсор мышки.
    private String cssStyle;        // CSS стиль дополнительно применяемый к корневому элементу компонента.
    private boolean disabled;       // признак неактивности компонента (false по умолчанию).
    private boolean hidden;         // признак невидимости компонента (false по умолчанию).
    private String ref;             // позволяет определить ссылку на данный компонент в одном из иных компонентов иерархии.
    // свойства, определяющие положение данного компонента в родительском контейнере
    //   под управлением любого менеджера компоновки:
    private String ctCls;           // CSS класс который будет дополнительно добавлен к контейнеру в котором данный компонент расположен.
    private String itemId;          // идентификатор компонента в контейнере. Должен быть уникальным в рамках всех компонентов лежащих в данном контейнере.
    private boolean hideParent;     // признак, определяющий поведение контейнера в момент когда входящий в него компонент становится невидимым.
    //   под управлением <code>Ext.layout.FormLayout</code>:
    private String clearCls;        // CSS класс, дополнительно применяемый к области-разделителю между данным и нижележашим компонентами на форме.
    private String itemCls;         // CSS класс, дополнительно применяемый к области включающей в себя и сам компонент и метку к нему.
    private String labelStyle;      // CSS стиль, дополнительно применяемый к метке компонента.
    private String labelSeparator;  // текст (допускается html), используемый в разделителя между меткой и самим компонентом.
    private String fieldLabel;      // метка компонента на форме.
    private boolean hideLabel;      // позволяет скрыть метку к компоненту на форме.

    public AbstractComponent(final ComponentContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    public ComponentContext getContext() {
        return ctx;
    }
    /**
     * Позволяет установить контекст компонента в том случае если он еще не был установлен ранее.
     * @param ctx  идентификатор данного компонента.
     * @throws IllegalStateException  в случае если у данного компонента уже установлен контекст.
     */
    public void setContext(final ComponentContext ctx) {
        if (this.ctx!=null)
            throw new IllegalStateException("Context already specified for component");
        this.ctx = ctx;
    }

    /**
     * Возвращает CSS класс, который должен быть дополнительно применен к корневому DOM элементу компонента.
     * @return  имя класса или <code>null</code> в случае его отсутствия.
     */
    public String getCssClass() {
        return cssClass;
    }
    /**
     * Указывает CSS класс, который должен быть дополнительно применен к корневому DOM элементу компонента.
     * @param cssClass имя класса или <code>null</code> в случае его отсутствия.
     */
    public void setCssClass(final String cssClass) {
        this.cssClass = StringUtil.trim(cssClass);
    }

    /**
     * Возвращает CSS класс, который будет дополнительно применен к корневому DOM элементу компонента в те моменты когда над ним находится курсор мышки.<br/>
     * @return имя класса или <code>null</code> в случае его отсутствия.
     */
    public String getOverCssClass() {
        return overCssClass;
    }
    /**
     * Указывает CSS класс, который будет дополнительно применен к корневому DOM элементу компонента в те моменты когда над ним находится курсор мышки.<br/>
     * @param overCssClass имя класса или <code>null</code> в случае его отсутствия.
     */
    public void setOverCssClass(final String overCssClass) {
        this.overCssClass = StringUtil.trim(overCssClass);
    }

    /**
     * Возвращает CSS стили которые должны быть дополнительно применены к корневому элементу компонента.
     * @return  стили CSS или <code>null</code> в случае их отсутствия.
     */
    public String getCssStyle() {
        return cssStyle;
    }
    /**
     * Указывает CSS стили которые должны быть дополнительно применены к корневому элементу компонента.
     * @param cssStyle  стили CSS или <code>null</code> в случае их отсутствия.
     */
    public void setCssStyle(final String cssStyle) {
        this.cssStyle = StringUtil.trim(cssStyle);
    }

    /**
     * Возвращает <code>true</code> если компонент должен отображаться по умолчанию в неактивном состоянии.
     * @return <code>true</code>  если компонент должен отображаться в неактивном состоянии. По умолчанию возвращает <code>false</code>.
     */
    public boolean isDisabled() {
        return disabled;
    }
    /**
     * Указывает должен ли компонент отображаться по умолчанию в неактивном состоянии.
     * @param disabled <code>true</code>  если компонент должен отображаться в неактивном состоянии.
     */
    public void setDisabled(final boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * Возвращает <code>true</code> если компонент по умолчанию должен быть невидимым.
     * @return <code>true</code> если компонент должен быть невидимым. По умолчанию возвращает <code>false</code>.
     */
    public boolean isHidden() {
        return hidden;
    }
    /**
     * Указывает должен ли компонент по умолчанию быть невидимым.
     * @param hidden <code>true</code> если компонент должен быть невидимым. 
     */
    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }

    public String getRef() {
        return ref;
    }
    public void setRef(final String ref) {
        this.ref = ref;
    }

    //
    // Группа свойств, определяющих положение компонента в родительском контйнере ...
    //

    /**
     * Возвращает класс CSS который будет дополнительно добавлен к соответствующему DOM элементу контейнера в котором этот
     * компонент расположен.
     * @return имя CSS класса который будет дополнительно добавлен к соответствующему DOM элементу контейнера в котором этот
     * компонент расположен.
     */
    public String getCtCssClass() {
        return ctCls;
    }
    /**
     * Устанавливает класс CSS который будет дополнительно добавлен к соответствующему DOM элементу контейнера в котором этот
     * компонент расположен.
     * @param ctCls имя CSS класса который будет дополнительно добавлен к соответствующему DOM элементу контейнера в котором этот
     * компонент расположен.
     */
    public void setCtCssClass(final String ctCls) {
        this.ctCls = StringUtil.trim(ctCls);
    }

    /**
     * Возвращает альтернативный идентификатор компонента в контейнере, который позволяет однозначно идентифицировать компонент среди всех прочих дочерних компонент лежащих
     * в этом же контейнере компонент.
     * @return  альтернативный идентификатор дочернего компонента в контейнере. Обязан быть уникальным в рамках всех прочих дочерних компонент этого же контейнера. Может быть <code>null</code>.
     */
    public String getItemId() {
        return itemId;
    }
    /**
     * Устанавливает альтернативный идентификатор компонента в контейнере, который позволяет однозначно идентифицировать компонент среди всех прочих дочерних компонент лежащих
     * в этом же контейнере компонент.
     * @param itemId  альтернативный идентификатор дочернего компонента в контейнере. Обязан быть уникальным в рамках всех прочих дочерних компонент этого же контейнера. Может быть <code>null</code>.
     */
    public void setItemId(final String itemId) {
        this.itemId = StringUtil.trim(itemId);
    }

    /**
     * Определяет поведение контейнера в котором находится данный компонент в момент, когда последний становится видимым/невидимым.<br/>
     * Если данное свойство равно <code>true</code> то переключение компонента в невидимый режим автоматически переключает в невидимый режим
     * и весь контейнер в котором этот компонент расположен.
     * @return  <code>true</code> в случае когда при скрытии компонента должен быть скрыт и весь контейнер в котором этот компонент находится. По умолчанию возвращает <code>false</code>.
     */
    public boolean isHideParent() {
        return hideParent;
    }
    /**
     * Определяет поведение контейнера в котором находится данный компонент в момент, когда последний становится видимым/невидимым.<br/>
     * Если данное свойство равно <code>true</code> то переключение компонента в невидимый режим автоматически переключает в невидимый режим
     * и весь контейнер в котором этот компонент расположен.
     * @param hideParent  <code>true</code> в случае когда требуется при скрытии компонента автоматически скрывать и весь контейнер в котором этот компонент находится.
     */
    public void setHideParent(final boolean hideParent) {
        this.hideParent = hideParent;
    }


    /**
     * Возвращает класс CSS который требуется применить к области экрана, разделяющему данный и последующий компоненты на форме.
     * @return CSS класс дополнительно применяемый к области экрана между данным и последующим компонентами на форме.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public String getClearCls() {
        return clearCls;
    }
    /**
     * Задает класс CSS который требуется применить к области экрана, разделяющему данный и последующий компоненты на форме.
     * @param clearCls  CSS класс дополнительно применяемый к области экрана между данным и последующим компонентами на форме.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public void setClearCls(final String clearCls) {
        this.clearCls = StringUtil.trim(clearCls);
    }

    /**
     * Возвращает класс CSS который будет применен к области экрана включающей в себя и сам компонент и метку к нему.
     * @return класс CSS который будет применен к области экрана включающей в себя данный компонент и метку к нему.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public String getItemCls() {
        return itemCls;
    }
    /**
     * Задает класс CSS который требуется применить к области экрана включающей в себя и сам компонент и метку к нему.
     * @param itemCls класс CSS который будет применен к области экрана включающей в себя данный компонент и метку к нему.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public void setItemCls(final String itemCls) {
        this.itemCls = StringUtil.trim(itemCls);
    }

    /**
     * Возвращает стиль CSS который будет применен к метке для данного компонента.
     * @return стиль CSS который будет применен к метке для данного компонента.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public String getLabelStyle() {
        return labelStyle;
    }
    /**
     * Задает стиль CSS который будет применен к метке для данного компонента.
     * @param labelStyle стиль CSS который будет применен к метке для данного компонента.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public void setLabelStyle(final String labelStyle) {
        this.labelStyle = StringUtil.trim(labelStyle);
    }

    /**
     * Возвращает текст (допускается html) который будет помещен между меткой и компонентом.
     * Если данное свойство не указано то ExtJS будет использовать символ ':'.
     * @return текст, используемый в качестве разделителя между меткой и компонентом.
     *  Если свойство равно <code>null</code> то будет использоваться значение по умолчанию.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public String getLabelSeparator() {
        return labelSeparator;
    }
    /**
     * Задает текст (допускается html) который будет помещен между меткой и компонентом.
     * Если данное свойство не указано то ExtJS будет использовать символ ':'.
     * @param labelSeparator текст, используемый в качестве разделителя между меткой и компонентом.
     *  Если свойство равно <code>null</code> то будет использоваться значение по умолчанию.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public void setLabelSeparator(final String labelSeparator) {
        this.labelSeparator = labelSeparator;
    }

    /**
     * Возвращает текст (допускается html) используемый в качестве метки к данному компоненту на форме.
     * @return текст используемый в качестве метки к данному компоненту на форме.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public String getFieldLabel() {
        return fieldLabel;
    }
    /**
     * Задает текст (допускается html) используемый в качестве метки к данному компоненту на форме.
     * @param fieldLabel текст используемый в качестве метки к данному компоненту на форме.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public void setFieldLabel(final String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    /**
     * Данное свойство определяет будет ли отображаться для данного компонента метка на форме.
     * @return <code>true</code> если метка должна отсутствовать.
     *      По умолчанию возвращает <code>false</code>.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public boolean isHideLabel() {
        return hideLabel;
    }
    /**
     * Определяет будет ли отображаться для данного компонента метка на форме.
     * @param hideLabel <code>true</code> если метка должна отсутствовать.
     * @see <code>Ext.layout.FormLayout</code>
     */
    public void setHideLabel(final boolean hideLabel) {
        this.hideLabel = hideLabel;
    }


    /**
     * Подключает новый плагин к данному компоненту
     * @param plugin  ссылка на соответствующий экземпляр {@link ComponentPlugin}
     */
    public void addPlugin(final ComponentPlugin plugin) {
        if (plugin==null)
            throw new IllegalArgumentException("Plugin must be specified");
        if (plugins==null)
            plugins = new HashSet<ComponentPlugin>();
        plugins.add( plugin );
    }

    /**
     * Подключает новый обработчик определенного события.
     * @param eventName  имя события. Не может быть <code>null</code>.
     * @param handler  обработчик события.
     * @see #getSupportedEvents()
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
        return EVENTS;
    }


    /**
     * <p>Сериализует в javascript общие свойства компонента</p>
     * <p>Выходной JSON поток должен находиться в состоянии <i>редактирования свойств</i> объекта. 
     * @param out  выходной поток в который помещается формируемая модель ExtJS компонента в JSON формате.
     * @throws IOException  в случае каких-либо ошибок связанных с помещением данных в поток.
     * @throws InvocationTargetException  в случае ошибок в процессе сериализации данных в JSON формат.
     * @throws IllegalAccessException  в случае ошибок в процессе сериализации данных в JSON формат.
     */
    protected void renderAttrs(final JsonWriter out) throws Exception {
        if (ctx!=null)
            out.writeProperty("id", ctx.getClientId());
        if (ref!=null)
            out.writeProperty("ref", ref);
        if (cssClass!=null)
            out.writeProperty("cls", cssClass);
        if (overCssClass!=null)
            out.writeProperty("overCls", overCssClass);
        if (cssStyle!=null)
            out.writeProperty("style", cssStyle);
        if (disabled)
            out.writeProperty("disabled", true);
        if (hidden)
            out.writeProperty("hidden", true);

        if (ctCls!=null)
            out.writeProperty("ctCls", ctCls);
        if (itemId!=null)
            out.writeProperty("itemId", itemId);
        if (hideParent)
            out.writeProperty("hideParent", true);
        if (clearCls!=null)
            out.writeProperty("clearCls", clearCls);
        if (itemCls!=null)
            out.writeProperty("itemCls", itemCls);
        if (labelStyle!=null)
            out.writeProperty("labelStyle", labelStyle);
        if (labelSeparator!=null)
            out.writeProperty("labelSeparator", labelSeparator);
        if (fieldLabel!=null)
            out.writeProperty("fieldLabel", fieldLabel);
        if (hideLabel)
            out.writeProperty("hideLabel", true);

        if (listeners!=null && !listeners.isEmpty()) {
            out.writeComplexProperty("listeners");
            out.beginObject();
            for (Map.Entry<String,JSFunction> entry : listeners.entrySet()) {
                out.writeProperty(entry.getKey(), entry.getValue());
            }
            out.endObject();
        }
        if (plugins!=null && !plugins.isEmpty()) {
            out.writeComplexProperty("plugins");
            out.beginArray();
            for (ComponentPlugin plugin : plugins) {
                out.writeObject(plugin);
            }
            out.endArray();
        }
    }

    @Override
    public String toString() {
        return "[" + StringUtil.extractClass(getClass().getName()) + "{id:" + (ctx!=null?ctx.getClientId():"null") + "}]";
    }
}
