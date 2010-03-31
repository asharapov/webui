package org.echosoft.framework.ui.extjs.widgets;

import java.util.Set;

import org.echosoft.common.json.JSFunction;
import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.AbstractBoxComponent;
import org.echosoft.framework.ui.extjs.model.Align;

/**
 * Описывает простую кнопку.
 * @author Anton Sharapov
 */
public class Button extends AbstractBoxComponent {

    public static final Set<String> EVENTS = StringUtil.asUnmodifiableSet(AbstractBoxComponent.EVENTS,
            "click", "toggle",
            "menushow", "menuhide", "menutriggerout", "menutriggerover", "mouseout", "mouseover"
    );

    public static enum Scale {
        SMALL, MEDIUM, LARGE
    }
    public static enum Type {
        SUBMIT, RESET, BUTTON
    }

    private Boolean allowDepress;       // разрешает или нет возвращать кнопку из состояния "нажато".
    private Align arrowAlign;           // определяет где на кнопке рисовать стрелочки если компонент сопряжен с меню.
    private boolean enableToggle;       // разрешен ли режим в котором кнопка может находиться в двух состояниях (нажата и отжата).
    private boolean handleMouseEvents;  // следует ли компоненту обрабатывать события "onmouseXXX".
    private JSFunction handler;         // обработчик события "onclick".
    private String icon;                // путь до иконки которая будет использоваться в качестве фонового изображения.
    private String iconCls;             // CSS класс который должен выставлять для компонента фоновое изображение. Альтернатива icon.
    private Align iconAlign;            // определяет выравнивание изображения кнопки относительно ее текста.
//    private Object menu;
//    private Align menuAlign;
    private boolean pressed;            // находится ли кнопка в состоянии "нажато"; имеет смысл только при enableToggle=true.
    private Scale scale;                // размер кнопки (подстраиваемся под возможный размер иконки: 16px, 24px, 32px).
    private Integer tabIndex;
    private String text;                // html текст написанный на кнопке.
    private String toggleGroup;         // объединяет кнопки в группы. В группе в один момент времени нажатой может быть только одна кнопка.
    private JSFunction toggleHandler;   // обработчик переключения режимов кнопки.
    private String tooltip;             // всплывающая подсказка.
    private Type type;                  // тип кнопки.

    public Button() {
        this(null);
    }
    public Button(final ComponentContext ctx) {
        super(ctx);
        handleMouseEvents = true;
        arrowAlign = Align.RIGHT;
        iconAlign = Align.LEFT;
        scale = Scale.SMALL;
        type = Type.BUTTON;
    }

    /**
     * Данное свойство определяет возможность перевода кнопки из состояния "нажато" в состояние "отжато".
     * Имеет смысл только когда свойство {@link #getEnableToggle()} возвращает <code>true</code>.
     * @return <code>true</code> если допускается перевод кнопки из состояния "нажато" в состояние "отжато";
     *         <code>false</code> если такой перевод не допускается.
     *  По умолчанию метод возвращает <code>null</code>.
     */
    public Boolean getAllowDepress() {
        return allowDepress;
    }
    /**
     * Данное свойство определяет возможность перевода кнопки из состояния "нажато" в состояние "отжато".
     * Имеет смысл только когда свойство {@link #getEnableToggle()} возвращает <code>true</code>.
     * @param allowDepress <code>true</code> если допускается перевод кнопки из состояния "нажато" в состояние "отжато";
     *         <code>false</code> если такой перевод не допускается.
     *  По умолчанию метод возвращает <code>null</code>.
     */
    public void setAllowDepress(final Boolean allowDepress) {
        this.allowDepress = allowDepress;
    }

