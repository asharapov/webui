package org.echosoft.framework.ui.extjs.widgets.form;

import java.util.Set;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;

/**
 * @author Anton Sharapov
 */
public class Radio extends AbstractField {

    public static final Set<String> EVENTS =
            StringUtil.asUnmodifiableSet(AbstractField.EVENTS, "check");

    private String name;                    // имя параметра в котором будет на сервер отправлено значение поля.
    private String boxLabel;                // текст расположенный справа от переключателя.
    private String inputValue;              // значение отправляемое на сервер когда данный переключатель выбран.
    private boolean checked;                // выбран ли данный переключатель.

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

    /**
     * Значение отправляемое компонентом на сервер в случае помещения данного переключателя в положение "включено".
     * @return значение отправляемое компонентом на сервер в случае помещения данного переключателя в положение "включено".
     */
    public String getInputValue() {
        return inputValue;
    }
    /**
     * Указывает значение отправляемое компонентом на сервер в случае помещения данного переключателя в положение "включено".
     * @param inputValue значение отправляемое компонентом на сервер в случае помещения данного переключателя в положение "включено".
     */
    public void setInputValue(final String inputValue) {
        this.inputValue = inputValue;
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

    @Override
    public void invoke(final JsonWriter out) throws Exception {
        final ComponentContext ctx = getContext();
        if (getName()==null)
            setName( ctx.getClientId() + ".value" );
        out.beginObject();
        out.writeProperty("xtype", "radio");
        renderContent(out);
        out.endObject();
    }

    @Override
    public void renderContent(final JsonWriter out) throws Exception {
        super.renderContent(out);
        out.writeProperty("name", name);
        if (boxLabel!=null)
            out.writeProperty("boxLabel", boxLabel);
        if (inputValue!=null)
            out.writeProperty("inputValue", inputValue);
        out.writeProperty("checked", checked);
    }

    @Override
    protected Set<String> getSupportedEvents() {
        return Radio.EVENTS;
    }

}