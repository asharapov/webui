package org.echosoft.framework.ui.extjs.widgets.form;

import java.util.Set;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;

/**
 * @author Anton Sharapov
 */
public class ComboBox extends AbstractField {

    public static final Set<String> EVENTS =
            StringUtil.asUnmodifiableSet(AbstractField.EVENTS, "autosize", "keydown", "keypress", "keyup");

    private Integer tabIndex;               //
    private boolean allowBlank;             // может ли поле быть пустым.
    private String emptyText;               // текст отображаемый в пустом поле ввода.

    private String valueField;
    private String displayField;

    public ComboBox() {
        this(null);
    }
    public ComboBox(final ComponentContext ctx) {
        super(ctx);
    }

    public Integer getTabIndex() {
        return tabIndex;
    }
    public void setTabIndex(final Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    /**
     * Возвращает <code>true</code> если поле ввода является валидным даже при отсутствии значения в нем.
     * @return <code>true</code> если поле валидно и при отсутствии значения в нем.
     *      По умолчанию возвращает <code>true</code>.
     */
    public boolean isAllowBlank() {
        return allowBlank;
    }
    /**
     * Определяет, является ли поле ввода валидным при отсутствии значения в нем.
     * @param allowBlank <code>true</code> если поле валидно и при отсутствии значения в нем.
     */
    public void setAllowBlank(final boolean allowBlank) {
        this.allowBlank = allowBlank;
    }

    /**
     * Возвращает текст который будет отображаться в пустом поле ввода. Этот же текст будет отправляться на сервер если
     * пользователь в этом поле так ничего и не напишет перед отправкой формы на сервер.
     * @return текст который будет отображаться в пустом поле ввода.
     */
    public String getEmptyText() {
        return emptyText;
    }
    /**
     * Указывает текст который будет отображаться в пустом поле ввода. Этот же текст будет отправляться на сервер если
     * пользователь в этом поле так ничего и не напишет перед отправкой формы на сервер.
     * @param emptyText текст который будет отображаться в пустом поле ввода.
     */
    public void setEmptyText(final String emptyText) {
        this.emptyText = emptyText;
    }





    @Override
    public void invoke(final JsonWriter out) throws Exception {
        final ComponentContext ctx = getContext();
//        setName( ctx.getClientId() + ".value" );
//        if (isStateful()) {
//            final String svalue = StringUtil.trim( (String)ctx.getAttribute("value", Scope.PR_ST) );
//            if (svalue!=null) {
//                value = svalue;
//            }
//            ctx.setAttribute("value", svalue, Scope.STATE);
//        }
        out.beginObject();
        out.writeProperty("xtype", "combo");
        renderContent(out);
        out.endObject();
    }

    @Override
    protected void renderContent(final JsonWriter out) throws Exception {
        super.renderContent(out);
//        out.writeProperty("name", name);
        if (tabIndex != null)
            out.writeProperty("tabIndex", tabIndex);
        if (!allowBlank)
            out.writeProperty("allowBlank", false);
        if (emptyText != null)
            out.writeProperty("emptyText", emptyText);
    }

    @Override
    protected Set<String> getSupportedEvents() {
        return ComboBox.EVENTS;
    }
}