    /**
     * Данное свойство определяет расположение пиктограммы со стрелкой относительно текста на кнопке.
     * Имеет смысл только в том случае когда с данным компонентом ассоциировано меню.
     * @return область кнопки, отведенная под отображении пиктограмы со стрелкой.
     *          По умолчанию возвращает {@link Align#RIGHT}.
     */
    public Align getArrowAlign() {
        return arrowAlign;
    }
    /**
     * Данное свойство определяет расположение пиктограммы со стрелкой относительно текста на кнопке.
     * Имеет смысл только в том случае когда с данным компонентом ассоциировано меню.
     * @param arrowAlign область кнопки, отведенная под отображении пиктограмы со стрелкой.
     *   <code>null</code> будет транслировано в значение по умолчанию - {@link Align#RIGHT}.
     */
    public void setArrowAlign(final Align arrowAlign) {
        this.arrowAlign = arrowAlign!=null ? arrowAlign : Align.RIGHT;
    }

    /**
     * Данное свойство определяет возможность кнопки пребывать в одном из двух состояний "нажато", "отжато".
     * @return <code>true</code> если кнопка пребывает в одном из двух состояний: "нажато", "отжато".
     *      По умолчанию возвращает <code>false</code>.
     */
    public boolean getEnableToggle() {
        return enableToggle;
    }
    /**
     * Данное свойство определяет возможность кнопки пребывать в одном из двух состояний "нажато", "отжато".
     * @param enableToggle <code>true</code> если кнопка может пребывать в одном из двух состояний: "нажато", "отжато".
     */
    public void setEnableToggle(final boolean enableToggle) {
        this.enableToggle = enableToggle;
    }

    /**
     * Если <code>true</code> то кнопка обрабатывает события типа "onmousemove", "onmouseout".
     * @return <code>true</code> то кнопка обрабатывает события типа "onmousemove", "onmouseout".
     *      По умолчанию возвращает <code>true</code>.
     */
    public boolean isHandleMouseEvents() {
        return handleMouseEvents;
    }
    /**
     * Если <code>true</code> то кнопка обрабатывает события типа "onmousemove", "onmouseout".
     * @param handleMouseEvents если <code>true</code> то кнопка обрабатывает события типа "onmousemove", "onmouseout".
     */
    public void setHandleMouseEvents(final boolean handleMouseEvents) {
        this.handleMouseEvents = handleMouseEvents;
    }

    /**
     * Возвращает функцию используемую в качестве обработчика события возникающего каждый раз когда пользователь кликает по кнопке.
     * Данная функция имеет два аргумента:
     * <ol>
     *  <li> b : Ext.Button
     *  <li> e : Ext.EventObject
     * </ol>
     * @return  функция используемая в качестве обработчика события "click".
     */
    public JSFunction getHandler() {
        return handler;
    }
    /**
     * Назначает функцию используемую в качестве обработчика события возникающего каждый раз когда пользователь нажимает на кнопку.
     * Данная функция имеет два аргумента:
     * <ol>
     *  <li> b : Ext.Button
     *  <li> e : Ext.EventObject
     * </ol>
     * @param handler  функция используемая в качестве обработчика события "click".
     */
    public void setHandler(final JSFunction handler) {
        this.handler = handler;
    }

    /**
     * Возвращает ссылку на пиктограмму которая будет использоваться в качестве фонового изображения на данной кнопке.
     * @return ссылка на фоновое изображение для данной кнопки.
     */
    public String getIcon() {
        return icon;
    }
    /**
     * Указывает ссылку на пиктограмму которая будет использоваться в качестве фонового изображения на данной кнопке.
     * @param icon ссылка на фоновое изображение для данной кнопки.
     */
    public void setIcon(final String icon) {
        this.icon = icon;
    }

    /**
     * Возвращает название CSS класса, используемого для задания фонового изображения для данной кнопки.
     * Используется в качестве "продвинутой" альтернативы простому свойству <code>icon</code>.
     * @return  имя CSS класса используемого для задания фонового изображения для данной кнопки.
     */
    public String getIconCls() {
        return iconCls;
    }
    /**
     * Указывает название CSS класса, используемого для задания фонового изображения для данной кнопки.
     * Используется в качестве "продвинутой" альтернативы простому свойству <code>icon</code>.
     * @param iconCls  имя CSS класса используемого для задания фонового изображения для данной кнопки.
     */
    public void setIconCls(final String iconCls) {
        this.iconCls = StringUtil.trim(iconCls);
    }

