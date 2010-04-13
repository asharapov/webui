package org.echosoft.framework.ui.extjs.widgets.form;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.Scope;

/**
 * Компонент для ввода целых чисел однозначно приводимых к типу Integer.
 * @author Anton Sharapov
 */
public class IntegerField extends AbstractTextField {

    private Integer value;                  // значение в данном поле ввода.
    private Integer minValue;               // минимально допустимое значение.
    private Integer maxValue;               // максимально допустимое значение.

    public IntegerField() {
        this(null);
    }
    public IntegerField(final ComponentContext ctx) {
        super(ctx);
    }


    /**
     * Возвращает значение данного поля.
     * @return  значение данного поля.
     */
    public Integer getValue() {
        return value;
    }
    /**
     * Устанавливает значение данного поля.
     * @param value значение данного поля.
     */
    public void setValue(final Integer value) {
        this.value = value;
    }

    /**
     * Возвращает минимально допустимое значение в данном поле.
     * @return минимально допустимое значение.
     */
    public Integer getMinValue() {
        return minValue;
    }
    /**
     * Устанавливает минимально допустимое значение в данном поле.
     * @param minValue минимально допустимое значение.
     */
    public void setMinValue(final Integer minValue) {
        this.minValue = minValue;
    }

    /**
     * Возвращает максимально допустимое значение в данном поле.
     * @return максимально допустимое значение.
     */
    public Integer getMaxValue() {
        return maxValue;
    }
    /**
     * Устанавливает максимально допустимое значение в данном поле.
     * @param maxValue максимально допустимое значение.
     */
    public void setMaxValue(final Integer maxValue) {
        this.maxValue = maxValue;
    }


    @Override
    public void invoke(final JsonWriter out) throws Exception {
        final ComponentContext ctx = getContext();
        setName( ctx.getClientId() + ".value" );
        if (isStateful()) {
            final String svalue = ctx.getAttribute("value", Scope.PR_ST);
            if (svalue!=null && !svalue.isEmpty()) {
                ctx.setAttribute("value", svalue, Scope.STATE);
                value = Integer.parseInt(svalue,10);
            }
        }
        out.beginObject();
        out.writeProperty("xtype", "numberfield");
        renderAttrs(out);
        out.endObject();
    }

    @Override
    protected void renderAttrs(final JsonWriter out) throws Exception {
        super.renderAttrs(out);
        if (value!=null)
            out.writeProperty("value", value);
        out.writeProperty("allowDecimals", false);
        out.writeProperty("minValue", minValue!=null ? minValue : Integer.MIN_VALUE);
        out.writeProperty("maxValue", maxValue!=null ? maxValue : Integer.MAX_VALUE);
    }

}
