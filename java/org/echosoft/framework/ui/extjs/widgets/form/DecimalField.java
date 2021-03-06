package org.echosoft.framework.ui.extjs.widgets.form;

import java.math.BigDecimal;

import org.echosoft.common.json.JsonWriter;
import org.echosoft.common.utils.StringUtil;
import org.echosoft.framework.ui.core.ComponentContext;
import org.echosoft.framework.ui.core.Scope;

/**
 * Компонент для ввода чисел с фиксированным количеством знаков до и после запятой.
 * @author Anton Sharapov
 */
public class DecimalField extends AbstractTextField {

    private static final int DEFAULT_PRECISION = 2;

    private BigDecimal value;               // значение в данном поле ввода.
    private BigDecimal minValue;            // минимально допустимое значение.
    private BigDecimal maxValue;            // максимально допустимое значение.
    private int precision;                  // количество знаков после запятой.

    public DecimalField() {
        this(null);
    }
    public DecimalField(final ComponentContext ctx) {
        super(ctx);
        precision = DEFAULT_PRECISION;
    }


    /**
     * Возвращает значение данного поля.
     * @return  значение данного поля.
     */
    public BigDecimal getValue() {
        return value;
    }
    /**
     * Устанавливает значение данного поля.
     * @param value значение данного поля.
     */
    public void setValue(final BigDecimal value) {
        this.value = value;
    }

    /**
     * Возвращает минимально допустимое значение в данном поле.
     * @return минимально допустимое значение.
     */
    public BigDecimal getMinValue() {
        return minValue;
    }
    /**
     * Устанавливает минимально допустимое значение в данном поле.
     * @param minValue минимально допустимое значение.
     */
    public void setMinValue(final BigDecimal minValue) {
        this.minValue = minValue;
    }

    /**
     * Возвращает максимально допустимое значение в данном поле.
     * @return максимально допустимое значение.
     */
    public BigDecimal getMaxValue() {
        return maxValue;
    }
    /**
     * Устанавливает максимально допустимое значение в данном поле.
     * @param maxValue максимально допустимое значение.
     */
    public void setMaxValue(final BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Возвращает максимально допустимое количество знаков в дробной части числа.
     * @return максимально допустимое количество знаков в дробной части числа.
     *  Значение свойства по умолчанию: <code>2</code>.
     */
    public int getPrecision() {
        return precision;
    }
    /**
     * Указывает максимально допустимое количество знаков в дробной части числа.
     * @param precision максимально допустимое количество знаков в дробной части числа.
     */
    public void setPrecision(final int precision) {
        this.precision = precision>=0 ? precision : DEFAULT_PRECISION;
    }


    @Override
    public void invoke(final JsonWriter out) throws Exception {
        final ComponentContext ctx = getContext();
        if (getName()==null)
            setName( ctx.getClientId() + ".value" );
        if (isStateful()) {
            final String svalue = StringUtil.trim( (String)ctx.getAttribute("value", Scope.PR_ST) );
            if (svalue!=null) {
                value = new BigDecimal(svalue);
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
        if (value != null)
            out.writeProperty("value", value);
        if (minValue != null)
            out.writeProperty("minValue", minValue);
        if (maxValue != null)
            out.writeProperty("maxValue", maxValue);
        if (precision != DEFAULT_PRECISION)
            out.writeProperty("decimalPrecision", precision);
    }

}