    /**
     * Определяет способ расположения изображения относительно текста на кнопке.
     * @return  значение, определяющее где расположено изображение относительно текста.
     *      По умолчанию возвращает {@link Align#LEFT}.
     */
    public Align getIconAlign() {
        return iconAlign;
    }
    /**
     * Определяет способ расположения изображения относительно текста на кнопке.
     * @param iconAlign значение, определяющее где расположено изображение относительно текста.
     *      По умолчанию возвращает {@link Align#LEFT}.
     */
    public void setIconAlign(final Align iconAlign) {
        this.iconAlign = iconAlign!=null ? iconAlign : Align.LEFT;
    }

    /**
     * Возвращает начальное состояние кнопки.
     * Имеет смысл только когда свойство {@link #getEnableToggle()} возвращает <code>true</code>.
     * @return <code>true</code> если при кнопка будет по умолчанию находиться в состоянии "нажато".
     *      По умолчанию возвращает <code>false</code>.
     */
    public boolean isPressed() {
        return pressed;
    }
    /**
     * Определяет начальное состояние кнопки.
     * Имеет смысл только когда свойство {@link #getEnableToggle()} возвращает <code>true</code>.
     * @param pressed <code>true</code> если при кнопка будет по умолчанию находиться в состоянии "нажато".
     */
    public void setPressed(final boolean pressed) {
        this.pressed = pressed;
    }

    /**
     * Определяет размеры кнопки, адаптированные под использование пиктограм наиболее популярных размеров: 16px, 24px, 32px.
     * @return  размеры кнопки. По умолчанию возвращает {@link Scale#SMALL}.
     */
    public Scale getScale() {
        return scale;
    }
    /**
     * Определяет размеры кнопки, адаптированные под использование пиктограм наиболее популярных размеров: 16px, 24px, 32px.
     * @param scale размеры кнопки. Если <code>null</code> то будет использоваться размер по умолчанию {@link Scale#SMALL}. 
     */
    public void setScale(final Scale scale) {
        this.scale = scale!=null ? scale : Scale.SMALL;
    }

