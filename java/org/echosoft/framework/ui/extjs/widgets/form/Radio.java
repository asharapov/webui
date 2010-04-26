package org.echosoft.framework.ui.extjs.widgets.form;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.Scope;

/**
 * @author Anton Sharapov
 */
public class Radio extends AbstractField {

    private String name;                    // имя параметра в котором будет на сервер отправлено значение поля.
    private boolean checked;                // значение компонента.
    private String boxLabel;                // текст расположенный справа от переключателя.

    public Radio() {
        this(null);
    }
    public Radio(final ComponentContext ctx) {
        super(ctx);
    }

    /**
     * Возвращает имя параметра в запросе где будет сохраняться значение данного компонента.
     * По умолчанию вычисляется как:
     * <pre>
     *      <code>name = getContext().getClientId() + ".value";</code>
     * </pre>
     * @return имя параметра в запросе где будет сохраняться значение данного компонента.
     */
    public String getName() {
        return name;
    }
    /**
     * Указывает имя параметра в запросе где будет сохраняться значение данного компонента.
     * По умолчанию вычисляется как:
     * <pre>
     *      <code>name = getContext().getClientId() + ".value";</code>
     * </pre>
     * @param name имя параметра в запросе где будет сохраняться значение данного компонента.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Возвращает значение данного поля.
     * @return  значение данного поля.
     */
    public boolean isChecked() {
        return checked;
    }
    /**
     * Указывает текст отображаемый в данном поле.
     * @param checked значение данного поля.
     */
    public void setChecked(final boolean checked) {
        this.checked = checked;
    }

    /**
     * Возвращает текст, отображаемый справа от переключателя.
     * @return текст, отображаемый справа от переключателя.
     */
    public String getBoxLabel() {
        return boxLabel;
    }
    /**
     * Устанавливает текст, который должен отображаться справа от переключателя.
     * @param boxLabel текст, отображаемый справа от переключателя.
     */
    public void setBoxLabel(final String boxLabel) {
        this.boxLabel = boxLabel;
    }

    @Override
    public void invoke(final JsonWriter out) throws Exception {
        final ComponentContext ctx = getContext();
        setName( ctx.getClientId() + ".value" );
        if (isStateful()) {
            final String svalue = StringUtil.trim( (String)ctx.getAttribute("value", Scope.PR_ST) );
            if (svalue!=null) {
                checked = "true".equals(svalue.toLowerCase());
            }
            ctx.setAttribute("value", svalue, Scope.STATE);
        }
        out.beginObject();
        out.writeProperty("xtype", "radio");
        renderContent(out);
        out.endObject();
    }

    @Override
    public void renderContent(final JsonWriter out) throws Exception {
        super.renderContent(out);
        out.writeProperty("name", name);
        out.writeProperty("checked", checked);
        if (boxLabel!=null)
            out.writeProperty("boxLabel", boxLabel);
    }
}