package org.echosoft.framework.ui.extjs.widgets;

import java.util.Set;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.extjs.AbstractContainerComponent;
import org.echosoft.framework.ui.extjs.layout.ToolbarLayout;

/**
 * Панель инструментов.
 * @author Anton Sharapov
 */
public class Toolbar extends AbstractContainerComponent {

    public static enum Align {
        LEFT, CENTER, RIGHT
    }
    public static final Set<String> EVENTS = StringUtil.asUnmodifiableSet(AbstractContainerComponent.EVENTS, "overflowchange");

    private Align buttonAlign;              // способ выравнивания кнопок в тулбаре
    private boolean enableOverflow;         // требуется ли обработка выхода кнопок за пределы тулбара

    public Toolbar() {
        this(null);
    }
    public Toolbar(final ComponentContext ctx) {
        super(ctx);
        buttonAlign = Align.LEFT;
        setLayout( new ToolbarLayout() );
    }

    /**
     * Регистрирует новый вспомогательный элемент-разделитель на панели инструментов.
     * @return новый элемент типа {@link ToolbarSpacer}
     */
    public ToolbarSpacer addSpacer() {
        return getLayout().append( new ToolbarSpacer() );
    }

    /**
     * Регистрирует новый вспомогательный элемент-разделитель на панели инструментов.
     * @return новый элемент типа {@link ToolbarSeparator}
     */
    public ToolbarSeparator addSeparator() {
        return getLayout().append( new ToolbarSeparator() );
    }

    /**
     * Регистрирует новый вспомогательный элемент-разделитель на панели инструментов.
     * @return новый элемент типа {@link ToolbarFill}
     */
    public ToolbarFill addFill() {
        return getLayout().append( new ToolbarFill() );
    }

    /**
     * Регистрирует новый компонент {@link Button} на панели инструментов.
     * @param text  текст на кнопке.
     * @return новый экземпляр компонента {@link Button} который был помещен на панель инструментов.<br/>
     *  <strong>Внимание!</strong> в возвращаемом экземпляре не установлен контекст выполнения.
     */
    public Button addButton(final String text) {
        final Button button = getLayout().append( new Button() );
        button.setText(text);
        return button;
    }

    /**
     * Создает и регистрирует новый компонент {@link ButtonGroup} на панели инструментов.
     * @param title  текст заголовка.
     * @param columns  максимальное кол-во дочерних компонент которые будут расположены на одной строке.
     * @return новый экземпляр компонента {@link Button} который был помещен на панель инструментов.<br/>
     *  <strong>Внимание!</strong> в возвращаемом экземпляре не установлен контекст выполнения.
     */
    public ButtonGroup addButtonGroup(final String title, final Integer columns) {
        final ButtonGroup bg = getLayout().append( new ButtonGroup() );
        bg.setTitle( title );
        bg.setColumns( columns );
        return bg;
    }

    /**
     * Возвращает способ выравнивания элементов панели инструментов.
     * @return выбранный способ выравнивания для элементов панели инструментов.
     *  По умолчанию возвращает {@link Align#LEFT}.
     */
    public Align getButtonAlign() {
        return buttonAlign;
    }
    /**
     * Задает способ выравнивания элементов панели инструментов.
     * @param buttonAlign способ выравнивания для элементов панели инструментов.
     *  если <code>null</code> то будет установлено значение по умолчанию: {@link Align#LEFT}.
     */
    public void setButtonAlign(final Align buttonAlign) {
        this.buttonAlign = buttonAlign!=null ? buttonAlign : Align.LEFT;
    }

    /**
     * Возвращает <code>true</code> если панель инструментов должна обрабатывать ситуацию когда
     * элементы панели не умещаются в отведенном им пространстве.
     * По умолчанию возвращает <code>false</code>
     * @return <code>true</code> если требуется определение и соотв. обработка ситуации когда элементы панели
     * не умещаются в отведенном им пространстве. По умолчанию возвращает <code>false</code>.
     */
    public boolean isEnableOverflow() {
        return enableOverflow;
    }
    /**
     * Указывает требуется или нет определение и специальная обработка ситуации когда
     * элементы панели не умещаются в отведенном им пространстве. По умолчанию свойство возвращает <code>false</code>.
     * @param enableOverflow <code>true</code> если требуется определение и соотв. обработка ситуации когда элементы панели
     * не умещаются в отведенном им пространстве. 
     */
    public void setEnableOverflow(final boolean enableOverflow) {
        this.enableOverflow = enableOverflow;
    }

    @Override
    public void invoke(final JsonWriter out) throws Exception {
        final ComponentContext ctx = getContext();
        ctx.getResources().attachScript( ctx.encodeThemeURL("/pkgs/pkg-toolbars.js",false) );
        out.beginObject();
        out.writeProperty("xtype", "toolbar");
        renderAttrs(out);
        out.endObject();
    }

    @Override
    protected Set<String> getSupportedEvents() {
        return EVENTS;
    }

    @Override
    protected void renderAttrs(final JsonWriter out) throws Exception {
        super.renderAttrs(out);
        if (buttonAlign!=Align.LEFT)
            out.writeProperty("buttonAlign", buttonAlign);
        if (enableOverflow)
            out.writeProperty("enableOverflow", true);
    }
}
