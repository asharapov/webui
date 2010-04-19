package org.echosoft.framework.ui.extjs.widgets.form;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.Scope;

/**
 * Компонент для ввода целых чисел однозначно приводимых к типу Long.
 * @author Anton Sharapov
 */
public class LongField extends AbstractTextField {

    private Long value;                     // значение в данном поле ввода.
    private Long minValue;                  // минимально допустимое значение.
    private Long maxValue;                  // максимально допустимое значение.

    public LongField() {
        this(null);
    }
    public LongField(final ComponentContext ctx) {
        super(ctx);
    }


    /**
     * Возвращает значение данного поля.
     * @return  значение данного поля.
     */
    public Long getValue() {
        return value;
    }
    /**
     * Устанавливает значение данного поля.
     * @param value значение данного поля.
     */
    public void setValue(final Long value) {
        this.value = value;
    }

    /**
     * Возвращает минимально допустимое значение в данном поле.
     * @return минимально допустимое значение.
     */
    public Long getMinValue() {
        return minValue;
    }
    /**
     * Устанавливает минимально допустимое значение в данном поле.
     * @param minValue минимально допустимое значение.
     */
    public void setMinValue(final Long minValue) {
        this.minValue = minValue;
    }

    /**
     * Возвращает максимально допустимое значение в данном поле.
     * @return максимально допустимое значение.
     */
    public Long getMaxValue() {
        return maxValue;
    }
    /**
     * Устанавливает максимально допустимое значение в данном поле.
     * @param maxValue максимально допустимое значение.
     */
    public void setMaxValue(final Long maxValue) {
        this.maxValue = maxValue;
    }


    @Override
    public void invoke(final JsonWriter out) throws Exception {
        final ComponentContext ctx = getContext();
        setName( ctx.getClientId() + ".value" );
        if (isStateful()) {
            final String svalue = StringUtil.trim( (String)ctx.getAttribute("value", Scope.PR_ST) );
            if (svalue!=null) {
                value = Long.parseLong(svalue,10);
            }
            ctx.setAttribute("value", svalue, Scope.STATE);
        }
        out.beginObject();
        out.writeProperty("xtype", "numberfield");
        renderContent(out);
        out.endObject();
    }

    @Override
    protected void renderContent(final JsonWriter out) throws Exception {
        super.renderContent(out);
        if (value!=null)
            out.writeProperty("value", value);
        out.writeProperty("allowDecimals", false);
        out.writeProperty("minValue", minValue!=null ? minValue : Long.MIN_VALUE);
        out.writeProperty("maxValue", maxValue!=null ? maxValue : Long.MAX_VALUE);
    }

}