    /**
     * Возвращает значение свойства DOM <code>tabIndex</code> для данной кнопки.
     * @return свойство <code>tagIndex</code> для DOM элемента соответствующего данной кнопке.
     */
    public Integer getTabIndex() {
        return tabIndex;
    }
    /**
     * Устанавливает значение свойства DOM <code>tabIndex</code> для данной кнопки.
     * @param tabIndex значение свойства <code>tabIndex</code> для DOM элемента соответствующего данной кнопке.
     */
    public void setTabIndex(final Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    /**
     * Возвращает текст, который будет размещен на данной кнопке. Допускается использование HTML разметки.
     * @return текст на кнопке.
     */
    public String getText() {
        return text;
    }
    /**
     * Указывает текст, который будет размещен на данной кнопке. Допускается использование HTML разметки.
     * @param text текст на кнопке.
     */
    public void setText(final String text) {
        this.text = text;
    }

    /**
     * Возвращает код группы кнопок на странице к которой относится данный компонент.
     * Включение кнопки в группу дает дополнительные возможности по управлению ее состоянием. В частности в группе может быть
     * только одна кнопка в состоянии "нажато"
     * @return  группа кнопок в которую входит данная кнопка или <code>null</code>.
     */
    public String getToggleGroup() {
        return toggleGroup;
    }
    /**
     * Указывает код группы кнопок на странице к которой относится данный компонент.
     * Включение кнопки в группу дает дополнительные возможности по управлению ее состоянием. В частности в группе может быть
     * только одна кнопка в состоянии "нажато"
     * @param toggleGroup  группа кнопок в которую входит данная кнопка или <code>null</code>.
     */
    public void setToggleGroup(final String toggleGroup) {
        this.toggleGroup = toggleGroup;
    }

    /**
     * Возвращает функцию используемую в качестве обработчика события возникающего каждый раз когда пользователь переключает состояние кнопки.
     * Данная функция имеет два аргумента:
     * <ol>
     *  <li> b : Ext.Button
     *  <li> e : Ext.EventObject
     * </ol>
     * @return  функция используемая в качестве обработчика события "toggle".
     */
    public JSFunction getToggleHandler() {
        return toggleHandler;
    }
    /**
     * Назначает функцию используемую в качестве обработчика события возникающего каждый раз когда пользователь переключает состояние кнопки.
     * Данная функция имеет два аргумента:
     * <ol>
     *  <li> b : Ext.Button
     *  <li> e : Ext.EventObject
     * </ol>
     * @param toggleHandler  функция используемая в качестве обработчика события "toggle".
     */
    public void setToggleHandler(final JSFunction toggleHandler) {
        this.toggleHandler = toggleHandler;
    }

    /**
     * Возвращает всплывающую подсказку появляющуюся при наведении мышкой на для данный компонент.
     * @return Всплывающая подсказка для данного компонента.
     */
    public String getTooltip() {
        return tooltip;
    }
    /**
     * Указывает всплывающую подсказку появляющуюся при наведении мышкой на для данный компонент.
     * @param tooltip текст который будет отображаться во всплывающей подсказке для данного компонента.
     */
    public void setTooltip(final String tooltip) {
        this.tooltip = StringUtil.trim(tooltip);
    }

    /**
     * Возвращает тип кнопки.
     * @return тип кнопки. По умолчанию возвращает {@link Type#BUTTON}.
     */
    public Type getType() {
        return type;
    }
    /**
     * Указывает тип кнопки.
     * @param type тип кнопки. Если <code>null</code> то тип будет установлен в значение по умолчанию ({@link Type#BUTTON}).
     */
    public void setType(final Type type) {
        this.type = type!=null ? type : Type.BUTTON;
    }

    @Override
    public void invoke(final JsonWriter out) throws Exception {
        final ComponentContext ctx = getContext();
        ctx.getResources().attachScript( ctx.encodeThemeURL("/pkgs/pkg-buttons.js",false) );
        out.beginObject();
        out.writeProperty("xtype", "button");
        renderAttrs(out);
        out.endObject();
    }

    @Override
    protected Set<String> getSupportedEvents() {
        return Button.EVENTS;
    }

    @Override
    protected void renderAttrs(final JsonWriter out) throws Exception {
        super.renderAttrs(out);
        if (allowDepress!=null)
            out.writeProperty("allowDepress", allowDepress);
        if (arrowAlign!=Align.RIGHT)
            out.writeProperty("arrowAlign", arrowAlign.name().toLowerCase());
        if (enableToggle)
            out.writeProperty("enableToggle", true);
        if (!handleMouseEvents)
            out.writeProperty("handleMouseEvents", false);
        if (handler!=null)
            out.writeProperty("handler", handler);
        if (icon!=null) {
            out.writeProperty("icon", getContext().encodeURL(icon));
        }
        if (iconCls!=null)
            out.writeProperty("iconCls", iconCls);
        if (iconAlign!=Align.LEFT)
            out.writeProperty("iconAlign", iconAlign.name().toLowerCase());
        if (pressed)
            out.writeProperty("pressed", true);
        if (scale!=Scale.SMALL)
            out.writeProperty("scale", scale.name().toLowerCase());
        if (tabIndex!=null)
            out.writeProperty("tabIndex", tabIndex);
        if (text!=null)
            out.writeProperty("text", text);
        if (toggleGroup!=null)
            out.writeProperty("toggleGroup", toggleGroup);
        if (tooltip!=null)
            out.writeProperty("tooltip", tooltip);
        if (type!=Type.BUTTON)
            out.writeProperty("type", type.name().toLowerCase());
    }
}
