package org.echosoft.framework.ui.extjs.widgets.form;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.Scope;

/**
 * Отвечает за ввод многострочного текста.
 * @author Anton Sharapov
 */
public class TextAreaField extends AbstractTextField {

    private String value;                   // текст в поле ввода.

    public TextAreaField() {
        this(null);
    }
    public TextAreaField(final ComponentContext ctx) {
        super(ctx);
    }

    /**
     * Возвращает значение данного поля.
     * @return  значение данного поля.
     */
    public String getValue() {
        return value;
    }
    /**
     * Указывает текст отображаемый в данном поле.
     * @param value значение данного поля.
     */
    public void setValue(final String value) {
        this.value = value;
    }


    @Override
    public void invoke(final JsonWriter out) throws Exception {
        final ComponentContext ctx = getContext();
        setName( ctx.getClientId() + ".value" );
        if (isStateful()) {
            final String svalue = ctx.getAttribute("value", Scope.PR_ST);
            if (svalue!=null) {
                ctx.setAttribute("value", svalue, Scope.STATE);
                value = svalue;
            }
        }
        out.beginObject();
        out.writeProperty("xtype", "textarea");
        renderContent(out);
        out.endObject();
    }

    @Override
    protected void renderContent(final JsonWriter out) throws Exception {
        super.renderContent(out);
        if (value!=null)
            out.writeProperty("value", value);
    }

}